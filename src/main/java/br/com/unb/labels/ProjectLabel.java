package br.com.unb.labels;

public enum ProjectLabel {

	NAME("name"), 
	DESCRIPTION("description"), 
	COORDENATOR("coordenator"),
	START_DATE("startDateTime"), 
	END_DATE("endDateTime"), 
	OBSERVATION("observation"),
	TYPE("type"), 
	PARTICIPATING_INSTITUTIONS("participatingInstitutions"),
	FUNDING_INSTITUTIONS("fundingInstitutions");
	
	private String label;
	
	ProjectLabel(String label) 
	{
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
