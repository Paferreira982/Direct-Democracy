package control;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;


@WebServlet("/SvUsuarioPerfil")
public class SvUsuarioPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.getSession().removeAttribute("msg");
			Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
			
			if (loggedUser != null) {
	              SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	              String dataNascimento = formato.format(loggedUser.getDataNascimento());
	              request.setAttribute("dataNascimento", dataNascimento);
			} else {
				request.getSession().invalidate();
				response.sendRedirect("login.jsp");
			}
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO NO SERVLET DE ACESSO AO PERFIL DE USUÁRIO <-[ERROR]");
			e.printStackTrace();
		}
	}

}
