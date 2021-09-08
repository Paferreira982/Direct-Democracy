function validatePrazo() {
    let prazo = document.getElementById("prazo");
    if (prazo == null || prazo.value == "") {
        document.getElementById("prazo-feedback").innerHTML = "Por favor, informe um prazo.";
        prazo.removeAttribute("class");
        prazo.setAttribute("class", "form-control is-invalid");
        return;
    }

    document.getElementById("prazo-feedback").innerHTML = "";
    prazo.removeAttribute("class");
    prazo.setAttribute("class", "form-control is-valid");
}

function validateDesc() {
    let descricao = document.getElementById('descricao');

    if (descricao.value.length > 500 || descricao.value.length < 10) {
        document.getElementById("descricao-feedback").innerHTML = "A descrição deve ter entre 10 e 500 caracteres.";
        descricao.removeAttribute("class");
        descricao.setAttribute("class", "form-control is-invalid");
        return;
    }

    document.getElementById("descricao-feedback").innerHTML = "";
    descricao.removeAttribute("class");
    descricao.setAttribute("class", "form-control is-valid");
}

function validateTitulo() {
    let titulo = document.getElementById('titulo');

    if (titulo.value.length > 40 || titulo.value.length < 5) {
        document.getElementById("titulo-feedback").innerHTML = "O titulo deve ter entre 5 e 40 caracteres.";
        titulo.removeAttribute("class");
        titulo.setAttribute("class", "form-control is-invalid");
        return;
    }

    let regex = "!@#$%¨&*()_+1234567890-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'";
    
    for (let i = 0; i < titulo.value.length; i++) {
        if (regex.includes(titulo.value.charAt(i))) {
            document.getElementById("titulo-feedback").innerHTML = "Por favor, informe um titulo válido";
            titulo.removeAttribute("class");
            titulo.setAttribute("class", "form-control is-invalid");
            return;
        }
    }

    document.getElementById("titulo-feedback").innerHTML = "";
    titulo.removeAttribute("class");
    titulo.setAttribute("class", "form-control is-valid");

}

function validateNome() {
    let nome = document.getElementById('nome');
    
    if (nome.value.length > 40 || nome.value.length < 5) {
        document.getElementById("nome-feedback").innerHTML = "O nome deve ter entre 5 e 40 caracteres."
        nome.removeAttribute("class");
        nome.setAttribute("class", "form-control is-invalid")
        return;
    }
    
    if (nome.value.split(" ").length == 1) {
        document.getElementById("nome-feedback").innerHTML = "Por favor, informe seu nome completo."
        nome.removeAttribute("class");
        nome.setAttribute("class", "form-control is-invalid")
        return;
    }
    
    let regex = "!@#$%¨&*()_+1234567890-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'";
    
    for (let i = 0; i < nome.value.length; i++) {
        if (regex.includes(nome.value.charAt(i))) {
            document.getElementById("nome-feedback").innerHTML = "Por favor, informe um nome válido"
            nome.removeAttribute("class");
            nome.setAttribute("class", "form-control is-invalid")
            return;
        }
    }
    
    document.getElementById("nome-feedback").innerHTML = "";
    nome.removeAttribute("class");
    nome.setAttribute("class", "form-control is-valid");
    
}

function validateData() {
    let data = document.getElementById('dataNascimento');
    let hoje = new Date();
    let nascimento = new Date(data.value.split("/"));
    let ano = hoje.getFullYear() - nascimento.getFullYear();
    let m = hoje.getMonth() - nascimento.getMonth();

    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) {
        ano--;
    }

    if (ano < 16) {
        document.getElementById("dataNascimento-feedback").innerHTML = "Somente usuários maiores de 16 anos são permitidos."
        data.removeAttribute("class");
        data.setAttribute("class", "form-control is-invalid")
        return;
    }

    document.getElementById("dataNascimento-feedback").innerHTML = ""
    data.removeAttribute("class");
    data.setAttribute("class", "form-control is-valid")

}

function validateSex() {
    let sexo = document.getElementById('sexo');

    if (sexo.value == "Selecione") {
        document.getElementById("sexo-feedback").innerHTML = "Por favor, informe seu sexo."
        sexo.removeAttribute("class");
        sexo.setAttribute("class", "form-control is-invalid")
        return;
    }

    document.getElementById("sexo-feedback").innerHTML = ""
    sexo.removeAttribute("class");
    sexo.setAttribute("class", "form-control is-valid")
}

