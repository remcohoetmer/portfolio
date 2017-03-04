package nl.remco.scope.service;

import org.assertj.core.api.AbstractAssert;

import nl.remco.scope.service.Scope;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
class ScopeAssert extends AbstractAssert<ScopeAssert, Scope> {

    private ScopeAssert(Scope actual) {
        super(actual, ScopeAssert.class);
    }

    static ScopeAssert assertThatScope(Scope actual) {
        return new ScopeAssert(actual);
    }

    ScopeAssert hasName(String expectedDescription) {
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

    ScopeAssert hasId(String expectedId) {
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

    ScopeAssert hasNoDescription() {
        isNotNull();

        String actualDescription = actual.getName();
        assertThat(actualDescription)
                .overridingErrorMessage("Expected description to be <null> but was <%s>", actualDescription)
                .isNull();

        return this;
    }

    ScopeAssert hasNoId() {
        isNotNull();

        String actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%s>", actualId)
                .isNull();

        return this;
    }

    ScopeAssert hasStatus(String expectedTitle) {
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
