package br.com.unb.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Project implements EntityProvenance{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nome;
	private String descricao;
	private String coordenador;
	private Date dataHoraInicio;
	private Date dataHoraFim;
	private String observacao;
	private Set<String> nomesInstituicoesParticipantes;
	private Set<String> nomesInstituicoesFinanciadoras;
	private User usuario;
	private List<Account> accounts;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	/**
	 * @return the coordenador
	 */
	public String getCoordenador() {
		return coordenador;
	}
	/**
	 * @param coordenador the coordenador to set
	 */
	public void setCoordenador(String coordenador) {
		this.coordenador = coordenador;
	}
	/**
	 * @return the dataHoraInicio
	 */
	public Date getDataHoraInicio() {
		return dataHoraInicio;
	}
	/**
	 * @param dataHoraInicio the dataHoraInicio to set
	 */
	public void setDataHoraInicio(Date dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}
	/**
	 * @return the dataHoraFim
	 */
	public Date getDataHoraFim() {
		return dataHoraFim;
	}
	/**
	 * @param dataHoraFim the dataHoraFim to set
	 */
	public void setDataHoraFim(Date dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}
	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}
	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	/**
	 * @return the nomesInstituicoesParticipantes
	 */
	public Set<String> getNomesInstituicoesParticipantes() {
		return nomesInstituicoesParticipantes;
	}
	/**
	 * @param nomesInstituicoesParticipantes the nomesInstituicoesParticipantes to set
	 */
	public void setNomesInstituicoesParticipantes(
			Set<String> nomesInstituicoesParticipantes) {
		this.nomesInstituicoesParticipantes = nomesInstituicoesParticipantes;
	}
	/**
	 * @return the nomesInstituicoesFinanciadoras
	 */
	public Set<String> getNomesInstituicoesFinanciadoras() {
		return nomesInstituicoesFinanciadoras;
	}
	/**
	 * @param nomesInstituicoesFinanciadoras the nomesInstituicoesFinanciadoras to set
	 */
	public void setNomesInstituicoesFinanciadoras(
			Set<String> nomesInstituicoesFinanciadoras) {
		this.nomesInstituicoesFinanciadoras = nomesInstituicoesFinanciadoras;
	}

	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Projeto [nome=" + nome + ", descricao=" + descricao
				+ ", coordenador=" + coordenador + ", dataHoraInicio="
				+ dataHoraInicio + ", dataHoraFim=" + dataHoraFim + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		if (!(obj instanceof Project)) {
			return false;
		}
		Project other = (Project) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.PROJECT;
	}
}
