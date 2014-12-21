package br.com.unb.model;
import org.neo4j.graphdb.RelationshipType;


public enum RelationshipProvenanceType implements RelationshipType {

	HAS("HAS"), WAS_DERIVED_FROM("WasDerivedFrom"), USED("Used"), WAS_GENERATED_BY("WasGeneratedBy"), 
	WAS_ATTRIBUTED_TO("WasAttributedTo"), WAS_ASSOCIATED_WITH("WasAssociatedWith"),
	HAS_GROUP("HasGroup");
	
	private String name;
	
	RelationshipProvenanceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
