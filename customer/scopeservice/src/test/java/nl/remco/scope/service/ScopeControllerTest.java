package nl.remco.scope.service;

import nl.remco.scope.error.RestErrorHandler;
import nl.remco.scope.service.ScopeController;
import nl.remco.scope.service.ScopeDTO;
import nl.remco.scope.service.ScopeNotFoundException;
import nl.remco.scope.service.ScopeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;

import static nl.remco.scope.service.ScopeDTOAssert.assertThatTodoDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class ScopeControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String STATUS = "status";

    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_STATUS = 20;

    @Mock
    private ScopeService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ScopeController(service))
                .setHandlerExceptionResolvers(withExceptionControllerAdvice())
                .build();
    }

    /**
     * For some reason this does not work. The correct error handler method is invoked but when it tries
     * to return the validation errors as json, the following error appears to log:
     *
     * Failed to invoke @ExceptionHandler method:
     * public com.javaadvent.bootrest.error.ValidationErrorDTO com.javaadvent.bootrest.error.RestErrorHandler.processValidationError(org.springframework.web.bind.MethodArgumentNotValidException)
     * org.springframework.web.HttpMediaTypeNotAcceptableException: Could not find acceptable representation
     *
     * I have to figure out how to fix this before I can write the unit tests that ensure that validation is working.
     */
    private ExceptionHandlerExceptionResolver withExceptionControllerAdvice() {
        final ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(final HandlerMethod handlerMethod,
                                                                              final Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestErrorHandler.class).resolveMethod(exception);
                if (method != null) {
                    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
                    messageSource.setBasename("messages");
                    return new ServletInvocableHandlerMethod(new RestErrorHandler(messageSource), method);
                }
                return super.getExceptionHandlerMethod(handlerMethod, exception);
            }
        };
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    @Test
    public void create_TodoEntryWithOnlyTitle_ShouldCreateNewTodoEntryWithoutDescription() throws Exception {
        ScopeDTO newTodoEntry = new ScopeDTOBuilder()
                .status(STATUS)
                .build();

        mockMvc.perform(post("/api/scope")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        );

        ArgumentCaptor<ScopeDTO> createdArgument = ArgumentCaptor.forClass(ScopeDTO.class);
        verify(service, times(1)).create(createdArgument.capture());
        verifyNoMoreInteractions(service);

        ScopeDTO created = createdArgument.getValue();
        assertThatTodoDTO(created)
                .hasNoId()
                .hasTitle(STATUS)
                .hasNoDescription();
    }

    @Test
    public void create_TodoEntryWithOnlyTitle_ShouldReturnResponseStatusCreated() throws Exception {
        ScopeDTO newTodoEntry = new ScopeDTOBuilder()
                .status(STATUS)
                .build();

        when(service.create(isA(ScopeDTO.class))).then(invocationOnMock -> {
            ScopeDTO saved = (ScopeDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/scope")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void create_TodoEntryWithOnlyTitle_ShouldReturnTheInformationOfCreatedTodoEntryAsJSon() throws Exception {
        ScopeDTO newTodoEntry = new ScopeDTOBuilder()
                .status(STATUS)
                .build();

        when(service.create(isA(ScopeDTO.class))).then(invocationOnMock -> {
            ScopeDTO saved = (ScopeDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/scope")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.name", isEmptyOrNullString()));
    }

    @Test
    public void create_TodoEntryWithMaxLengthTitleAndDescription_ShouldCreateNewTodoEntryWithCorrectInformation() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        ScopeDTO newTodoEntry = new ScopeDTOBuilder()
                .status(maxLengthTitle)
                .name(maxLengthDescription)
                .build();

        mockMvc.perform(post("/api/scope")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        );

        ArgumentCaptor<ScopeDTO> createdArgument = ArgumentCaptor.forClass(ScopeDTO.class);
        verify(service, times(1)).create(createdArgument.capture());
        verifyNoMoreInteractions(service);

        ScopeDTO created = createdArgument.getValue();
        assertThatTodoDTO(created)
                .hasNoId()
                .hasTitle(maxLengthTitle)
                .hasDescription(maxLengthDescription);
    }

    @Test
    public void create_TodoEntryWithMaxLengthTitleAndDescription_ShouldReturnResponseStatusCreated() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        ScopeDTO newTodoEntry = new ScopeDTOBuilder()
                .status(maxLengthTitle)
                .name(maxLengthDescription)
                .build();

        when(service.create(isA(ScopeDTO.class))).then(invocationOnMock -> {
            ScopeDTO saved = (ScopeDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/scope")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newTodoEntry))
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void create_TodoEntryWithMaxLengthTitleAndDescription_ShouldReturnTheInformationOfCreatedTodoEntryAsJson() throws Exception {
        String maxLengthStatus = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthName = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        ScopeDTO newScopeEntry = new ScopeDTOBuilder()
                .status(maxLengthStatus)
                .name(maxLengthName)
                .build();

        when(service.create(isA(ScopeDTO.class))).then(invocationOnMock -> {
            ScopeDTO saved = (ScopeDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/scope")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newScopeEntry))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.status", is(maxLengthStatus)))
                .andExpect(jsonPath("$.name", is(maxLengthName)));
    }

    @Test
    public void delete_TodoEntryNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        when(service.delete(ID)).thenThrow(new ScopeNotFoundException(ID));

        mockMvc.perform(delete("/api/scope/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_TodoEntryFound_ShouldReturnResponseStatusOk() throws Exception {
        ScopeDTO deleted = new ScopeDTOBuilder()
                .id(ID)
                .build();

        when(service.delete(ID)).thenReturn(deleted);

        mockMvc.perform(delete("/api/scope/{id}", ID))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_TodoEntryFound_ShouldTheInformationOfDeletedTodoEntryAsJson() throws Exception {
        ScopeDTO deleted = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        when(service.delete(ID)).thenReturn(deleted);

        mockMvc.perform(delete("/api/scope/{id}", ID))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    @Test
    public void findAll_ShouldReturnResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/scope"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAll_OneTodoEntryFound_ShouldReturnListThatContainsOneTodoEntryAsJson() throws Exception {
        ScopeDTO found = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        when(service.findAll()).thenReturn(Arrays.asList(found));

        mockMvc.perform(get("/api/scope"))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ID)))
                .andExpect(jsonPath("$[0].status", is(STATUS)))
                .andExpect(jsonPath("$[0].name", is(NAME)));
    }

    @Test
    public void findById_TodoEntryFound_ShouldReturnResponseStatusOk() throws Exception {
        ScopeDTO found = new ScopeDTOBuilder().build();

        when(service.findById(ID)).thenReturn(found);

        mockMvc.perform(get("/api/scope/{id}", ID))
                .andExpect(status().isOk());
    }

    @Test
    public void findById_TodoEntryFound_ShouldTheInformationOfFoundTodoEntryAsJson() throws Exception {
        ScopeDTO found = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        when(service.findById(ID)).thenReturn(found);

        mockMvc.perform(get("/api/scope/{id}", ID))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    @Test
    public void findById_TodoEntryNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        when(service.findById(ID)).thenThrow(new ScopeNotFoundException(ID));

        mockMvc.perform(get("/api/scope/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_TodoEntryWithOnlyTitle_ShouldUpdateTheInformationOfTodoEntry() throws Exception {
        ScopeDTO updatedTodoEntry = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .build();

        mockMvc.perform(put("/api/scope/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        );

        ArgumentCaptor<ScopeDTO> updatedArgument = ArgumentCaptor.forClass(ScopeDTO.class);
        verify(service, times(1)).update(updatedArgument.capture());
        verifyNoMoreInteractions(service);

        ScopeDTO updated = updatedArgument.getValue();
        assertThatTodoDTO(updated)
                .hasId(ID)
                .hasTitle(STATUS)
                .hasNoDescription();
    }

    @Test
    public void update_TodoEntryWithOnlyTitle_ShouldReturnResponseStatusOk() throws Exception {
        ScopeDTO updatedTodoEntry = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .build();

        when(service.update(isA(ScopeDTO.class))).then(invocationOnMock -> (ScopeDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/scope/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void update_TodoEntryWithOnlyTitle_ShouldReturnTheInformationOfUpdatedTodoEntryAsJSon() throws Exception {
        ScopeDTO updatedTodoEntry = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .build();

        when(service.update(isA(ScopeDTO.class))).then(invocationOnMock ->  (ScopeDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/scope/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.name", isEmptyOrNullString()));
    }

    @Test
    public void update_TodoEntryWithMaxLengthTitleAndDescription_ShouldUpdateTheInformationOfTodoEntry() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        ScopeDTO updatedTodoEntry = new ScopeDTOBuilder()
                .id(ID)
                .status(maxLengthTitle)
                .name(maxLengthDescription)
                .build();

        mockMvc.perform(put("/api/scope/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        );

        ArgumentCaptor<ScopeDTO> updatedArgument = ArgumentCaptor.forClass(ScopeDTO.class);
        verify(service, times(1)).update(updatedArgument.capture());
        verifyNoMoreInteractions(service);

        ScopeDTO updated = updatedArgument.getValue();
        assertThatTodoDTO(updated)
                .hasId(ID)
                .hasTitle(maxLengthTitle)
                .hasDescription(maxLengthDescription);
    }

    @Test
    public void update_TodoEntryWithMaxLengthTitleAndDescription_ShouldReturnResponseStatusOk() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        ScopeDTO updatedTodoEntry = new ScopeDTOBuilder()
                .id(ID)
                .status(maxLengthTitle)
                .name(maxLengthDescription)
                .build();

        when(service.create(isA(ScopeDTO.class))).then(invocationOnMock -> (ScopeDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/scope/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void update_TodoEntryWithMaxLengthTitleAndDescription_ShouldReturnTheInformationOfCreatedUpdatedTodoEntryAsJson() throws Exception {
        String maxLengthStatus = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthName = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        ScopeDTO updatedTodoEntry = new ScopeDTOBuilder()
                .id(ID)
                .status(maxLengthStatus)
                .name(maxLengthName)
                .build();

        when(service.update(isA(ScopeDTO.class))).then(invocationOnMock -> (ScopeDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/scope/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedTodoEntry))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.status", is(maxLengthStatus)))
                .andExpect(jsonPath("$.name", is(maxLengthName)));
    }
}
