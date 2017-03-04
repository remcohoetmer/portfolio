package nl.remco.scope.service;

import java.util.List;


interface ScopeService {
	ScopeDTO create(ScopeDTO scope);
    ScopeDTO delete(String id);
    List<ScopeDTO> findAll();
    ScopeDTO findById(String id);
    ScopeDTO update(ScopeDTO scope);
}
