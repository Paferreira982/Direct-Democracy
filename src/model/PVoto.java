package model;

import org.hibernate.Session;

public class PVoto {
	protected boolean deletar(Voto voto) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			session.remove(voto);
			persist.close(session);
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO DELETAR VOTO SEM RELACIONAMENTO DO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
}
