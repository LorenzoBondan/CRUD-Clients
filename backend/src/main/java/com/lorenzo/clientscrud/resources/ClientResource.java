package com.lorenzo.clientscrud.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lorenzo.clientscrud.dto.ClientDTO;
import com.lorenzo.clientscrud.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {

	@Autowired
	private ClientService service;
	
	@GetMapping
	public ResponseEntity<Page<ClientDTO>> findAll(
			
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
			
			)
	{
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<ClientDTO> list = service.findAllPaged(pageRequest);
		return ResponseEntity.ok().body(list);
	}
	
	//*** TRAZENDO CLIENTE POR ID
	@GetMapping(value = "/{id}") //ARGUMENTO ACRESCENTADO NA FRENTE DA ROTA BÁSICA
	public ResponseEntity<ClientDTO> findById(@PathVariable Long id) //PARÂMETRO TEM QUE FICAR IGUAL AO CAMINHO ACIMA | PathVariable DADO OBRIGATÓRIO
	{
		ClientDTO dto = service.findById(id);	
		return ResponseEntity.ok().body(dto);
	}
	
	// INSERINDO NOVA CATEGORIA
		@PostMapping
		public ResponseEntity<ClientDTO> insert (@RequestBody ClientDTO dto)
		{
			dto = service.insert(dto);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(dto.getId()).toUri();
			return ResponseEntity.created(uri).body(dto);	
		}
		
		// ATUALIZANDO CATEGORIA -> É UMA MISTURA DO BUSCA POR ID COM POST
		@PutMapping(value = "/{id}")
		public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody ClientDTO dto)
		{
			dto = service.update(id, dto);
			return ResponseEntity.ok().body(dto);
		}
		
		// DELETANDO CATEGORIA
		@DeleteMapping(value = "/{id}")
		public ResponseEntity<ClientDTO> delete(@PathVariable Long id)
		{
			service.delete(id);
			return ResponseEntity.noContent().build(); // RESPOSTA 204 = DEU CERTO, COM O CORPO VAZIO
		}
		
}