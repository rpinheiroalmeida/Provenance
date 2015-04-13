package br.com.unb.labels;

public enum ActivityLabel {

	NAME("name"), 
	PROGRAM_NAME("programName"), 
	PROGRAM_VERSION("programVersion"),
	COMMAND_LINE("commandLine"), 
	FUNCTION("function"), 
	START_DATE("startDate"),
	END_DATE("endDate"),
	FILE_NAME("fileName"),
	START_HOUR("startHour"),
	END_HOUR("startHour"),
	TYPE("type");
	
	private String label;
	
	ActivityLabel(String label) 
	{
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
