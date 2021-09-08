package model;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Classe responsável pela persistência de dados da classe Usuario.
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class PUsuario {
	//TODO: DOCUMENTAR
	protected Usuario findById(long id) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Usuario auxUser = (Usuario) session.find(Usuario.class, id);
			persist.close(session);
			return auxUser;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO LOCALIZAR USUÁRIO NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	//TODO: DOCUMENTAR
	protected boolean find(Usuario user) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Usuario auxUser = (Usuario) session.find(Usuario.class, user.getId());
			
			if (auxUser == null) {
				user.setMsgError("Usuario não encontrado");
				persist.close(session);
				return false;
			}
			
			user.setCpf(auxUser.getCpf());
			user.setDataNascimento(auxUser.getDataNascimento());
			user.setId(auxUser.getId());
			user.setNome(auxUser.getNome());
			user.setProjetosProprios(auxUser.getProjetosProprios());
			user.setRepresentante(auxUser.getRepresentante());
			user.setSenha(auxUser.getSenha());
			user.setSexo(auxUser.getSexo());
			
			persist.close(session);
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO LOCALIZAR USUÁRIO NO BANCO <-[ERROR]");
			e.printStackTrace();
			user.setMsgError("Erro ao localizar usuário no banco");
			persist.close(session);
			return false;
		}
	}
	
	//TODO: DOCUMENTAR
	protected boolean update(Usuario user) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			session.update(user);
			persist.close(session);
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO ATUALIZAR O USUÁRIO "+user.getNome().toUpperCase()+" NO BANCO <-[ERROR]");
			e.printStackTrace();
			user.setMsgError("Erro ao atualizar o usuário " + user.getNome().toUpperCase());
			persist.close(session);
			return false;
		}
	}
	
	//TODO: DOCUMENTAR
	protected ArrayList<Usuario> getUsersByName(String nome) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Usuario> aux = (ArrayList<Usuario>) session.createCriteria(Usuario.class).add(Restrictions.like("nome", nome + "%")).setMaxResults(25).list();
			persist.close(session);
			return aux;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR USUARIOS PELO NOME NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	//TODO: DOCUMENTAR
	protected ArrayList<Usuario> getRandonUsers(Long id) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Usuario> aux = (ArrayList<Usuario>) session.createCriteria(Usuario.class).add(Restrictions.ne("sexo", "Robot"))
					.add(Restrictions.ne("id", id)).add(Restrictions.sqlRestriction("1=1 order by rand()")).setMaxResults(12).list();
			persist.close(session);
			return aux;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR USUARIOS RANDOMICOS NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	/**
	 * Método responsável por salvar um usuário no banco.
	 * @param Usuario com campos obrigatórios preenchidos.
	 * @return Retorna um booleano, true caso bem sucedido.
	 */
	protected boolean save(Usuario user) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			session.save(user);
			persist.close(session);
			return true;	
		} catch (ConstraintViolationException e) {
			user.setMsgError("CPF já cadastrado");
			persist.close(session);
			return false;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO SALVAR O USUÁRIO "+user.getNome().toUpperCase()+" NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	/**
	 * Método responsável por realizar o login de Usuario e preenche o objeto passado pela memória.
	 * @param Usuario com cpf e Senha Preenchidos.
	 * @return Retorna um booleano, true caso bem sucedido.
	 */
	protected boolean login(Usuario loggingUser) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Usuario auxUser = (Usuario) session.createCriteria(Usuario.class).add(Restrictions.eq("cpf", loggingUser.getCpf()))
					.add(Restrictions.eq("senha", loggingUser.getSenha())).uniqueResult();
			
			if (auxUser == null) {
				loggingUser.setMsgError("CPF ou senha inválidos");
				persist.close(session);
				return false;
			}
			
			loggingUser.setCpf(auxUser.getCpf());
			loggingUser.setDataNascimento(auxUser.getDataNascimento());
			loggingUser.setId(auxUser.getId());
			loggingUser.setNome(auxUser.getNome());
			loggingUser.setProjetosProprios(auxUser.getProjetosProprios());
			loggingUser.setRepresentante(auxUser.getRepresentante());
			loggingUser.setSenha(auxUser.getSenha());
			loggingUser.setSexo(auxUser.getSexo());
			
			persist.close(session);
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO LOGAR USUARIO (CPF): "+loggingUser.getCpf()+" <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	/**
	 * Método responsável por buscar no banco todos os usuários representados por um usuário.
	 * @param Id do usuário que deseja saber quem são seus representados.
	 * @return Retorna uma lista de "Usuario".
	 */
	protected ArrayList<Usuario> getRepresentados(long id) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Usuario> resultList = (ArrayList<Usuario>) session.createCriteria(Usuario.class).add(Restrictions.eq("representante.id", id)).list();
			persist.close(session);
			return resultList;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR A LISTA DE 'REPRESENTADOS' NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	/**
	 * Método responsável por validar no banco se um usuário já existe.
	 * @param Id do usuário.
	 * @return Retorna um booleano, true caso o usuário já existe e false caso não.
	 */
	protected boolean find(long id) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Usuario aux = session.find(Usuario.class, id);
			persist.close(session);
			
			if (aux != null)
				return true;
			else
				return false;
			
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO LOCALIZAR UM USUÁRIO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	/**
	 * Método responsável por criar um usuário padrão no sistema.
	 * @return Retorna um booleano, true caso bem sucedido.
	 */
	protected boolean createFirstUser() {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Usuario u = new Usuario();
			session.save(new Usuario("Admin", "00000000000", u.encriptarSenha("admin123"), "Robot", new Date(), null, null));
			persist.close(session);
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO CRIAR O USUÁRIO PADRÃO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
}
