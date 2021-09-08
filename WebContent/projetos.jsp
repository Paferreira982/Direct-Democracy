<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" import="model.Projeto" import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="pt-br">
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
  	
	Projeto representedProject = (Projeto) request.getSession().getAttribute("representedProject");
	Projeto selfProject = (Projeto) request.getSession().getAttribute("selfProject");
	
	String msgRepresented = (String) request.getSession().getAttribute("msgRepresented");
	String msgSelf = (String) request.getSession().getAttribute("msgSelf");
	String dataInicial = (String) request.getSession().getAttribute("dataInicial");
	String prazo = (String) request.getSession().getAttribute("prazo");
	String msgCadastroProjeto = (String) request.getSession().getAttribute("msgCadastroProjeto");
	
	Double approval = null;
	Integer votos = null;
	if (representedProject == null && selfProject != null) {
		approval = selfProject.getProjectAproval();
		votos = selfProject.getTotalVotos();
	} else if (selfProject == null && representedProject != null){
		approval = representedProject.getProjectAproval();
		votos = representedProject.getTotalVotos();
	}
	%>
  <script>
    let projectApproval = <%= approval %>;
    let totalVotos = <%= votos %>;
    let initialDate = '<%= dataInicial %>';
    let prazoProjeto = '<%= prazo %>';
  </script>
  <title>PROJETOS | DIRECT DEMOCRACY</title>
