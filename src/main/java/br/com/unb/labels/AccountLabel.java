package br.com.unb.labels;

public enum AccountLabel {

	NAME("name"), 
	DESCRIPTION("description"), 
	EXECUTION_PLACE("executionPlace"),
	START_DATE("startDateTime"), 
	END_DATE("endDateTime"), 
	OBSERVATION("observation"),
	VERSION("version"),
	VERSION_DATE("versionDate"),
	TYPE("type");
	
	private String label;
	
	AccountLabel(String label) 
	{
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
