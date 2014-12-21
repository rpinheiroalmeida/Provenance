package br.com.unb.model;

public enum EntityType {

	PROJECT("Project"), ACCOUNT("Account"), AGENT("Agent"), ACTIVITY("Activity"), COLLECTION("Collection"), GROUP("Group");
	
	private String name;
	
	EntityType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
