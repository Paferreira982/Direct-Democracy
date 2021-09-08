package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Projeto;
import model.Usuario;

@WebServlet("/SvProjetos")
public class SvProjetos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("msg");
		Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser != null) {
			Projeto currentProject = new Projeto();
			if (loggedUser.getRepresentante() != null) { // TEM UM REPRESENTANTE
			  	if(currentProject.findCurrentProjectsByAutorId(loggedUser.getRepresentante().getId())) { // PROUCURA POR PROJETOS EM VOTAÇÃO DO REPRESENTANTE
			  		
			        String dataInicial = currentProject.getDataInicioStringed();
			        String prazo = currentProject.getPrazoStringed();
			        
			  		request.getSession().setAttribute("representedProject", currentProject); // TEM REPRESENTANTE COM UM PROJETO EM VOTAÇÃO
			  		request.getSession().setAttribute("dataInicial", dataInicial);
			  		request.getSession().setAttribute("prazo", prazo);
			  	} else
			  		request.getSession().setAttribute("msgRepresented", currentProject.getMsgError()); // TEM REPRESENTANTE SEM PROJETO EM VOTAÇÃO
			  	
			} else { // NÃO TEM UM REPRESENTANTE
				if(currentProject.findCurrentProjectsByAutorId(loggedUser.getId())) {
					
			        String dataInicial = currentProject.getDataInicioStringed();
			        String prazo = currentProject.getPrazoStringed();
			        
			  		request.getSession().setAttribute("dataInicial", dataInicial);
			  		request.getSession().setAttribute("prazo", prazo);
					request.getSession().setAttribute("selfProject", currentProject); // NÃO TEM REPRESENTANTE E POSSUI PROJETO EM VOTAÇÃO
				} else
					request.getSession().setAttribute("msgSelf", currentProject.getMsgError()); // NÃO TEM REPRESENTANTE E NÃO POSSUI PROJETO EM VOTAÇÃO
				
			}
			
			request.getRequestDispatcher("projetos.jsp").forward(request, response);
			
		} else {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		}
	}

}