</head>
<body onload="updateAllInputs();">
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
            <a class="nav-link active" href="#">Meu Projeto</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="SvTodosProjetos">Projetos</a>
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
  <%
  	out.println("<main style='height: 88.75vh;'>");
    if (representedProject != null) { // REPRESENTANTE COM PROJETO EM VOTAÇÃO
    	
    	
    	out.println(
    	"<div class='card' id='container-atualizar-cadastro'>" +
	    	"<div class='card-header'>Dados do projeto</div>" +
	    	"<div class='card-body'>" +
	    		"<form>" +
	    			"<input type='hidden' value='"+representedProject.getId()+"'>" +
	    			"<div class='form-group'>" +
	    				"<label for='titulo'>Título</label>" +
	    				"<input type='text' class='form-control' id='titulo' name='titulo' required onblur='validateTitulo()' disabled value='"+representedProject.getTitulo()+"'>" +
	    			"</div>" +
	    			"<div class='form-group'>" +
	    				"<label for='autor'>Autor</label>" +
	    				"<input type='text' class='form-control' id='autor' name='autor' required onblur='validateTitulo()' disabled value='"+representedProject.getAutor().getNome()+"'>" +
	    			"</div>" +
	    			"<div class='form-group'>" +
	    				"<label for='descricao'>Descrição</label>" +
	    				"<textarea class='form-control' id='descricao' rows='3' maxlength='300' name='descricao' style='resize: none;' required onblur='validateDesc()' disabled value='"+representedProject.getDescricao()+"'></textarea>" +
	    			"</div>" +
	    			"<div class='form-group'>" +
	    				"<label for='prazo'>Prazo de votação</label>" +
	    				"<input type='date' class='form-control' id='prazo' name='prazo' required onclick='getDataAtual()' value='"+representedProject.getPrazo()+"' onblur='validatePrazo()' disabled>" +
	    			"</div>" +
	    		"</form>" +
	    	"</div>" +
    	"</div>" +
    	"<div class='card' id='dados-projeto' style='margin-top: -100px;'>" +
    		"<div class='card-body'>" +
    			"<form>" +
    				"<fieldset disabled>" +
    					"<legend>Informações da votação</legend>" +
    					"<label for='project-approval'>Aprovação do projeto</label>" +
    					"<div class='progress' style='height: 30px; background-color: #dc3545;'>" +
    						"<div id='project-approval' class='progress-bar bg-success progress-bar-striped progress-bar-animated' role='progressbar'aria-valuenow='50' aria-valuemin='0' aria-valuemax='100'></div>" +
    					"</div>" +
    					"<b style='color: #198754;' id='positivos'></b>" +
    					"<b id='negativos' style='color: #dc3545; margin-left: 70%; text-overflow: ellipsis; white-space: nowrap;'></b> <br> <br>" +
    					"<div class='mb-3'>" +
    						"<label for='total-votos' class='form-label'>Total de votos</label>" +
    						"<input type='text' id='total-votos' class='form-control' aria-disabled='disabled'>" +
    					"</div>" +
   						"<div class='form-group'>" +
   							"<label for='initial-date'>Data Inicial</label>" +
    						"<input type='date' class='form-control' id='initial-date' required disabled>" +
    					"</div>" +
    					"<div class='mb-3'>" +
    						"<label for='tempo-restante' class='form-label'>Tempo restante</label>" +
    						"<input type='text' id='tempo-restante' class='form-control' aria-disabled='disabled' value=''>" +
    					"</div>" +
    				"</fieldset>");
    								
    								
    } else if (msgRepresented != null) { // REPRESENTANTE SEM PROJETO EM VOTAÇÃO
    	
    	
    	out.println(
    	"<div class='card' id='container-cadastro'>" +	
    		"<h5 class='card-header'>Projetos do seu representante</h5>" +
    		"<div class='card-body'>" +	
    			"<div class='alert alert-warning ' role='alert'>Seu representante não possui um projeto em votação no momento.</div>" +
    			"<h5 class='card-title'>Acompanhe o seu representante</h5>" +	
    			"<p class='card-text'>Veja quais e como seu representante está votando nos projetos.</p>" +
    			"<form>" +	
    				"<input type='hidden' name='idAutor' value='"+loggedUser.getRepresentante().getId()+"'>" +
    				"<button type='submit' class='btn btn-secondary btn' formaction='SvRepresentante' formmethod='post'>Acompanhar</button>");
    
    
    } else if (selfProject != null) { // SEM REPRESENTANTE COM PROJETO EM VOTAÇÃO
    	
    	
    	request.getSession().setAttribute("selfProject", selfProject);
    
    	out.println(
    			"<div class='card' id='container-atualizar-cadastro'>" +
    				"<div class='card-header'>Dados do projeto</div>" +
    				"<div class='card-body'>");
    
    	if (msgCadastroProjeto != null) {
    		out.println("<div class='alert alert-success' role='alert'>"+msgCadastroProjeto+"</div>");
    		request.getSession().removeAttribute("msgCadastroProjeto");
    	}
    	
    	out.println(
    			"<form>" +
    				"<input type='hidden' value='"+selfProject.getId()+"' name='projectId'>" +
    				"<div class='form-group'>" +
    					"<label for='titulo'>Título</label>" +
    					"<input type='text' class='form-control' id='titulo' name='titulo' placeholder='Informe o título do projeto' required onblur='validateTitulo()' value='"+selfProject.getTitulo()+"'>" +
    					"<div class='invalid-feedback' id='titulo-feedback'></div>" +
					"</div>" +
    				"<div class='form-group'>" +
 						"<label for='descricao'>Descrição</label>" +
    					"<textarea class='form-control' id='descricao' rows='3' maxlength='300' style='resize: none;' name='descricao' required onblur='validateDesc()'>"+selfProject.getDescricao()+"</textarea>" +
    					"<div class='invalid-feedback' id='descricao-feedback'></div>" +
    				"</div>" +
    				"<div class='form-group'>" +
    					"<label for='prazo'>Prazo de votação</label>" +
    					"<input type='date' class='form-control' id='prazo' name='prazo' required onclick='getDataAtual()' onblur='validatePrazo()' disabled>" +
    				"</div>" +
    				"<div class='mb-3' style='margin-top: 10px;'>" +
    					"<label for='project-image' class='form-label'>Imagem do projeto</label>" +
    					"<input class='form-control' type='file' id='project-image'>" +
    				"</div> <br>" +
    				"<div class='d-grid gap-2'>" +
    					"<button type='submit' class='btn btn-secondary btn-lg' formaction='SvProjetoAtualizar' formmethod='post'>Atualizar projeto</button>" +
    				"</div>" +
    			"</form>" +
    		"</div>" +
    	"</div>" +
    	"<div class='card' id='dados-projeto'>" +
    		"<div class='card-body'>" +
    			"<form>" +
    				"<fieldset disabled>" +
    					"<legend>Informações da votação</legend>" +
    					"<label for='project-approval'>Aprovação do projeto</label>" +
    					"<div class='progress' style='height: 30px; background-color: #dc3545;'>" +
    						"<div id='project-approval' class='progress-bar bg-success progress-bar-striped progress-bar-animated' role='progressbar' aria-valuenow='50' aria-valuemin='0' aria-valuemax='100'></div>" +
    					"</div>" +
    					"<b style='color: #198754;' id='positivos'></b>" +
    					"<b id='negativos' style='color: #dc3545; margin-left: 70%; text-overflow: ellipsis; white-space: nowrap;'></b> <br> <br>" +
    					"<div class='mb-3'>" +
    						"<label for='total-votos' class='form-label'>Total de votos</label>" +
    						"<input type='text' id='total-votos' class='form-control' aria-disabled='disabled'>" +
    					"</div>" +
    					"<div class='form-group'>" +
    						"<label for='initial-date'>Data Inicial</label>" +
    						"<input type='date' class='form-control' id='initial-date' required disabled>" +
    					"</div>" +
    					"<div class='mb-3'>" +
    						"<label for='tempo-restante' class='form-label'>Tempo restante</label>" +
    						"<input type='text' id='tempo-restante' class='form-control' aria-disabled='disabled' value=''>" +
    					"</div>" +
    				"</fieldset>");
    					
    								
    } else if (msgSelf != null) { // SEM REPRESENTANTE SEM PROJETO EM VOTAÇÃO
    	
    	
    	out.println(
    			"<div class='card' id='container-cadastro'>" +
    	    		"<div class='card-header'>Cadastrar projeto</div>" +
    				"<div class='card-body'>" +
    	    		"<form>" +
    					"<div class='form-group'>" +
    	    				"<label for='titulo'>Título</label>" +
    						"<input type='text' class='form-control' id='titulo' name='titulo' placeholder='Informe o título do projeto' required onblur='validateTitulo()'>" +
    	    				"<div class='invalid-feedback' id='titulo-feedback'></div>" +
    					"</div>" +
    	    			"<div class='form-group'>" +
    						"<label for='descricao'>Descrição</label>" +
    	    				"<textarea class='form-control' id='descricao' rows='3' maxlength='300' style='resize: none;' name='descricao' required onblur='validateDesc()'></textarea>" +
    						"<div class='invalid-feedback' id='descricao-feedback'></div>" +
    	    			"</div>" +
    					"<div class='form-group'>" +
    	    				"<label for='prazo'>Prazo de votação</label>" +
    						"<input type='date' class='form-control' id='prazo' name='prazo' required onclick='getDataAtual()' onblur='validatePrazo()'>" +
    	    				"<div class='invalid-feedback' id='prazo-feedback'></div>" +
    					"</div>" +
    	    			"<div class='mb-3' style='margin-top: 10px;'>" +
    						"<label for='project-image' class='form-label'>Imagem do projeto</label>" +
    	    				"<input class='form-control' type='file' id='project-image'>" +
    	    			"</div> <br>" +
    	    			"<div class='d-grid gap-2'>" +
    	    				"<button type='submit' class='btn btn-secondary btn-lg' formaction='SvProjetoCadastrar' formmethod='post'>Propor projeto</button>" +
    	    			"</div>");
    								
    								
    } else { // SITUAÇÃO QUE NÃO PODE OCORRER (ERRO)
    	
    }
    out.println(
				"</form>" +
			"</div>" +
		"</div>" +
	"</main>");
  %>
  
</body>
</html>