package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvProjeto")
public class SvProjeto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		Long id = Long.parseLong(request.getParameter("projectId"));
		
		if (loggedUser != null) {
			Projeto project = new Projeto();
			project.setId(id);
			
			if (project.findById())
		  		request.getSession().setAttribute("project", project);
			else
				request.getSession().setAttribute("msgProject", project.getMsgError());

			request.getRequestDispatcher("projeto.jsp").forward(request, response);
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
