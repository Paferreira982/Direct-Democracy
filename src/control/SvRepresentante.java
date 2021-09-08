package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvRepresentante")
public class SvRepresentante extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser != null) {
			Usuario searchedRepresentante = null;
			String idStringed = request.getParameter("representanteId");
			Long id;
			if (idStringed != null) {
				id = Long.parseLong(idStringed);
				searchedRepresentante = new Usuario(id);
			} else if (loggedUser.getRepresentante() != null) {
				searchedRepresentante = loggedUser.getRepresentante();
			} else {
				request.getSession().setAttribute("msgProfile", "Você ainda não possui um representante, por favor o selecione por esta tela.");
				request.getRequestDispatcher("menu.jsp").forward(request, response);
			}
			
			if (searchedRepresentante != null) {
				if (searchedRepresentante.find()) {
					Projeto persist = new Projeto();
					ArrayList<Projeto> projetosPropios = persist.getOwnProjects(searchedRepresentante.getId());
					ArrayList<Projeto> projetosVotados = persist.getAllVotedProjects(searchedRepresentante.getId());
					
					projetosVotados.addAll(projetosPropios);
					
					request.getSession().setAttribute("searchedRepresentante", searchedRepresentante);
					request.getSession().setAttribute("projetosVotados", projetosVotados);
				} else {
					request.getSession().invalidate();
					response.sendRedirect("login.jsp");
				}
				
				request.getRequestDispatcher("representante.jsp").forward(request, response);
			}

			
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
