package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvProjetoAtualizar")
public class SvProjetoAtualizar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.getSession().removeAttribute("msg");
			Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
			
			if (loggedUser != null) {
				Projeto project = (Projeto) request.getSession().getAttribute("selfProject");
				Long id = Long.parseLong(request.getParameter("projectId"));
				String titulo = request.getParameter("titulo");
				String descricao = request.getParameter("descricao");
				
				Projeto projeto = new Projeto(id, titulo, descricao, project.getDataInicio(), project.getPrazo(),
						project.getStatus(), project.getAutor(), project.getVotos());
				
				if (projeto.atualizar())
					request.getSession().setAttribute("msgCadastroProjeto", "Projeto atualizado com sucesso");
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
