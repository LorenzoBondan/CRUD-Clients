package com.lorenzo.clientscrud.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lorenzo.clientscrud.dto.ClientDTO;
import com.lorenzo.clientscrud.entities.Client;
import com.lorenzo.clientscrud.repositories.ClientRepository;
import com.lorenzo.clientscrud.services.exceptions.DataBaseException;
import com.lorenzo.clientscrud.services.exceptions.ResourceNotFoundException;


@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	//*** TRAZER TUDO PAGINADO
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest)
	{
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}
	
	//*** METODO DE BUSCAR CLIENTE POR ID
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found.") );
		return new ClientDTO(entity);
	}
	
	//*** METODO DE INSERIR CLIENTE
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoToEntity(dto,entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id); // GETREFERENCEBYID
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);

		}
	}
	
	// MÉTODO AUXILIAR PRA COPIAR TODAS AS PROPRIEDADES DA DTO PRA ENTITY
		// USADO NO INSERT E NO UPDATE PRA TODOS OS ATRIBUTOS
	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		// CASO VC TENTE DELETAR UMA CATEGORIA COM PRODUTOS VINCULADOS
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}
}
