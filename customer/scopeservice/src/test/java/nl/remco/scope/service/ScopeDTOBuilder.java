package nl.remco.scope.service;

import nl.remco.scope.service.ScopeDTO;

class ScopeDTOBuilder {

    private String description;
    private String id;
    private String title;

    ScopeDTOBuilder() {

    }

    ScopeDTOBuilder name(String description) {
        this.description = description;
        return this;
    }

    ScopeDTOBuilder id(String id) {
        this.id = id;
        return this;
    }

    ScopeDTOBuilder status(String title) {
        this.title = title;
        return this;
    }

    ScopeDTO build() {
        ScopeDTO dto = new ScopeDTO();

        dto.setName(description);
        dto.setId(id);
        dto.setStatus(title);

        return dto;
    }
}
