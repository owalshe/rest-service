package rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rest.service.exceptions.ResourceAlreadyExistsException;
import rest.service.exceptions.ResourceNotFoundException;
import rest.service.model.DataResource;
import rest.service.response.UpdateResponse;

@RestController
public class ResourceController {

	@Autowired
	private ResourceService service;

	@GetMapping(value = "/data", produces = "application/json")
	public ResponseEntity<List<DataResource>> getAll() {
		return new ResponseEntity<>(this.service.getAllResources(), HttpStatus.OK);
	}

	@GetMapping(value = "/data/{id}", produces = "application/json")
	public ResponseEntity<DataResource> get(@PathVariable Long id) throws ResourceNotFoundException {
		return new ResponseEntity<>(this.service.getResource(id), HttpStatus.OK);
	}

	@PostMapping(value = "/data", produces = "application/json")
	public ResponseEntity<DataResource> create(@RequestBody DataResource dataResource) throws ResourceAlreadyExistsException {
		return new ResponseEntity<>(this.service.newResource(dataResource), HttpStatus.CREATED);
	}

	@PutMapping(value = "/data/{id}", produces = "application/json")
	public ResponseEntity<DataResource> update(@PathVariable Long id, @RequestBody DataResource dataResource) {
		UpdateResponse response = this.service.updateResource(id, dataResource);
		if(response.isUpdated()) {
			return new ResponseEntity<>(response.getResource(), HttpStatus.OK);
		}
		return new ResponseEntity<>(response.getResource(), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/data/{id}", produces = "application/json")
	public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
		Boolean isDeleted = this.service.deleteResource(id);
		if(isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
