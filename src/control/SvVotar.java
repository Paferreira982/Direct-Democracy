package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvVotar")
public class SvVotar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser != null) {
			Integer status = Integer.parseInt(request.getParameter("status"));
			String voto = request.getParameter("voto");
			Projeto project = (Projeto) request.getSession().getAttribute("project");
			
			boolean votoBooleano;
			if (voto.equals("positivo"))
				votoBooleano = true;
			else
				votoBooleano = false;
			
			if (status == 0) {
				
				if (project.votar(loggedUser, votoBooleano))
					request.getSession().setAttribute("msg", "Voto computado com sucesso");
				else
					request.getSession().setAttribute("msg", project.getMsgError());
				
			} else if (status == 1 && votoBooleano) {
				
				if (project.retirarVoto(loggedUser))
					request.getSession().setAttribute("msg", "Voto removido com sucesso");
				else
					request.getSession().setAttribute("msg", project.getMsgError());
				
			} else if (status == 1 && votoBooleano == false) {
				
				if (project.retirarVoto(loggedUser) && project.votar(loggedUser, votoBooleano))
					request.getSession().setAttribute("msg", "Voto computado com sucesso");
				else
					request.getSession().setAttribute("msg", project.getMsgError());
				
			} else if (status == 2 && votoBooleano) {
				
				if (project.retirarVoto(loggedUser) && project.votar(loggedUser, votoBooleano))
					request.getSession().setAttribute("msg", "Voto computado com sucesso");
				else
					request.getSession().setAttribute("msg", project.getMsgError());
				
			} else if (status == 2 && votoBooleano == false) {
				
				if (project.retirarVoto(loggedUser))
					request.getSession().setAttribute("msg", "Voto removido com sucesso");
				else
					request.getSession().setAttribute("msg", project.getMsgError());
				
			}
			
			request.getRequestDispatcher("projeto.jsp").forward(request, response);
			
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
