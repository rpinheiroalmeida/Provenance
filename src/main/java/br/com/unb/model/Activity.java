package br.com.unb.model;

import java.util.Date;

public class Activity implements EntityProvenance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nome;
	private String nomePrograma;
	private String versaoPrograma;
	private String linhaComando;
	private String funcao;
	private Date dataInicio;
	private Date dataFim;
	private String nomeArquivo;
	private String horaInicio;
	private String horaFim;
	private Account account;

	private Long idAccountExibir;
	private Long idProjetoExibir;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long idAtividade) {
		this.id = idAtividade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nomeAtividade) {
		this.nome = nomeAtividade;
	}
	
	/**
	 * @return the nomePrograma
	 */
	public String getNomePrograma() {
		return nomePrograma;
	}
	/**
	 * @param nomePrograma the nomePrograma to set
	 */
	public void setNomePrograma(String nomePrograma) {
		this.nomePrograma = nomePrograma;
	}
	/**
	 * @return the versaoPrograma
	 */
	public String getVersaoPrograma() {
		return versaoPrograma;
	}
	/**
	 * @param versaoPrograma the versaoPrograma to set
	 */
	public void setVersaoPrograma(String versaoPrograma) {
		this.versaoPrograma = versaoPrograma;
	}
	/**
	 * @return the linhaComando
	 */
	public String getLinhaComando() {
		return linhaComando;
	}
	/**
	 * @param linhaComando the linhaComando to set
	 */
	public void setLinhaComando(String linhaComando) {
		this.linhaComando = linhaComando;
	}
	/**
	 * @return the funcao
	 */
	public String getFuncao() {
		return funcao;
	}
	/**
	 * @param funcao the funcao to set
	 */
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	/**
	 * @return the dataHoraInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}
	/**
	 * @param dataHoraInicio the dataHoraInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	/**
	 * @return the dataHoraFim
	 */
	public Date getDataFim() {
		return dataFim;
	}
	/**
	 * @param dataHoraFim the dataHoraFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
		if (account != null){
			this.idAccountExibir = account.getId();
			this.idProjetoExibir = account.getProject().getId();
		}
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraFim() {
		return horaFim;
	}
	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}
	public EntityType getType() {
		return EntityType.ACTIVITY;
	}
	
	public Long getIdAccountExibir() {
		return idAccountExibir;
	}
	public void setIdAccountExibir(Long idAccountExibir) {
		this.idAccountExibir = idAccountExibir;
	}
	public Long getIdProjetoExibir() {
		return idProjetoExibir;
	}
	public void setIdProjetoExibir(Long idProjetoExibir) {
		this.idProjetoExibir = idProjetoExibir;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Activity)) {
			return false;
		}
		Activity other = (Activity) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
