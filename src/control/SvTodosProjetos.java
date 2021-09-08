package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvTodosProjetos")
public class SvTodosProjetos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser != null) {
			Projeto persist = new Projeto();
			List<Projeto> finishedProjects = new ArrayList<Projeto>();
			finishedProjects = persist.findFinishedProjects();
			
			request.getSession().setAttribute("currentProjects", finishedProjects);
			request.getRequestDispatcher("projetos-finalizados.jsp").forward(request, response);
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
