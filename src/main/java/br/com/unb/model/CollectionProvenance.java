package br.com.unb.model;

import java.util.Set;

import org.neo4j.graphdb.Node;

public class CollectionProvenance implements EntityProvenance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private long size;
	private String description;
	private String location;
	private String notes;
	private Activity activity;
	private Set<EntityProvenance> entitiesProvenance;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Set<EntityProvenance> getEntities() {
		return entitiesProvenance;
	}
	public void setEntities(Set<EntityProvenance> entitiesProvenance) {
		this.entitiesProvenance = entitiesProvenance;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.COLLECTION;
	}
	
//	@Override
//	public EntityProvenance transform(Node node) {
//		CollectionProvenance collection = new CollectionProvenance();
//		collection.setId(node.getId());
//		collection.setName((String) node.getProperty("nome"));
//		collection.setSize((Long) node.getProperty("tamanho"));
//		collection.setLocation((String) node.getProperty("localizacao"));
//		if (node.hasProperty("descricao")) {
//			collection.setDescription((String) node.getProperty("descricao"));
//		}
//		if (node.hasProperty("notacao")) {
//			collection.setNotes((String) node.getProperty("notacao"));
//		}
//		return collection;
//	}
	
	public static String buildJson(Node node) {
		return String.format("{id:%d, name: '%s', type:'%s' }", 
			node.getId(), node.getProperty("name"), EntityType.COLLECTION.getName());
		
	}

}
