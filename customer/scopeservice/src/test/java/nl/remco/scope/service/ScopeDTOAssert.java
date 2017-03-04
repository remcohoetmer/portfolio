package nl.remco.scope.service;

import org.assertj.core.api.AbstractAssert;

import nl.remco.scope.service.ScopeDTO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
class ScopeDTOAssert extends AbstractAssert<ScopeDTOAssert, ScopeDTO> {

    private ScopeDTOAssert(ScopeDTO actual) {
        super(actual, ScopeDTOAssert.class);
    }

    static ScopeDTOAssert assertThatTodoDTO(ScopeDTO actual) {
        return new ScopeDTOAssert(actual);
    }

    public ScopeDTOAssert hasDescription(String expectedDescription) {
        isNotNull();

        String actualDescription = actual.getName();
        assertThat(actualDescription)
                .overridingErrorMessage("Expected description to be <%s> but was <%s>",
                        expectedDescription,
                        actualDescription
                )
                .isEqualTo(expectedDescription);

        return this;
    }

    public ScopeDTOAssert hasId(String expectedId) {
        isNotNull();

        String actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <%s> but was <%s>",
                        expectedId,
                        actualId
                )
                .isEqualTo(expectedId);

        return this;
    }

    public ScopeDTOAssert hasNoDescription() {
        isNotNull();

        String actualDescription = actual.getName();
        assertThat(actualDescription)
                .overridingErrorMessage("expected description to be <null> but was <%s>", actualDescription)
                .isNull();

        return this;
    }

    public ScopeDTOAssert hasNoId() {
        isNotNull();

        String actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%s>", actualId)
                .isNull();

        return this;
    }

    public ScopeDTOAssert hasTitle(String expectedTitle) {
        isNotNull();

        String actualTitle = actual.getStatus();
        assertThat(actualTitle)
                .overridingErrorMessage("Expected title to be <%s> but was <%s>",
                        expectedTitle,
                        actualTitle
                )
                .isEqualTo(expectedTitle);

        return this;
    }
}
