package br.com.unb.controller;

import br.com.caelum.vraptor.Resource;

@Resource
public class Mundo {

	public String boasVindas() {
		return "olá mundo Rodrigo...";
	}
}
