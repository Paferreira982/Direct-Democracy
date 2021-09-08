<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" import="model.Projeto" import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/forms.css">
  <script src="js/funcionalidades.js"></script>
  <script src="js/tratamento.js"></script>
  <title>PROJETOS FINALIZADOS | DIRECT DEMOCRACY</title>
</head>
<body>
<%
	Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
	String msgProject = (String) request.getSession().getAttribute("msg");
	ArrayList<Projeto> currentProjects = (ArrayList<Projeto>) request.getSession().getAttribute("currentProjects");
	
	if (loggedUser == null) {
		request.getSession().invalidate();
		response.sendRedirect("login.jsp");
	}
%>
	<header>
    <nav class="navbar navbar-dark bg-dark navbar-expand-lg">
      <a class="navbar-brand" href="#">
        <img src="img/logo.png" width="30" height="30" class="d-inline-block align-top margin-left" alt="logotipo"
          id="logo">
        &ensp;Direct Democracy
      </a>
      <div class="collapse navbar-collapse margin-left-low" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" href="SvMenu">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvPerfil">Perfil</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvProjetos">Meu Projeto</a>
          </li>
          <li class="nav-item active">
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

  <main class="menu-props" style="width: 100%;">
    <div class="row">
      <div class="container" style="padding-left: 500px; padding-right: 500px">
        <div class="row justify-content-start">
          <h1 class="text-secondary text-center" style="margin-top: 20px;">PROJETOS FINALIZADOS</h1>
          
          <%
          if (loggedUser != null) {
              if (msgProject != null) {
  				out.println("<div class='alert alert-info' role='alert' style='margin-top: 100px;'>"+msgProject+"</div>");
  			} else {
  				for (Projeto project : currentProjects) {
  					out.println(
  							"<form class='col-3' action='SvProjetoPage' method='POST'>" +
  								"<input type='hidden' value='"+project.getId()+"' name='projectId'>" +
  								"<button type='submit' id='project-card' class='col-3 project-config button-card-project' formmethod='POST' formaction='SvProjeto'>" +
  									"<div class='card' style='width: 18rem;'>" +
  										"<img class='card-img-top' src='img/img-card.jpg'>" +
  										"<div class='card-body'>" +
  											"<h5 class='card-title'>"+project.getTitulo()+"</h5>" +
  											"<p class='card-text'>"+project.getDescricao()+"</p>" +
  										"</div>" +
  									"</div>" +
  								"</button>" +
  							"</form>");
  				}
  			}
          }
	%>
	  
        </div>
      </div>
    </div>
  </main>

  <footer class="bg-dark">
  </footer>
</body>
</html>