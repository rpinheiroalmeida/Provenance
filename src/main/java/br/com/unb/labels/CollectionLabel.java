package br.com.unb.labels;

public enum CollectionLabel {

	NAME("name"),
	LOCATION("location"),
	SIZE("size"),
	TYPE("type");
	
	private String label;
	
	CollectionLabel(String label) 
	{
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
