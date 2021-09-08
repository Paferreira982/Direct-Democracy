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

@WebServlet("/SvMenu")
public class SvMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		ArrayList<Usuario> searchedUsers = new ArrayList<Usuario>();
		ArrayList<Projeto> currentProjects = new ArrayList<Projeto>();
		
		if (loggedUser != null) {
			Usuario user = new Usuario();
			searchedUsers = user.getRandonUsers(loggedUser.getId());
			
			if (searchedUsers.isEmpty())
				request.getSession().setAttribute("msgProfile", "Nenhum usuário encontrado");
			
			Projeto pj = new Projeto();
			currentProjects = pj.findCurrentProjects(loggedUser.getId());
			
			if (currentProjects.isEmpty())
				request.getSession().setAttribute("msgProject", "Nenhum projeto em votação no momento");
			
			request.getSession().setAttribute("searchedUsers", searchedUsers);
			request.getSession().setAttribute("currentProjects", currentProjects);
			
			request.getRequestDispatcher("menu.jsp").forward(request, response);
			
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
