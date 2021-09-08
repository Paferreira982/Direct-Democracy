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
    <title>LOGIN | DIRECT DEMOCRACY</title>
</head>
<body>
	<%
	Usuario firstUser = new Usuario();
	firstUser.rotinaBanco(); // Cria um usuário padrão no sistema (Caso não tenha)
	
	String msg = (String) request.getAttribute("msg");
	String cpf = (String) request.getAttribute("CpfSended");
	
	request.getSession().invalidate();
	
	%>
	<header>
        <nav class="navbar navbar-dark bg-dark navbar-expand-lg">
            <a class="navbar-brand" href="#">
              <img src="img/logo.png" width="30" height="30" class="d-inline-block align-top margin-left" alt="logotipo" id="logo">
              &nbsp;Direct Democracy |
            </a>
            <div class="collapse navbar-collapse margin-left-low" id="navbarNav">
                <ul class="navbar-nav">
                  <li class="nav-item">
                    <a class="nav-link disabled" href="#">Home</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link active" href="#">Login</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="tela_cadastro.jsp">Cadastre-se</a>
                  </li>
                </ul>
              </div>
        </nav>
    </header>
    <main style="height: 88.75vh;">
        <div id="container-login" class="card">
            <div class="card-body">
            <%
            if (msg != null)
            	out.println("<div class='alert alert-danger' role='alert'>"+msg+"</div>");
            %>
                <form>
                    <div class="mb-3">
                        <label for="input-cpf" class="form-label">CPF</label>
                        <input type="text" class="form-control" id="input-cpf" placeholder="Insira seu cpf" value="<%= cpf == null?"":cpf %>"
                         required="required" name="cpf" onkeyup="tratarCPF();" maxlength="14" autocomplete="off">
                    </div>
                    <div class="mb-3">
                        <label for="input-senha" class="form-label">Senha</label>
                        <input type="password" class="form-control" id="input-senha" placeholder="Insira sua senha" 
                        name="senha" required="required" maxlength="15">
                    </div>
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary" formaction="SvLogin" formmethod="post">Fazer Login</button> <br>
                    </div>
                    <div class="dropdown-divider"></div>
                    <p class="text-secondary">
                        Novo, por aqui? <a href="tela_cadastro.jsp" class="text-muted">Registre-se.</a>
                    </p>
                </form>
            </div>
        </div>
    </main>
    
	<footer class="bg-dark">
	</footer>
</body>
</html>