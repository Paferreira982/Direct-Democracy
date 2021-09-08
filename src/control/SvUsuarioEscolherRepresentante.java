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

@WebServlet("/SvUsuarioEscolherRepresentante")
public class SvUsuarioEscolherRepresentante extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		Long id = Long.parseLong(request.getParameter("representanteId"));
		Integer status = Integer.parseInt(request.getParameter("status"));
		
		if (loggedUser != null) {
			if (status == 1) {
				
				if(loggedUser.removeRepresentante())
					request.getSession().setAttribute("msg", "Representante removido com sucesso");
				else
					request.getSession().setAttribute("msg", loggedUser.getMsgError());
				
			} else { 
				Projeto persist = new Projeto();
				ArrayList<Projeto> projetosVotados = persist.getAllVotedProjects(loggedUser.getId());
				for (Projeto project : projetosVotados) {
					if (project.getStatus().equals("votando")) {
						project.retirarVoto(loggedUser);
					}
				}
				
				if(loggedUser.setRepresentante(id))
					request.getSession().setAttribute("msg", "Representante marcado com sucesso");
				else
					request.getSession().setAttribute("msg", loggedUser.getMsgError());
				
			}
			
			request.getRequestDispatcher("representante.jsp").forward(request, response);
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
