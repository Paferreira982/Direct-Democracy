<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Usuario"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/forms.css">
  <script src="js/funcionalidades.js"></script>
  <script src="js/tratamento.js"></script>
  <title>PERFIL | DIRECT DEMOCRACY</title>
</head>
<body onload="tratarCPF()">
	<%
	Usuario loggedUser = (Usuario) request.getSession().getAttribute("loggedUser");
	if (loggedUser == null) {
		request.getSession().invalidate();
		response.sendRedirect("login.jsp");
	}
	
	String dataNascimento = (String) request.getSession().getAttribute("dataNascimento");
	String msg = (String) request.getSession().getAttribute("msg");
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
	          <li class="nav-item active">
	            <a class="nav-link" href="SvMenu">Home</a>
	          </li>
	          <li class="nav-item">
	            <a class="nav-link active" href="#">Perfil</a>
	          </li>
	          <li class="nav-item">
	            <a class="nav-link" href="SvProjetos">Meu Projeto</a>
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
    
      <main>
        <div class="card" id="container-cadastro">
          <div class="card-header">
            Perfil
          </div>
          <div class="card-body">
            <form>
              <div class="form-group">
	            <%
	            if (msg != null) {
	            	if (msg.contains("sucesso"))
	            		out.println("<div class='alert alert-success' role='alert'>"+msg+"</div>");
	            	else
	            		out.println("<div class='alert alert-danger' role='alert'>"+msg+"</div>");
	            }
	            %>
                <label for="nome">Nome completo</label>
                <input type="text" class="form-control" id="nome" name="nome" placeholder="Insira seu nome completo" required onblur="validateNome()" value='<%= loggedUser.getNome() %>'>
                <div class="invalid-feedback" id="nome-feedback"></div>
              </div>
              <div class="form-group">
                <label for="sexo">Sexo</label>
                <select class="form-control" id="sexo" name="sexo" onblur="validateSex()" required>
                  <%
                  out.println("<option selected='selected' hidden value='"+loggedUser.getSexo()+"'>"+loggedUser.getSexo()+"</option>");
                  %>
                  <option>Masculino</option>
                  <option>Feminino</option>
                </select>
                <div class="invalid-feedback" id="sexo-feedback"></div>
              </div>
              <div class="form-group">
                <label for="input-cpf">CPF</label>
                <input type="text" class="form-control" id="input-cpf" name="cpf" placeholder="Insira seu cpf" onkeyup="tratarCPF();" 
                maxlength="14" autocomplete="off" required onblur="validateCPF()" value='<%= loggedUser.getCpf() %>'>
                <div class="invalid-feedback" id="cpf-feedback"></div>
              </div>
              <div class="form-group">
                <label for="dataNascimento">Data de nascimento</label>
                <input type="date" class="form-control" id="dataNascimento" name="dataNascimento" required onblur="validateData()" value='<%= dataNascimento %>'>
                <div class="invalid-feedback" id="dataNascimento-feedback" onclick="adjustMaxAndMinOfDate()"></div>
              </div>
              <div class="form-group">
                <label for="senha">Nova Senha</label>
                <input type="password" class="form-control" id="senha" name="senha" placeholder="Insira uma senha nova" onkeyup="verificarSenha(); controlRequired();"
                autocomplete="off" maxlength="15">
              </div>
              <div id="cointainer-alerta" class="alert alert-warning" role="alert">
                <img src="img/x-vermelho.png" id="x-vermelho-tamanho" class="x-vermelho"> <span class="text-secondary">Tamanho mínimo de 6 digitos</span><br>
                <img src="img/x-vermelho.png" id="x-vermelho-letra" class="x-vermelho"> <span class="text-secondary">Pelo menos uma letra</span> <br>
                <img src="img/x-vermelho.png" id="x-vermelho-numero" class="x-vermelho"> <span class="text-secondary">Pelo menos um número</span> <br>
                <img src="img/correto-verde.png" id="correto-verde" class="x-vermelho"> <span class="text-secondary">Sem caracteres inválidos.</span>
              </div>
              <div class="form-group">
                <label for="confirm-senha">Confirmação de senha</label>
                <input type="password" class="form-control" id="confirm-senha" name="confirm-senha" placeholder="Confirme sua senha"
                disabled="disabled" autocomplete="off" maxlength="15" onkeyup="confirmSenha()">
              </div>
              <div id="cointainer-alerta-confirm" class="alert alert-warning" role="alert" hidden="hidden">
                <img src="img/x-vermelho.png" id="x-vermelho-confirm" class="x-vermelho"> <span class="text-secondary">Senhas devem coincidir</span><br>
              </div> <br>
              <div class="form-group">
                <label for="senha">Senha atual</label>
                <input type="password" class="form-control" id="senha-atual" name="senhaAtual" placeholder="Insira sua senha atual" autocomplete="off" maxlength="15">
              </div> <br>
              <div class="d-grid gap-2">
                <button type="submit" class="btn btn-secondary btn-lg" formaction="SvUsuarioAtualizar" formmethod="post">Atualizar Perfil</button>
              </div>
            </form>
          </div>
        </div>
      </main>

    </body>
</body>
</html>