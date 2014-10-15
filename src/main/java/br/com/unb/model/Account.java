package br.com.unb.model;

import java.util.Date;
import java.util.List;

import org.neo4j.graphdb.Node;

public class Account implements EntityProvenance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;
	private String descricao;
	private String localExecucao;
	private Date dataHoraInicio;
	private Date dataHoraFim;
	private String anotacoes;
	private String versao;
	private Date dataVersao;
	private Project project;
	private List<Activity> activities;
	
	public Long getId() {
		return id;
	}
	public void setId(Long idExperimento) {
		this.id = idExperimento;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getLocalExecucao() {
		return localExecucao;
	}
	public void setLocalExecucao(String localExecucao) {
		this.localExecucao = localExecucao;
	}
	public Date getDataHoraInicio() {
		return dataHoraInicio;
	}
	public void setDataHoraInicio(Date dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}
	public Date getDataHoraFim() {
		return dataHoraFim;
	}
	public void setDataHoraFim(Date dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}
	public String getAnotacoes() {
		return anotacoes;
	}
	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}
	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
	public Date getDataVersao() {
		return dataVersao;
	}
	public void setDataVersao(Date dataVersao) {
		this.dataVersao = dataVersao;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	@Override
	public EntityType getType() {
		return EntityType.ACCOUNT;
	}
	
	public static String buildJson(Node node) {
		return String.format("{id:%d, name: '%s', type:'%s' }", 
			node.getId(), node.getProperty("nome"), EntityType.ACCOUNT.getName());
		
	}
}
