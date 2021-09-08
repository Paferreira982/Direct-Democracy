package control;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;

@WebServlet("/SvPerfil")
public class SvPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        String dataNascimento = dateFormat.format(loggedUser.getDataNascimento());
	        request.getSession().setAttribute("dataNascimento", dataNascimento);
	        request.getRequestDispatcher("perfil.jsp").forward(request, response);
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
