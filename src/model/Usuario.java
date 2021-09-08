package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name = "usuarios")
public class Usuario {
	// -> ATRIBUTOS <- //
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 40)
	private String nome;
	
	@Column(nullable = false, length = 11, unique = true)
	private String cpf;
	
	@Column(nullable = false, length = 16)
	private String senha;
	
	@Column(nullable = false, length = 9)
	private String sexo;
	
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@OneToMany(mappedBy = "autor")
	private List<Projeto> projetosProprios;
	
	@ManyToOne
	@JoinColumn(name = "id_representante")
	private Usuario representante;
	
	@Transient
	private String msgError;
	
	// -> MÉTODOS CONSTRUTORES <- //
	
	// CONSTRUTOR EM BRANCO
	public Usuario() {
		
	}
	
	// CONSTRUTOR SÓ COM O ID
	public Usuario(long id) {
		this.id = id;
	}

	// CONSTRUTOR COMPLETO COM ID
	public Usuario(long id, String nome, String cpf, String senha, String sexo, Date dataNascimento,
			List<Projeto> projetosProprios, Usuario representante) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.senha = senha;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.projetosProprios = projetosProprios;
		this.representante = representante;
	}
	
	//CONSTRUTOR SEM ID
	public Usuario(String nome, String cpf, String senha, String sexo, Date dataNascimento,
			List<Projeto> projetosProprios, Usuario representante) {
		this.nome = nome;
		this.cpf = cpf;
		this.senha = senha;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.projetosProprios = projetosProprios;
		this.representante = representante;
	}
	
	// CONSTRUTOR PARA CADASTRO DE USUÁRIO
	public Usuario(String nome, String cpf, String senha, String sexo, Date dataNascimento) {
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.senha = senha;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
	}

	// -> MÉTODOS DE CONTROLE <- //
	
	//TODO: DOCUMENTAR
	public boolean atualizar(String senha) {
		if (!encriptarSenha(senha).equals(this.senha)) {
			msgError = "Senha atual incorreta";
			return false;
		}
		
		this.senha = desencriptarSenha(this.senha);
		
		if (!verificarDados())
			return false;
		
		return update();
	}
	
	//TODO: DOCUMENTAR
	public boolean atualizar(String senha, String senhaNova) {
		if (!encriptarSenha(senha).equals(this.senha)) {
			msgError = "Senha atual incorreta";
			return false;
		}
		
		this.senha = senhaNova;
		
		if (!verificarDados())
			return false;
		
		return update();
	}
	
	/**
	 * Método responsável por realizar o cadastro de Usuario.
	 * @return Retorna um booleano, true caso bem sucedido.
	 */
	public boolean cadastrar() {	
		if (!verificarDados())
			return false;
		
		return salvar();
	}
	
	//TODO: DOCUMENTAR
	private boolean verificarDados() {
		try {
			nome = verificarNome(nome);
			if(nome.length() == 1) { // VERIFICA SE O NOME ESTÁ CORRETO
				msgError = gerateErrorMsg(Integer.parseInt(nome), "nome");
				return false;
			}
			
			cpf = adjustCPF(cpf);
			if(cpf.length() == 1) { // VERIFICA SE O CPF ESTÁ CORRETO
				msgError = gerateErrorMsg(Integer.parseInt(cpf), "cpf");
				return false;
			}
			
			senha = verificarSenha(senha);
			if(senha.length() == 1) { // VERIFICA SE O CPF ESTÁ CORRETO
				msgError = gerateErrorMsg(Integer.parseInt(senha), "senha");
				return false;
			}
			senha = encriptarSenha(senha);
			
			if(dataNascimento == null) {
				msgError = gerateErrorMsg(2, "data de nascimento");
				return false;
			}
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			Date maiorIdade = simpleDate.parse("2005-01-01");
			if (dataNascimento.after(maiorIdade)) { // VERIFICA SE O USUÁRIO É MAIOR DE 16 ANOS
				msgError = "Somente pessoas maiores de 16 anos podem se cadastrar";
				return false;
			}
			
			return true;
		} catch (Exception e) {
			System.out.println("[ERROR]-> ERRO AO CADASTRAR USUARIO <-[ERROR]");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Método responsável por encriptar a senha para o banco de dados.
	 * @param Senha.
	 * @return Retorna uma String, contendo a senha encriptada.
	 */
	protected String encriptarSenha(String senha) {
		String alphabet = "Mç4lNkjfBVdhgv2CqXcZsÇ1wLKz9xJ0HGFaDbSuyA3POinIUY78mTtoR6pEr5WQeM";
		String encriptedSenha = "";
		
		for (Integer i = 0; i < senha.length(); i++) {
			Integer index = alphabet.indexOf(senha.charAt(i));
			index = (index + (senha.length() * i)) + (senha.length()+13);
			while(index > 64)
				index -= 64;
			
			encriptedSenha += alphabet.charAt(index);
		}
		
		encriptedSenha += "&";
		
		if(encriptedSenha.length() < 16) {
			for (Integer i = encriptedSenha.length(); i < 16; i++) {
				Integer index = alphabet.indexOf(encriptedSenha.charAt(i-6));
				index = (index + (encriptedSenha.length() * i)) + (encriptedSenha.length()+10);
				while(index > 64)
					index -= 64;
				
				if (alphabet.indexOf(encriptedSenha.charAt(i-6)) % 4 == 0)
					encriptedSenha += "&";
				else
					encriptedSenha += alphabet.charAt(index);
			}
		}
		
		return encriptedSenha;
	}
	
	/**
	 * Método responsável por desencriptar a senha proveniente do banco.
	 * @param Senha encriptada.
	 * @return Retorna uma String, contendo a senha desencriptada.
	 */
	private String desencriptarSenha(String encriptedSenha) {
		String alphabet = "Mç4lNkjfBVdhgv2CqXcZsÇ1wLKz9xJ0HGFaDbSuyA3POinIUY78mTtoR6pEr5WQe";
		String senha = "";
		encriptedSenha = encriptedSenha.split("&")[0];
		
		for (Integer i = 0; i < encriptedSenha.length(); i++) {
			Integer index = alphabet.indexOf(encriptedSenha.charAt(i));
			index = (index - (encriptedSenha.length() * i)) - (encriptedSenha.length()+13);
			
			while(index < 0)
				index += 64;
			
			senha += alphabet.charAt(index);
		}
		
		return senha;
	}

	/**
	 * Método responsável por realizar o login de usuário.
	 * @param CPF e SENHA do usuário.
	 * @return Retorna um booleano, true caso bem sucedido.
	 */
	public boolean login(String cpf, String senha) {
		try {
			if (cpf.isBlank() || cpf == null || senha.isBlank() || senha == null) {
				msgError = gerateErrorMsg(2, "senha ou cpf");
				return false;
			}
			
			cpf = adjustCPF(cpf); // CHAMA MÉTODO PARA DEIXAR O CPF NO FORMATO DESEJADO
			
			if(cpf.length() == 1) { // VERIFICA SE O CPF ESTÁ CORRETO
				msgError = gerateErrorMsg(Integer.parseInt(cpf), "cpf");
				return false;
			}
			
			this.cpf = cpf;
			this.senha = encriptarSenha(senha);
			
			PUsuario persist = new PUsuario();
			return persist.login(this);
			
		} catch (NumberFormatException e) {
			System.out.println("[ERROR]-> ERRO AO CONVERTER CODIGO DE ERRO DE CPF PARA INTEIRO <-[ERROR]");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("[ERROR]-> ERRO AO EFETURAR LOGIN <-[ERROR]");
			e.printStackTrace();
			return false;
		}
	}
	
	// TODO: DOCUMENTAR
	public boolean setRepresentante(long id) {
		representante = findById(id);
		if (representante == null) {
			msgError = "Representante com o id: " + id + " não encontrado";
			return false;
		}
		return update();
	}
	
	// TODO: DOCUMENTAR
	public boolean removeRepresentante() {
		if (representante != null) {
			representante = null;
			return update();
		} else {
			msgError = "Representa já está nulo";
			return false;
		}
	}

	/**
	 * Método responsável por verificar e tratar cpf de usuario.
	 * @param CPF não tratado.
	 * @return Retorna o cpf tratado ou um codigo de erro caso incorreto.
	 */
	private String adjustCPF(String cpf) {
		cpf = cpf.trim();
		
		String numbers = "0123456789";
		
		// PARTE DO CÓDIGO RESPONSÁVEL POR VERIFICAR O TAMANHO, O FORMATO E CONVERTER PARA O FORMATO NUMÉRICO (CASO NESCESSÁRIO)	
		if (cpf.length() == 14) { // CONVERTE O FORMATO DE STRING "000.000.000-00" EM NUMÉRICO "00000000000"
			if (String.valueOf(cpf.charAt(3)).equals(".") && String.valueOf(cpf.charAt(7)).equals(".")
					&& String.valueOf(cpf.charAt(11)).equals("-"))
				cpf = cpf.substring(0,3) + cpf.substring(4,7) + cpf.substring(8,11) + cpf.substring(12);
		}
		
		if (cpf.length() != 11) // FORMATOS INVÁLIDOS
			return "0"; // CÓDIGO DE ERRO PARA FORMATO/TAMANHO INVÁLIDO
		
		// PARTE DO CÓDIGO RESPONSÁVEL POR VERIFICAR SE HÁ CARACTERES NÃO NUMÉRICOS
		for (Integer i = 0; i < cpf.length(); i++) {
			boolean verificador = true;
			if (numbers.contains(String.valueOf(cpf.charAt(i))))
				verificador = false;
			
			if (verificador)
				return "1"; // CÓDIGO DE ERRO PARA CARACTERES INVÁLIDOS
		}
		return cpf;
	}
	
	/**
	 * Método responsável por verificar o nome do usuário.
	 * @param Nome a ser verificado.
	 * @return Retorna o nome validado ou um codigo de erro.
	 */
	private String verificarNome(String nome)  {
		nome = nome.trim();
		
		if (nome.length() > 40 || nome.length() < 5) // VERIFICA O TAMANHO DA STRING NOME
			return "0"; //CÓDIGO DE ERRO PARA TAMANHO INVÁLIDO
		
		if (nome.split(" ").length == 1)
			return "0"; //CÓDIGO DE ERRO PARA TAMANHO INVÁLIDO
		
		String regex = "!@#$%¨&*()_+1234567890-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'"; // CARACTERES NÃO ACEITOS
		
		for (Integer i = 0; i < nome.length(); i++) { // VERIFICA A PRESENÇA DE CARACTERES INVÁLIDOS NO NOME
			if (regex.contains(String.valueOf(nome.charAt(i))))
					return "1"; //CÓDIGO DE ERRO PARA CARACTERE INVÁLIDO.
		}
		
		return nome;
	}
	
	/**
	 * Método responsável por validar uma senha, tamanho min de 6 digitos e max de 15. Pelo menos uma letra e um número.
	 * @param Senha a ser verificada.
	 * @return Retorna uma String contendo a senha validada ou um código de erro.
	 */
	private String verificarSenha(String senha) {
		if (senha.length() > 15 || senha.length() < 6)
			return "0"; //CÓDIGO DE ERRO PARA TAMANHO INVÁLIDO
		
		String regex = "!@#$%¨&*()_+-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'"; // CARACTERES NÃO ACEITOS
		String numbers = "0123456789";
		
		boolean verificarNumero = false;
		boolean verificarLetra = false;
		
		for (Integer i = 0; i < senha.length(); i++) {
			if (regex.contains(String.valueOf(senha.charAt(i))))
				return "1"; //CÓDIGO DE ERRO PARA CARACTERE INVÁLIDO.
					
			if (!verificarNumero) { //VERIFICA SE HÁ PELO MENOS UM NÚMERO NA SENHA
				if (numbers.contains(String.valueOf(senha.charAt(i))))
					verificarNumero = true;
			}
		}
		
		for (Integer i = 0; i < senha.length(); i++) { //VERIFICA NA SENHA JÁ TRATADO SE HÁ PELO MENOS UMA LETRA
			if (!numbers.contains(String.valueOf(senha.charAt(i)))) {
				verificarLetra = true;
				break;
			}
		}
		
		if(!verificarNumero || !verificarLetra)
			return "0"; // CÓDIGO DE ERRO PARA TAMANHO OU FORMATO INVÁLIDO
		
		return senha;
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
	
	/**
	 * Método responsável por calcular a influência desse cidadão
	 * @return Retorna a influência de um cidadão (quantidade de votos que ele representa)
	 */
	public int getInfluencia() {
		int influencia = 1;
		
		PUsuario persist = new PUsuario();
		List<Usuario> representados = persist.getRepresentados(id);
		
		if (representados == null || representados.isEmpty())
			representados = new ArrayList<Usuario>();
		
		for (Usuario cidadao : representados)
			influencia += cidadao.getInfluencia(this);
		
		return influencia;
	}

	/**
	 * Método recursivo responsável por impedir a redundância de influência de um cidadão
	 * @param Usuário que deseja saber sua influência.
	 * @return Retorna a influência de um cidadão (quantidade de votos que ele representa)
	 */
	private int getInfluencia(Usuario usuarioPai) {
		int influencia = 1;
		
		PUsuario persist = new PUsuario();
		List<Usuario> representados = persist.getRepresentados(id);
		if (representados == null || representados.isEmpty())
			representados = new ArrayList<Usuario>();
		
		for (Usuario cidadao : representados) {
			if (!(cidadao.getId() == usuarioPai.getId()))
				influencia += cidadao.getInfluencia(usuarioPai);
		}
		
		return influencia;
	}
	
	//TODO: DOCUMENTAR
	public boolean rotinaBanco() {
		if (!createFirstUser())
			return false;
		
		return regulateProjects();
	}
	
	// -> MÉTODOS DE ACESSO DIRETO A CLASSE DE PERSISTÊNCIA <- //
	
	//TODO: DOCUMENTAR
	private boolean salvar() {
		PUsuario persist = new PUsuario();
		return persist.save(this);
	}
	
	//TODO: DOCUMENTAR
	public Usuario findById(long id) {
		PUsuario persist = new PUsuario();
		return persist.findById(id);
	}
	
	//TODO: DOCUMENTAR
	private boolean update() {
		PUsuario persist = new PUsuario();
		return persist.update(this);
	}
	
	//TODO: DOCUMENTAR
	public boolean find() {
		PUsuario persist = new PUsuario();
		return persist.find(this);
	}
	
	//TODO: DOCUMENTAR
	public ArrayList<Usuario> getUsersByName(String nome) {
		PUsuario persist = new PUsuario();
		return persist.getUsersByName(nome);
	}
	
	//TODO: DOCUMENTAR
	public ArrayList<Usuario> getRandonUsers(Long id) {
		PUsuario persist = new PUsuario();
		return persist.getRandonUsers(id);
	}
	
	//TODO: DOCUMENTAR
	private boolean regulateProjects() {
		PProjeto persist = new PProjeto();
		return persist.regulateProjects();
	}
	
	/**
	 * Método responsável por criar um usuário padrão para o sistema (caso não exista)
	 * @return Retorna um booleano, true caso bem sucedido
	 */
	private boolean createFirstUser() {
		PUsuario persist = new PUsuario();
		
		if (persist.find(1l))
			return true;
		
		return persist.createFirstUser();
	}
	
	// -> MÉTODOS GET/SET <- //
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public List<Projeto> getProjetosProprios() {
		return projetosProprios;
	}
	public void setProjetosProprios(List<Projeto> projetosProprios) {
		this.projetosProprios = projetosProprios;
	}
	public Usuario getRepresentante() {
		return representante;
	}
	public void setRepresentante(Usuario representante) {
		this.representante = representante;
	}
	public String getMsgError() {
		return msgError;
	}
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	// <-> //
}
