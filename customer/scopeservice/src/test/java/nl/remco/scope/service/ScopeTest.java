package nl.remco.scope.service;

import static nl.remco.scope.service.ScopeAssert.assertThatScope;

import org.junit.Test;

import nl.remco.scope.service.Scope;


public class ScopeTest {

    private static final String NAME = "name";
    private static final String STATUS = "status";

    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_STATUS = 20;

    private static final String UPDATED_NAME = "updatedName";
    private static final String UPDATED_STATUS = "updatedTitle";

    @Test(expected = NullPointerException.class)
    public void build_TitleIsNull_ShouldThrowException() {
        Scope.getBuilder()
                .status(null)
                .name(NAME)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TitleIsEmpty_ShouldThrowException() {
        Scope.getBuilder()
                .status("")
                .name(NAME)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TitleIsTooLong_ShouldThrowException() {
        String tooLongTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS + 1);
        Scope.getBuilder()
                .status(tooLongTitle)
                .name(NAME)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_DescriptionIsTooLong_ShouldThrowException() {
        String tooLongDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME + 1);
        Scope.getBuilder()
                .status(STATUS)
                .name(tooLongDescription)
                .build();
    }

    @Test
    public void build_WithoutDescription_ShouldCreateNewTodoEntryWithCorrectTitle() {
        Scope build = Scope.getBuilder()
                .status(STATUS)
                .build();

        assertThatScope(build)
                .hasNoId()
                .hasStatus(STATUS)
                .hasNoDescription();
    }

    @Test
    public void build_WithTitleAndDescription_ShouldCreateNewTodoEntryWithCorrectTitleAndDescription() {
        Scope build = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        assertThatScope(build)
                .hasNoId()
                .hasStatus(STATUS)
                .hasName(NAME);
    }

    @Test
    public void build_WithMaxLengthTitleAndDescription_ShouldCreateNewTodoEntryWithCorrectTitleAndDescription() {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        Scope build = Scope.getBuilder()
                .status(maxLengthTitle)
                .name(maxLengthDescription)
                .build();

        assertThatScope(build)
                .hasNoId()
                .hasStatus(maxLengthTitle)
                .hasName(maxLengthDescription);
    }

    @Test(expected = NullPointerException.class)
    public void update_TitleIsNull_ShouldThrowException() {
        Scope updated = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        updated.update(null, UPDATED_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_TitleIsEmpty_ShouldThrowException() {
        Scope updated = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        updated.update("", UPDATED_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_TitleIsTooLong_ShouldThrowException() {
        Scope updated = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        String tooLongTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS + 1);
        updated.update(tooLongTitle, UPDATED_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_DescriptionIsTooLong_ShouldThrowException() {
        Scope updated = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        String tooLongDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME + 1);
        updated.update(UPDATED_STATUS, tooLongDescription);
    }

    @Test
    public void update_DescriptionIsNull_ShouldUpdateTitleAndDescription() {
        Scope updated = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        updated.update(UPDATED_STATUS, null);

        assertThatScope(updated)
                .hasStatus(UPDATED_STATUS)
                .hasNoDescription();
    }

    @Test
    public void update_MaxLengthTitleAndDescription_ShouldUpdateTitleAndDescription() {
        Scope updated = Scope.getBuilder()
                .status(STATUS)
                .name(NAME)
                .build();

        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_STATUS);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_NAME);

        updated.update(maxLengthTitle, maxLengthDescription);

        assertThatScope(updated)
                .hasStatus(maxLengthTitle)
                .hasName(maxLengthDescription);
    }
}
