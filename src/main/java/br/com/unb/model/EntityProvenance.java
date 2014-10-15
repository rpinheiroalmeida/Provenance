package br.com.unb.model;

import java.io.Serializable;


public interface EntityProvenance extends Serializable {

	Long getId();
	
	EntityType getType();
	
}
