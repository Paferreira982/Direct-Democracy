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

@WebServlet("/SvUsuarioCadastro")
public class SvUsuarioCadastro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.getSession().removeAttribute("msg");
			String nome = request.getParameter("nome");
			String cpf = request.getParameter("cpf");
			String senha = request.getParameter("senha");
			String sexo = request.getParameter("sexo");
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
			Date dataNascimento = simpleDate.parse(request.getParameter("dataNascimento"));
			
			Usuario user = new Usuario(nome, cpf, senha, sexo, dataNascimento);
			
			if (user.cadastrar())
				request.setAttribute("msg", "Cadastro realizado com sucesso.");
			else
				request.setAttribute("msg", user.getMsgError());
			
			request.getRequestDispatcher("tela_cadastro.jsp").forward(request, response);
			
		} catch (Exception e) {
			System.err.println("[ERROR]-> ERRO NO SERVLET DE CADASTRO DE USUARIOS <-[ERROR]");
			e.printStackTrace();
		}

	}

}
