package model;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Classe responsável pela persistência de dados da classe Projeto.
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class PProjeto {
	//TODO: DOCUMENTAR
	protected ArrayList<Projeto> findFinishedProjects() {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Projeto> projects = (ArrayList<Projeto>) session.createCriteria(Projeto.class).add(Restrictions.ne("status", "votando")).list();
			persist.close(session);
			
			for (int i = 0; i < projects.size(); i++) {
				for (int j = i+1; j < projects.size(); j++) {
					if (projects.get(i).getId() == projects.get(j).getId()) {
						projects.remove(i);
						i--;
						break;
					}
				}
			}
			
			return projects;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR PROJETOS EM ANDAMENTO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	//TODO: DOCUMENTAR
	protected boolean regulateProjects() {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Projeto> projects = (ArrayList<Projeto>) session.createCriteria(Projeto.class).add(Restrictions.lt("prazo", new Date())).list();
			persist.close(session);
			
			for (int i = 0; i < projects.size(); i++) {
				for (int j = i+1; j < projects.size(); j++) {
					if (projects.get(i).getId() == projects.get(j).getId()) {
						projects.remove(i);
						i--;
						break;
					}
				}
			}
			
			for (Projeto project : projects) {
				if (!project.definirVotacao())
					return false;
			}
			
			return true;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO ATUALIZAR STATUS DE PROJETOS <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	//TODO: DOCUMENTAR
	protected ArrayList<Projeto> getOwnProjects(long idAutor) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Projeto> projects = (ArrayList<Projeto>) session.createCriteria(Projeto.class).add(Restrictions.eq("autor.id", idAutor)).list();
			persist.close(session);
			
			for (int i = 0; i < projects.size(); i++) {
				for (int j = i+1; j < projects.size(); j++) {
					if (projects.get(i).getId() == projects.get(j).getId()) {
						projects.remove(i);
						i--;
						break;
					}
				}
			}
			
			return projects;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR PROJETOS EM ANDAMENTO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	//TODO: DOCUMENTAR
	protected ArrayList<Projeto> getAllVotedProjects(long idEleitor) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Projeto> projects = (ArrayList<Projeto>) session.createCriteria(Projeto.class).createCriteria("votos.eleitor", "eleitor", Criteria.INNER_JOIN, Restrictions.eq("eleitor.id", idEleitor)).list();
			persist.close(session);
			
			for (int i = 0; i < projects.size(); i++) {
				for (int j = i+1; j < projects.size(); j++) {
					if (projects.get(i).getId() == projects.get(j).getId()) {
						projects.remove(i);
						i--;
						break;
					}
				}
			}
			
			return projects;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR PROJETOS EM ANDAMENTO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}
	
	//TODO: DOCUMENTAR
	protected boolean findProjectById(Projeto projeto) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Projeto project = (Projeto) session.find(Projeto.class, projeto.getId());
			persist.close(session);
			
			if (project == null) {
				projeto.setMsgError("Projeto não encontrado");
				return false;
			}
			
			projeto.setAutor(project.getAutor());
			projeto.setDataInicio(project.getDataInicio());
			projeto.setDescricao(project.getDescricao());
			projeto.setVotos(project.getVotos());
			projeto.setPrazo(project.getPrazo());
			projeto.setStatus(project.getStatus());
			projeto.setTitulo(project.getTitulo());
			
			return true;
		} catch (Exception e) {
			projeto.setMsgError("Erro ao localizar projeto com o id: " + projeto.getId());
			System.err.println("[ERROR]-> ERRO AO LOCALIZAR PROJETO PELO ID <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	//TODO: DOCUMENTAR
	protected boolean findCurrentProjectsByAutorId(Projeto projeto, long idAutor) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			Projeto project = (Projeto) session.createCriteria(Projeto.class).add(Restrictions.eq("status", "votando")).add(Restrictions.eq("autor.id", idAutor)).uniqueResult();
			persist.close(session);
			
			if (project == null) {
				projeto.setMsgError("Nenhum projeto em andamento");
				return false;
			}
			
			projeto.setAutor(project.getAutor());
			projeto.setDataInicio(project.getDataInicio());
			projeto.setDescricao(project.getDescricao());
			projeto.setId(project.getId());
			projeto.setVotos(project.getVotos());
			projeto.setPrazo(project.getPrazo());
			projeto.setStatus(project.getStatus());
			projeto.setTitulo(project.getTitulo());
			return true;
		} catch (Exception e) {
			projeto.setMsgError("Erro ao buscar projetos em andamento do autor com id: " + idAutor);
			System.err.println("[ERROR]-> ERRO AO BUSCAR PROJETOS EM ANDAMENTO DE UM AUTOR <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	protected boolean atualizarProjeto(Projeto projeto) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			session.update(projeto);
			persist.close(session);
			return true;	
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO ATUALIZAR O PROJETO "+projeto.getTitulo().toUpperCase()+" NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
		
	}
	
	protected boolean save(Projeto projeto) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			session.save(projeto);
			persist.close(session);
			return true;	
		} catch (ConstraintViolationException e) {
			projeto.setMsgError("PROJETO JÁ CADASTRADO");
			persist.close(session);
			return false;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO SALVAR O PROJETO "+projeto.getTitulo().toUpperCase()+" NO BANCO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return false;
		}
	}
	
	//TODO: DOCUMENTAR
	protected ArrayList<Projeto> findCurrentProjects(long id) {
		HibernateUtil persist = new HibernateUtil();
		Session session = persist.openSession();
		try {
			ArrayList<Projeto> projects = (ArrayList<Projeto>) session.createCriteria(Projeto.class).add(Restrictions.eq("status", "votando"))
					.add(Restrictions.ne("autor.id", id)).list();
			persist.close(session);
			
			for (int i = 0; i < projects.size(); i++) {
				for (int j = i+1; j < projects.size(); j++) {
					if (projects.get(i).getId() == projects.get(j).getId()) {
						projects.remove(i);
						i--;
						break;
					}
				}
			}
			
			return projects;
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO AO BUSCAR PROJETOS EM ANDAMENTO <-[ERROR]");
			e.printStackTrace();
			persist.close(session);
			return null;
		}
	}

}
