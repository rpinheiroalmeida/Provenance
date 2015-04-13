package br.com.unb.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Activity;
import br.com.unb.model.CollectionProvenance;
import br.com.unb.repository.CollectionProvenanceRepository;

@Component
public class CollectionProvenanceService {

	@Inject private CollectionProvenanceRepository collectionRepository;

	public Long save(CollectionProvenance collectionProvenance) {
		return collectionRepository.save(collectionProvenance);
	}

	public List<CollectionProvenance> findByName(String[] fileNames) {
		List<CollectionProvenance> collections = new ArrayList<>();
		for (String filename : fileNames) {
			collections.add(findByName(filename));
		}
		return collections;
	}
	
	public CollectionProvenance findByName(String filename) {
		return collectionRepository.findByName(filename);
	}

	public void save(List<CollectionProvenance> collections) {
		for (CollectionProvenance collectionProvenance : collections) {
			save(collectionProvenance);
		}
	}

	public void update(String user, Activity activity, CollectionProvenance collection) {
		collectionRepository.update(user, activity, collection);
	}
}
