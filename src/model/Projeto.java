package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name = "projetos")
public class Projeto {
	// -> ATRIBUTOS <- //
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 40)
	private String titulo;
	
	@Column(nullable = false, length = 500)
	private String descricao;
	
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	private Date prazo;
	
	@Column(nullable = false)
	private String status;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_autor", foreignKey = @ForeignKey(name ="fk_autor"))
	private Usuario autor;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_projeto", foreignKey = @ForeignKey(name ="fk_votos"))
	private List<Voto> votos;
	
	@Transient
	private String msgError;
	
	// -> MÉTODOS CONSTRUTORES <- //
	
	// CONSTRUTOR VAZIO
	public Projeto() {
		
	}
	
	// CONSTRUTOR COMPLETO COM ID
	public Projeto(long id, String titulo, String descricao, Date dataInicio, Date prazo, String status, Usuario autor,
			List<Voto> votos) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.prazo = prazo;
		this.status = status;
		this.autor = autor;
		this.votos = votos;
	}
	
	//CONSTRUTOR COMPLETO PARA CADASTRO
	public Projeto(String titulo, String descricao, Date dataInicio, Date prazo, Usuario autor) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.prazo = prazo;
		this.autor = autor;
		this.status = "votando";
	}
	
	// CONSTRUTOR COMPLETO SEM ID
	public Projeto(String titulo, String descricao, Date dataInicio, Date prazo, String status, Usuario autor,
			List<Voto> votos) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.prazo = prazo;
		this.status = status;
		this.autor = autor;
		this.votos = votos;
	}

	// -> MÉTODOS DE CONTROLE <- //
	//TODO: DOCUMENTAR
	public boolean cadastrar() {
		try {
			titulo = verificarTitulo(titulo);
			if(titulo.length() == 1) { // VERIFICA SE O NOME ESTÁ CORRETO
				msgError = gerateErrorMsg(Integer.parseInt(titulo), "titulo");
				return false;
			}
			
			if(descricao.length() < 10 || descricao.length() > 500) { // VERIFICA SE O CPF ESTÁ CORRETO
				msgError = gerateErrorMsg(0, "descricao");
				return false;
			}
			
			if(prazo == null) {
				msgError = gerateErrorMsg(2, "data de nascimento");
				return false;
			}
			
			PProjeto persist = new PProjeto();
			return persist.save(this);
			
		} catch (Exception e) {
			System.out.println("[ERROR]-> ERRO AO CADASTRAR PROJETO <-[ERROR]");
			e.printStackTrace();
			return false;
		}
	}

	//TODO: DOCUMENTAR
	public Double getProjectAproval() {
		if (votos == null)
			votos = new ArrayList<Voto>();
		
		Integer votosPositivos = getInfluenciaPositivaFromVotos(votos);
		Integer totalVotos = getTotalVotos();
		
		if (totalVotos == 0)
			return 0.0;
		
		return (double) ((votosPositivos * 100) / totalVotos);
	}
	
	//TODO: DOCUMENTAR
	public Integer getInfluenciaPositivaFromVotos(List<Voto> votos) {
		Integer influencia = 0;
		
		if (votos == null)
			votos = new ArrayList<Voto>();
		
		for (Voto aux : votos) {
			if (aux.getVoto())
				influencia += aux.getEleitor().getInfluencia();
		}
		
		return influencia;
	}
	
	//TODO: DOCUMENTAR
	public Integer getInfluenciaFromVotos(List<Voto> votos) {
		Integer influencia = 0;
		
		if (votos == null)
			votos = new ArrayList<Voto>();
		
		for (Voto aux : votos)
			influencia += aux.getEleitor().getInfluencia();
		
		return influencia;
	}
	
	//TODO: DOCUMENTAR
	public Integer getTotalVotos() {
		if (votos == null)
			votos = new ArrayList<Voto>();
		
		Integer totalVotos = getInfluenciaFromVotos(votos);
		return totalVotos;
	}
	
	//TODO: DOCUMENTAR
	private String verificarTitulo(String titulo)  {
		titulo = titulo.trim();
		
		if (titulo.length() > 40 || titulo.length() < 5) // VERIFICA O TAMANHO DA STRING NOME
			return "0"; //CÓDIGO DE ERRO PARA TAMANHO INVÁLIDO
		
		if (titulo.split(" ").length == 1)
			return "0"; //CÓDIGO DE ERRO PARA TAMANHO INVÁLIDO
		
		String regex = "!@#$%¨&*()_+1234567890-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'"; // CARACTERES NÃO ACEITOS
		
		for (Integer i = 0; i < titulo.length(); i++) { // VERIFICA A PRESENÇA DE CARACTERES INVÁLIDOS NO NOME
			if (regex.contains(String.valueOf(titulo.charAt(i))))
					return "1"; //CÓDIGO DE ERRO PARA CARACTERE INVÁLIDO.
		}
		
		return titulo;
	}
	
	/**
	 * Método responsável por decodificar um codigo de erro para uma mensagem.
	 * @param Codigo de erro.
	 * @return Retorna uma String contendo a mensagem de erro.
	 */
	private String gerateErrorMsg(Integer codigo, String type) {
		if (codigo == 0) {return "Tamanho ou formato inválido para " + type;}
		if (codigo == 1) {return "Caracteres inválidos para " + type;}
		if (codigo == 2) {return "Valor não pode estar em branco para " + type;}
		return "Codigo de erro '"+codigo+"' não cadastrado";
	}
	
	//TODO: DOCUMENTAR
	public boolean votar(Usuario user, boolean voto) {
		if (votos == null || votos.isEmpty())
			votos = new ArrayList<Voto>();
		
		if (verificarVoto(user))
			retirarVoto(user);
		
		Voto aux = new Voto(user, voto);
		votos.add(aux);
		return atualizar();
	}
	
	//TODO: DOCUMENTAR
	public boolean retirarVoto(Usuario user) {
		Voto aux = null;
		if (votos == null || votos.isEmpty()) {
			msgError = "Erro inesperado ao retirar voto positivo, lista em branco ou nula";
			return false;
			
		} else if (verificarVoto(user)) {
			for (int i = 0; i < votos.size(); i++) {
				if (votos.get(i).getEleitor().getId() == user.getId()) {
					aux = votos.get(i);
					votos.remove(i);
					break;
				}
			}
			
			if (atualizar() && aux.deletar())
				return true;
			
			return false;
			
		} else {
			msgError = "Erro inesperado ao retirar voto positivo, status incorreto";
			return false;
		}
	}
	
	//TODO: DOCUMENTAR
	public boolean verificarVoto(Usuario user) {
		if (votos == null || votos.isEmpty())
			return false;
		
		for (Voto aux : votos) {
			if (aux.getEleitor().getId() == user.getId())
				return true;
		}
		
		return false;
	}
	
	//TODO: DOCUMENTAR
	public boolean verificarVoto(Usuario user, boolean voto) {
		if (autor.getId() == user.getId())
			return true;
		
		if (votos == null || votos.isEmpty())
			return false;
		
		for (Voto aux : votos) {
			if (aux.getEleitor().getId() == user.getId() && voto == aux.getVoto())
				return true;
		}
		
		return false;
	}
	
	//TODO: DOCUMENTAR
	protected boolean definirVotacao() {
		Double approval = getProjectAproval();
		if (approval > 60.0)
			status = "Aprovado";
		else
			status = "Reprovado";
		
		return atualizar();
	}
	
	// -> MÉTODOS DE ACESSO DIRETO A CLASSE DE PERSISTÊNCIA <- //
	
	//TODO: DOCUMENTAR
	public ArrayList<Projeto> findFinishedProjects() {
		PProjeto persist = new PProjeto();
		return persist.findFinishedProjects();
	}
	
	//TODO: DOCUMENTAR
	public ArrayList<Projeto> getOwnProjects(long id) {
		PProjeto persist = new PProjeto();
		return persist.getOwnProjects(id);
	}
	
	//TODO: DOCUMENTAR
	public ArrayList<Projeto> getAllVotedProjects(long id) {
		PProjeto persist = new PProjeto();
		return persist.getAllVotedProjects(id);
	}
	
	//TODO: DOCUMENTAR
	public boolean atualizar() {
		PProjeto persist = new PProjeto();
		return persist.atualizarProjeto(this);
	}
	
	//TODO: DOCUMENTAR
	public boolean findCurrentProjectsByAutorId(long idAutor) {
		PProjeto persist = new PProjeto();
		return persist.findCurrentProjectsByAutorId(this, idAutor);
	}
	
	//TODO: DOCUMENTAR
	public ArrayList<Projeto> findCurrentProjects(long id) {
		PProjeto persist = new PProjeto();
		return persist.findCurrentProjects(id);
	}
	
	//TODO: DOCUMENTAR
	public String getPrazoStringed() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataFormatada = dateFormat.format(prazo);
        return dataFormatada;
	}
	
	public String getDataInicioStringed() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataFormatada = dateFormat.format(dataInicio);
        return dataFormatada;
	}
	
	//TODO: DOCUMENTAR
	public boolean findById() {
		PProjeto persist = new PProjeto();
		return persist.findProjectById(this);
	}
	
	// -> MÉTODOS GET/SET <- //

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Usuario getAutor() {
		return autor;
	}
	public void setAutor(Usuario autor) {
		this.autor = autor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getPrazo() {
		return prazo;
	}
	public void setPrazo(Date prazo) {
		this.prazo = prazo;
	}
	public String getMsgError() {
		return msgError;
	}
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	public List<Voto> getVotos() {
		return votos;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}
	
	
	// <-> //
}
