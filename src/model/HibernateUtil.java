package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe que contém métodos de conexão e controle com o banco de dados.
 */
public class HibernateUtil {
	/**
	* Método responsável por abrir uma sessão de conexão com o banco de dados.
	* @return Retorna uma sessão de conexão com o banco de dados.
	*/
	protected Session openSession() {
		try {
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			return session;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO ABRIR SESSÃO DE CONEXÃO COM O BANCO DE DADOS <-[ERROR]");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* Método responsável fechar a conexão com o banco de dados.
	* @param Sessão de conexão com o banco de dados (já aberta).
	* @return Retorna um booleano, true caso bem sucedido.
	*/
	protected boolean close(Session session) {
		try {
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO FECHAR SESSÃO DE CONEXÃO COM O BANCO DE DADOS <-[ERROR]");
			e.printStackTrace();
			return false;
		}
	}
	
}
