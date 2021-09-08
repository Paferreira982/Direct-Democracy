package control;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;

@WebServlet("/SvUsuarioBuscar")
public class SvUsuarioBuscar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msgProfile");
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser != null) {
			String nomeBuscado = request.getParameter("nome");
			ArrayList<Usuario> searchedUsers = new ArrayList<Usuario>();
			
			if (nomeBuscado != null && !nomeBuscado.equals(""))
				searchedUsers = loggedUser.getUsersByName(nomeBuscado);
			else
				searchedUsers = loggedUser.getRandonUsers(loggedUser.getId());
			
			if (searchedUsers.isEmpty())
				request.getSession().setAttribute("msgProfile", "Nenhum usuário encontrado com o nome: " + nomeBuscado);
			
			request.getSession().setAttribute("searchedUsers", searchedUsers);
			
			request.getRequestDispatcher("menu.jsp").forward(request, response);
			
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
