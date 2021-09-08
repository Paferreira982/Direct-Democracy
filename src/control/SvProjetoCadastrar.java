package control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvProjetoCadastrar")
public class SvProjetoCadastrar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.getSession().removeAttribute("msg");
			Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
			
			if (loggedUser != null) {
				String titulo = request.getParameter("titulo");
				String descricao = request.getParameter("descricao");
				Date dataInicio = new Date();
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				Date prazo = simpleDate.parse(request.getParameter("prazo"));
				
				Projeto projeto = new Projeto(titulo, descricao, dataInicio, prazo, loggedUser);
				
				if (projeto.cadastrar())
					request.getSession().setAttribute("msgCadastroProjeto", "Cadastro realizado com sucesso");
				else
					request.getSession().setAttribute("msg", projeto.getMsgError());
				
				request.getRequestDispatcher("SvProjetos").forward(request, response);
				
			} else {
				request.getSession().invalidate();
				response.sendRedirect("login.jsp");
			}
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO NO SERVLET DE CADASTRO DE PROJETOS <-[ERROR]");
			e.printStackTrace();
		}
	}

}
