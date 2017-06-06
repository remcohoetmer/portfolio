package nl.remco.scope.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nl.remco.scope.service.ScopeAssert.assertThatScope;
import static nl.remco.scope.service.ScopeDTOAssert.assertThatTodoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoDbScopeServiceTest {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String STATUS = "status";

    @Mock
    private ScopeRepository repository;

    private MongoDBScopeService service;

    @Before
    public void setUp() {
        this.service = new MongoDBScopeService(repository);
    }

    @Test
    public void create_ShouldSaveNewTodoEntry() {
        ScopeDTO newTodo = new ScopeDTOBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        when(repository.save(isA(Scope.class))).thenAnswer(invocation -> (Scope) invocation.getArguments()[0]);

        service.create(newTodo);

        ArgumentCaptor<Scope> savedTodoArgument = ArgumentCaptor.forClass(Scope.class);

        verify(repository, times(1)).save(savedTodoArgument.capture());
        verifyNoMoreInteractions(repository);

        Scope savedTodo = savedTodoArgument.getValue();
        assertThatScope(savedTodo)
                .hasStatus(STATUS)
                .hasName(NAME);
    }

    @Test
    public void create_ShouldReturnTheInformationOfCreatedTodoEntry() {
        ScopeDTO newTodo = new ScopeDTOBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        when(repository.save(isA(Scope.class))).thenAnswer(invocation -> {
            Scope persisted = (Scope) invocation.getArguments()[0];
            ReflectionTestUtils.setField(persisted, "id", ID);
            return persisted;
        });

        ScopeDTO returned = service.create(newTodo);

        assertThatTodoDTO(returned)
                .hasId(ID)
                .hasTitle(STATUS)
                .hasDescription(NAME);
    }

    @Test(expected = ScopeNotFoundException.class)
    public void delete_TodoEntryNotFound_ShouldThrowException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        service.findById(ID);
    }

    @Test
    public void delete_TodoEntryFound_ShouldDeleteTheFoundTodoEntry() {
        Scope deleted = new ScopeBuilder()
                .id(ID)
                .build();

        when(repository.findById(ID)).thenReturn(Optional.of(deleted));

        service.delete(ID);

        verify(repository, times(1)).delete(deleted);
    }

    @Test
    public void delete_TodoEntryFound_ShouldReturnTheDeletedTodoEntry() {
        Scope deleted = new ScopeBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        when(repository.findById(ID)).thenReturn(Optional.of(deleted));

        ScopeDTO returned = service.delete(ID);

        assertThatTodoDTO(returned)
                .hasId(ID)
                .hasTitle(STATUS)
                .hasDescription(NAME);
    }

    @Test
    public void findAll_OneTodoEntryFound_ShouldReturnTheInformationOfFoundTodoEntry() {
        Scope expected = new ScopeBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(expected));

        List<ScopeDTO> todoEntries = service.findAll();
        assertThat(todoEntries).hasSize(1);

        ScopeDTO actual = todoEntries.iterator().next();
        assertThatTodoDTO(actual)
                .hasId(ID)
                .hasTitle(STATUS)
                .hasDescription(NAME);
    }

    @Test(expected = ScopeNotFoundException.class)
    public void findById_TodoEntryNotFound_ShouldThrowException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        service.findById(ID);
    }

    @Test
    public void findById_TodoEntryFound_ShouldReturnTheInformationOfFoundTodoEntry() {
        Scope found = new ScopeBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        when(repository.findById(ID)).thenReturn(Optional.of(found));

        ScopeDTO returned = service.findById(ID);

        assertThatTodoDTO(returned)
                .hasId(ID)
                .hasTitle(STATUS)
                .hasDescription(NAME);
    }

    @Test(expected = ScopeNotFoundException.class)
    public void update_UpdatedTodoEntryNotFound_ShouldThrowException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        ScopeDTO updated = new ScopeDTOBuilder()
                .id(ID)
                .build();

        service.update(updated);
    }

    @Test
    public void update_UpdatedTodoEntryFound_ShouldSaveUpdatedTodoEntry() {
        Scope existing = new ScopeBuilder()
                .id(ID)
                .build();

        when(repository.findById(ID)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ScopeDTO updated = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        service.update(updated);

        verify(repository, times(1)).save(existing);
        assertThatScope(existing)
                .hasId(ID)
                .hasStatus(STATUS)
                .hasName(NAME);
    }

    @Test
    public void update_UpdatedTodoEntryFound_ShouldReturnTheInformationOfUpdatedTodoEntry() {
        Scope existing = new ScopeBuilder()
                .id(ID)
                .build();

        when(repository.findById(ID)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ScopeDTO updated = new ScopeDTOBuilder()
                .id(ID)
                .status(STATUS)
                .name(NAME)
                .build();

        ScopeDTO returned = service.update(updated);
        assertThatTodoDTO(returned)
                .hasId(ID)
                .hasTitle(STATUS)
                .hasDescription(NAME);
    }
}
