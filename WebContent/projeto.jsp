<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" import="model.Projeto" import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/forms.css">
  <script src="js/funcionalidades.js"></script>
  <script src="js/tratamento.js"></script>
   	<% 
	Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
	if (loggedUser == null) {
		request.getSession().invalidate();
		response.sendRedirect("login.jsp");
	}
	
	Projeto project = (Projeto) request.getSession().getAttribute("project");
	String msg = (String) request.getSession().getAttribute("msg");
	
	%>
  <title>PROJETO: <%= project.getTitulo().toUpperCase() %> | DIRECT DEMOCRACY</title>
</head>

<script>
  let projectApproval = <%= project.getProjectAproval() %>;
  let totalVotos = <%= project.getTotalVotos() %>;
  let initialDate = '<%= project.getDataInicioStringed() %>';
  let prazoProjeto = '<%= project.getPrazoStringed() %>';
</script>

<body onload='updateAllInputs();'>
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
            <a class="nav-link active" href="#">Projetos</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvRepresentante">Representante</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvLogout">Logout</a>
          </li>
        </ul>
      </div>
    </nav>
  </header>

  <main style="height: 88.75vh;">
    <div class="card" id="container-atualizar-cadastro">
      <div class="card-header">
        Dados do projeto
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
         <input type="hidden" value="projectId">
         <div class="form-group">
           <label for="titulo">Título</label>
           <input type="text" class="form-control" id="titulo" name="titulo" required onblur="validateTitulo()" disabled value='<%= project.getTitulo() %>'>
         </div>
         <div class="form-group">
           <label for="autor">Autor</label>
           <input type="text" class="form-control" id="autor" name="autor" required onblur="validateTitulo()" disabled value='<%= project.getAutor().getNome() %>'>
         </div>
         <div class="form-group">
           <label for="descricao">Descrição</label>
           <textarea class="form-control" id="descricao" rows="3" maxlength="300" style="resize: none;" required onblur="validateDesc()" disabled><%= project.getDescricao() %></textarea>
         </div>
         <div class="form-group">
           <label for="prazo">Prazo de votação</label>
           <input type="date" class="form-control" id="prazo" name="prazo" required onclick="getDataAtual()" onblur="validatePrazo()" disabled value='<%= project.getPrazoStringed() %>'>
         </div> <br>
          <div class="container"> 
            <div class="row justify-content-center">
            <%
        	String classeVotoPositivo;
        	String classeVotoNegativo;
        	if (project.getStatus().equals("votando")) {
                if (loggedUser.getRepresentante() != null) { // CASO TENHA UM REPRESENTANTE
                	String alerta = "";
                	if (project.verificarVoto(loggedUser.getRepresentante(), true)) {
                		classeVotoPositivo = "btn btn-success btn-lg";
                		classeVotoNegativo = "btn btn-outline-danger btn-lg";
                	} else if (project.verificarVoto(loggedUser.getRepresentante(), false)) {
                		classeVotoPositivo = "btn btn-outline-success btn-lg";
                		classeVotoNegativo = "btn btn-danger btn-lg";
                	} else {
                		classeVotoPositivo = "btn btn-outline-success btn-lg";
                		classeVotoNegativo = "btn btn-outline-danger btn-lg";
               		    alerta = "<div class='alert alert-warning' role='alert' style='margin-top: 20px'>" +
           							"Peça para seu presentante votar ou remova sua representação para votar você mesmo." +
           						 "</div>";
                	}
                	out.println(
                			"<div class='col-4'>" +
            					"<input type='submit' class='"+classeVotoPositivo+"' formmethod='POST' formaction='SvVotar' value='Apoiar' disabled/>" +
            				"</div>" +
                			"<div class='col-3'>" +
            					"<input type='submit' class='"+classeVotoNegativo+"' formmethod='POST' formaction='SvVotar' value='Não apoiar' disabled/>" +
            				"</div>" + alerta);
                } else { // CASO NÃO TENHA UM REPRESENTANTE
                	Integer status;
                	if (project.verificarVoto(loggedUser, true)) {
                		status = 1;
                		classeVotoPositivo = "btn btn-success btn-lg";
                		classeVotoNegativo = "btn btn-outline-danger btn-lg";
                	} else if (project.verificarVoto(loggedUser, false)) {
                		status = 2;
                		classeVotoPositivo = "btn btn-outline-success btn-lg";
                		classeVotoNegativo = "btn btn-danger btn-lg";
                	} else {
                		status = 0;
                		classeVotoPositivo = "btn btn-outline-success btn-lg";
                		classeVotoNegativo = "btn btn-outline-danger btn-lg";
                	}
                	out.println(
                			"<div class='col-4'>" +
                				"<form>" +
                					"<input type='hidden' value='"+status+"' name='status'>" +
                					"<input type='hidden' value='positivo' name='voto'>" +
            						"<input type='submit' class='"+classeVotoPositivo+"' formmethod='POST' formaction='SvVotar' value='Apoiar'/>" +
                				"</form>" +
            				"</div>" +
                			"<div class='col-3'>" +
                    			"<form>" +
                    				"<input type='hidden' value='"+status+"' name='status'>" +
                					"<input type='hidden' value='negativo' name='voto'>" +
            						"<input type='submit' class='"+classeVotoNegativo+"' formmethod='POST' formaction='SvVotar' value='Não apoiar'/>" +
      	            			"</form>" +
            				"</div>");
                }
        	} else {
        		String botao;
        		if (project.getStatus().equals("Aprovado")) {
        			botao = "<div class='col-4'>" +
        						"<input type='submit' class='btn btn-outline-success btn-lg' value='Projeto Aprovado' disabled/>" +
        					"</div>";
        		} else {
        			botao = "<div class='col-4'>" +
    							"<input type='submit' class='btn btn-outline-danger btn-lg' value='Projeto Rejeitado' disabled/>" +
    						"</div>";
        		}
        		out.println(botao);
        	}

            %>
            
            </div>
          </div>
      </div>
    </div>
    <div class="card" id="dados-projeto">
      <div class="card-body">
        <form>
          <fieldset disabled>
            <legend>Informações da votação</legend>
            <label for="project-approval">Aprovação do projeto</label>
            <div class="progress" style="height: 30px; background-color: #dc3545;">
              <div id="project-approval" class="progress-bar bg-success progress-bar-striped progress-bar-animated" role="progressbar"
              aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <b style="color: #198754;" id="positivos"></b>
            <b id="negativos" style="color: #dc3545; margin-left: 70%; text-overflow: ellipsis; white-space: nowrap;"></b>
            <br> <br>
            <div class="mb-3">
              <label for="total-votos" class="form-label">Total de votos</label>
              <input type="text" id="total-votos" class="form-control" aria-disabled="disabled">
            </div>
            <div class="form-group">
              <label for="initial-date">Data Inicial</label>
              <input type="date" class="form-control" id="initial-date" required disabled>
              <div class="invalid-feedback" id="prazo-feedback"></div>
            </div>
            <div class="mb-3">
              <label for="tempo-restante" class="form-label">Tempo restante</label>
              <input type="text" id="tempo-restante" class="form-control" aria-disabled="disabled" value="">
            </div>
          </fieldset>
          </form>
      </div>
    </div>
  </main>
</body>

</html>