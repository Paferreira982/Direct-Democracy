package control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;

@WebServlet("/SvUsuarioAtualizar")
public class SvUsuarioAtualizar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		Usuario userReserva = loggedUser;
		try {
			String senhaAtual = request.getParameter("senhaAtual");
			String senhaNova = request.getParameter("senha");

			if (loggedUser != null) {
				String nome = request.getParameter("nome");
				String cpf = request.getParameter("cpf");
				String sexo = request.getParameter("sexo");
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				Date dataNascimento = simpleDate.parse(request.getParameter("dataNascimento"));
				
				loggedUser.setCpf(cpf);
				loggedUser.setDataNascimento(dataNascimento);
				loggedUser.setNome(nome);
				loggedUser.setSexo(sexo);

				if (senhaNova != null && !senhaNova.equals("")) {
					if (loggedUser.atualizar(senhaAtual, senhaNova))
						request.getSession().setAttribute("msg", "Perfil atualizado com sucesso");
					else {
						loggedUser = userReserva;
						request.getSession().setAttribute("msg", loggedUser.getMsgError());
					}
						
				} else {
					if (loggedUser.atualizar(senhaAtual))
						request.getSession().setAttribute("msg", "Perfil atualizado com sucesso");
					else {
						loggedUser = userReserva;
						request.getSession().setAttribute("msg", loggedUser.getMsgError());
					}
				}
				
				request.getSession().setAttribute("loggedUser", loggedUser);
				request.getRequestDispatcher("perfil.jsp").forward(request, response);
			} else {
				request.getSession().invalidate();
				response.sendRedirect("login.jsp");
			}
		} catch (Exception e) {
			loggedUser = userReserva;
			System.err.println("[ERROR]-> ERRO NO SERVLET DE ATUALIZAR USUARIOS <-[ERROR]");
			e.printStackTrace();
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
