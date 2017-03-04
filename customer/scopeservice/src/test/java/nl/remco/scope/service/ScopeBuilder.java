package nl.remco.scope.service;

import org.springframework.test.util.ReflectionTestUtils;

import nl.remco.scope.service.Scope;


class ScopeBuilder {

    private String name;
    private String id;
    private String title = "NOT_IMPORTANT";

    ScopeBuilder() {

    }

    ScopeBuilder name(String name) {
        this.name = name;
        return this;
    }

    ScopeBuilder id(String id) {
        this.id = id;
        return this;
    }

    ScopeBuilder status(String status) {
        this.title = status;
        return this;
    }

    Scope build() {
        Scope todo = Scope.getBuilder()
                .status(title)
                .name(name)
                .build();

        ReflectionTestUtils.setField(todo, "id", id);

        return todo;
    }
}
