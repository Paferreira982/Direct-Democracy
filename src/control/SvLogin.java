package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;

@WebServlet("/SvLogin")
public class SvLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		
		Usuario user = new Usuario();
		String cpf = request.getParameter("cpf");
		String senha = request.getParameter("senha");
		
		if (user.login(cpf, senha)) {
			request.getSession().setAttribute("loggedUser", user);
			request.getRequestDispatcher("SvMenu").forward(request, response);
		} else {
			request.setAttribute("cpfSended", cpf);
			request.setAttribute("msg", user.getMsgError());
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}
