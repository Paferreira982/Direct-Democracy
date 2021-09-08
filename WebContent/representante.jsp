<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" import="model.Projeto" import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/forms.css">
  <script src="js/funcionalidades.js"></script>
  <script src="js/tratamento.js"></script>
  <title>REPRESENTANTE | DIRECT DEMOCRACY</title>
</head>
<%
	Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
	Usuario searchedRepresentante = (Usuario) request.getSession().getAttribute("searchedRepresentante");
	ArrayList<Projeto> projetosVotados = (ArrayList<Projeto>) request.getSession().getAttribute("projetosVotados");
	String msg = (String) request.getAttribute("msg");
	if (loggedUser == null) {
		request.getSession().invalidate();
		response.sendRedirect("login.jsp");
	}
	
%>
<body>
  <header>
    <nav class="navbar navbar-dark bg-dark navbar-expand-lg">
      <a class="navbar-brand" href="#">
        <img src="img/logo.png" width="30" height="30" class="d-inline-block align-top margin-left" alt="logotipo"
          id="logo">
        &ensp;Direct Democracy
      </a>
       <div class="collapse navbar-collapse margin-left-low" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item active">
            <a class="nav-link" href="SvMenu">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvPerfil">Perfil</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvProjetos">Meu Projeto</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvTodosProjetos">Projetos</a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="#">Representante</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvLogout">Logout</a>
          </li>
        </ul>
      </div>
    </nav>
  </header>

  <div style="height: 88.75vh;"></div>

  <main>
    <div class="card" id="container-representante">
      <div class="card-header">
        Perfil de representante
      </div>
      <div class="card-body">
 	    <%
        if (msg != null) {
        	if (msg.contains("sucesso"))
        		out.println("<div class='alert alert-success' role='alert'>"+msg+"</div>");
        	else
        		out.println("<div class='alert alert-danger' role='alert'>"+msg+"</div>");
        }
        %>
        <div class="row justify-content-center">
          <div class="col-6">
            <img src='img/profile-female-img.png' class='rounded-circle' width='64' height='64'>
            <span class="fs-5">&ensp;<%= searchedRepresentante.getNome() %></span>
          </div>
          <div class="col-4">
            <div style="margin-top: 13px; margin-left: -30px;">
              <img src='img/mega-fone.png' width="35">
              <span>Influência: <%= searchedRepresentante.getInfluencia()-1 %> representado(s)</span>
            </div>
          </div>
        </div>

        <div class="container" style="margin-top: 15px;">
          <div class="row justify-content-center">
            <div class="col-5">
              <form>
                <input type="hidden" name="representanteId" value="<%= searchedRepresentante.getId() %>">
                <%
                if (loggedUser.getRepresentante() != null) {
                	if (loggedUser.getRepresentante().getId() == searchedRepresentante.getId()) {
                    	out.println("<input type='hidden' name='status' value='1'>");
                    	out.println("<input type='submit' class='btn btn-success btn-lg' value='Desmarcar como representante' formaction='SvUsuarioEscolherRepresentante' formmethod='POST'/>");
                	} else {
                    	out.println("<input type='hidden' name='status' value='0'>");
                    	out.println("<input type='submit' class='btn btn-outline-success btn-lg' value='Marcar como representante' formaction='SvUsuarioEscolherRepresentante' formmethod='POST'/>");
                    }
                } else {
                	out.println("<input type='hidden' name='status' value='0'>");
                	out.println("<input type='submit' class='btn btn-outline-success btn-lg' value='Marcar como representante' formaction='SvUsuarioEscolherRepresentante' formmethod='POST'/>");
                }
                %>
                
              </form>
            </div>
          </div>
        </div>

        <div class="row justify-content-center" style="margin-top: 14px; border-top: 2px solid black; padding-top: 10px;">
          <div class="col-5">
            <span class="fs-2">&ensp;Histórico de Votos</span>
          </div>
        </div>
        
        <%
        if (projetosVotados == null || projetosVotados.isEmpty()) {
        	out.println("<br><div class='alert alert-warning' role='alert'>Este usuário não votou em nenhum projeto</div>");
        } else {
        	for (Projeto project : projetosVotados) {
        		String corpoHtml = "<div class='container' style='margin-bottom: 15px;'>" +
    					"<div class='row row-cols-1'>" +
						"<div class='col container' style='border-radius: 20px; padding: 10px; margin-top: 15px; color: white; background-color: rgb(53, 53, 53);'>" +
							"<div class='row row-cols-2 justify-content-center'>" +
								"<div class='col'>" +
									"<label for='titulo'>Título</label>" +
									"<input type='text' class='form-control' id='titulo' name='titulo' disabled value='"+project.getTitulo()+"'>" +
								"</div>" +
								"<div class='col'>" +
									"<label for='autor'>Autor</label>" +
									"<input type='text' class='form-control' id='autor' name='autor' disabled value='"+project.getAutor().getNome()+"'>" +
								"</div>" +
								"<div class='col-12'>" +
									"<label for='descricao'>Descrição</label>" +
									"<textarea class='form-control' id='descricao' rows='3' maxlength='300' style='resize: none;' disabled>"+project.getDescricao()+"</textarea> <br>" +
								"</div>" +
								"<div class='col-5'>";
				if (project.verificarVoto(searchedRepresentante, true)) {
					corpoHtml += "<input type='submit' class='btn btn-success btn-lg' value='Projeto Apoiado' disabled/>";
				} else {
					corpoHtml += "<input type='submit' class='btn btn-danger btn-lg' value='Projeto Não Apoiado' disabled/>";
				}
				
				corpoHtml += 
								"</div>" +
									"<div class='col-3'>" +
									"<form>" +
										"<input type='hidden' name='projectId' value='"+project.getId()+"'>" +
										"<input type='submit' class='btn btn-info btn-lg' value='Visualizar Projeto' formaction='SvProjeto' formmethod='POST'/>" +
									"</form>" +
									"</div>" +
								"</div>" +
							"</div>" +
						"</div>" +
					"</div>";
				
        		out.println(corpoHtml);
        	}
        }
        %>
      </div>
    </div>

  </main>

  <footer class="bg-dark">
  </footer>

</body>

</html>