package rest.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import rest.service.exceptions.ResourceAlreadyExistsException;
import rest.service.exceptions.ResourceNotFoundException;
import rest.service.model.DataResource;
import rest.service.repository.DataResourceRepository;
import rest.service.response.UpdateResponse;

@Service
public class ResourceService {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);
	
	@Autowired
	private DataResourceRepository repository;
	
	public DataResource getResource(Long id) throws ResourceNotFoundException {
		Optional<DataResource> optional = this.repository.findById(id);
		if(optional.isPresent()) {
			DataResource dataResource = optional.get();
			LOG.info(String.format("found resource %s", dataResource.toString()));
			return dataResource;
		}
		throw new ResourceNotFoundException(String.format("Unable to find resource for id %d", id));
	}
	
	public List<DataResource> getAllResources() {
		return this.repository.findAll();
	}
	
	public DataResource newResource(DataResource dataResource) throws ResourceAlreadyExistsException {
		Example<DataResource> example = Example.of(dataResource);
	    Optional<DataResource> optional = repository.findOne(example);
	    if(optional.isPresent()) {
			throw new ResourceAlreadyExistsException(String.format("Resource already exists for %s", dataResource));
		}
	    DataResource newResource = this.repository.save(dataResource);
	    LOG.info(String.format("created resource %s", newResource.toString()));
		return newResource;
	}
	
	public UpdateResponse updateResource(Long id, DataResource dataResource) {
		Optional<DataResource> optional = this.repository.findById(id);
		if(optional.isPresent()) {
			DataResource existingResource = optional.get();
			existingResource.copyFrom(dataResource);
			DataResource updatedResource = this.repository.save(existingResource);
			LOG.info(String.format("updated resource %s", updatedResource.toString()));
			return new UpdateResponse(updatedResource, true);
		}
		DataResource newResource = this.repository.save(dataResource);
		LOG.info(String.format("created resource %s", newResource.toString()));
		return new UpdateResponse(newResource, false);
	}

	public boolean deleteResource(Long id) {
		boolean exists = this.repository.existsById(id);
		if(exists) {
			this.repository.deleteById(id);
			LOG.info(String.format("deleted resource with id %d", id));
			return Boolean.TRUE;
		}
		LOG.info(String.format("unable to find resource by id %d", id));
		return Boolean.FALSE;
	}

}