function validateCPF() {
    let cpf = document.getElementById('input-cpf');

    if (cpf.value.length != 14 || !cpf.value.includes(".") || !cpf.value.includes("-")) {
        document.getElementById("cpf-feedback").innerHTML = "Por favor, informe um CPF válido."
        cpf.removeAttribute("class");
        cpf.setAttribute("class", "form-control is-invalid")
        return;
    }

    document.getElementById("cpf-feedback").innerHTML = ""
    cpf.removeAttribute("class");
    cpf.setAttribute("class", "form-control is-valid")

}

function confirmSenha() {
    let senha = document.getElementById('senha').value;
    let senhaConfirm = document.getElementById('confirm-senha');

    if (senha == senhaConfirm.value) {
        document.getElementById('cointainer-alerta-confirm').removeAttribute('class');
        document.getElementById('cointainer-alerta-confirm').setAttribute('class','alert alert-success');
        document.getElementById('x-vermelho-confirm').src = "img/correto-verde.png";

        document.getElementById('cointainer-alerta-confirm').setAttribute('hidden', 'hidden');
        
        senhaConfirm.removeAttribute("class");
        senhaConfirm.setAttribute("class", "form-control is-valid");
    } else {
        document.getElementById('cointainer-alerta-confirm').removeAttribute('hidden');

        senhaConfirm.removeAttribute("class");
        senhaConfirm.setAttribute("class", "form-control is-invalid");
    }
}

function adjustMaxAndMinOfDate() {
    document.getElementById("dataNascimento").setAttribute("max", getToday());
    document.getElementById("dataNascimento").setAttribute("min", "1900-01-01");
}

function getDataAtual() {
    document.getElementById("prazo").setAttribute("min", getToday());
}

function getToday() {
    let hoje = new Date();
    let dd = hoje.getDate();
    let mm = hoje.getMonth()+1;
    let yyyy = hoje.getFullYear();
    if(dd<10){
            dd='0'+dd
        } 
        if(mm<10){
            mm='0'+mm
        } 

    hoje = yyyy+'-'+mm+'-'+dd;
    return hoje;
}

function updateAllInputs() {
    updateApprovalRate();
    updateDateString();
    document.getElementById("total-votos").value = totalVotos;
    document.getElementById("initial-date").setAttribute("value", initialDate);
    document.getElementById("prazo").setAttribute("value", prazoProjeto);
}

function updateApprovalRate() {
    document.getElementById("project-approval").setAttribute("style", "width: " + projectApproval + "%");
    document.getElementById("project-approval").innerHTML = projectApproval + "%";

    let votosPositivos = Math.round(totalVotos * (projectApproval/100));
    let votosNegativos = Math.round(totalVotos * (100 - projectApproval)/100);

    document.getElementById("positivos").innerHTML = "Positivos: " + votosPositivos;
    document.getElementById("negativos").innerHTML = "Negativos: " + votosNegativos;
}

function updateDateString() {
    let dataInicial = new Date(); 
    let prazo = new Date(prazoProjeto);


    if (dataInicial > prazo) {
        document.getElementById("tempo-restante").value = "Projeto concluído";
    } else {
        let difference= Math.abs(prazo-dataInicial);

        if (difference%(1000 * 3600 * 24) != 0) {
            let dif = difference/(1000 * 3600 * 24);
            let dias = 0;
            let horas = 0;
    
            while (dif > 1) {
                dif -= 1;
                dias += 1;
            }
    
            horas = Math.round(dif * 24);
    
            document.getElementById("tempo-restante").value = dias + " dias e " + horas + " horas";
    
        } else {
            document.getElementById("tempo-restante").value = difference/(1000 * 3600 * 24) + " dias";
        }
    }
}

function controlRequired() {
    let senha = document.getElementById("senha");
    let confirmSenha = document.getElementById("confirm-senha");

    if (senha.value.length > 0) {
        senha.setAttribute("required");
        confirmSenha.setAttribute("required");
    } else {
        senha.removeAttribute("required");
        confirmSenha.removeAttribute("required");
    }
}