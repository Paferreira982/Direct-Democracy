function tratarCPF(){
	let cpf = document.getElementById('input-cpf');
    let regex = "0123456789"
    let string = cpf.value;
    const tecla = (window.event) ? event.keyCode : event.wich;  

    if (tecla != 8) {

        for (let i = 0; i < string.length; i++) {
            let verificador = true;
            if (regex.includes(string.charAt(i)))
                verificador = false;
            
            if (verificador) {
                if (!((string.charAt(3) == "." && i == 3) || (string.charAt(7) == "." && i == 7) || (string.charAt(11) == "-" && i == 11))) {
                    let aux = string;
                    string = aux.slice(0,i) + aux.slice(i+1, aux.length);
                    i--;
                }
            }
        }

        if (string.length == 14 && (!string.includes(".") || !string.includes("-"))) {
            let aux = string;
            string = aux.slice(0,3) + "." + aux.slice(4,7) + "." + aux.slice(8,11) + "-" + aux.slice(12)
        } else {
            if (string.length == 11 && !string.includes(".") && !string.includes("-")){
                let aux = string;
                string = aux.slice(0,3) + "." + aux.slice(3,6) + "." + aux.slice(6,9) + "-" + aux.slice(9, 11)
            } else {
                if (string.length == 3 || string.length == 7)
                string += ".";
        
                if (string.length == 11)
                    string += "-";
    
                if (string.length == 4 && string.charAt(3) != ".") {
                    let aux = string;
                    string = aux.slice(0,3) + "." + aux.slice(3,4)
                }
    
                if (string.length == 8 && string.charAt(7) != ".") {
                    let aux = string;
                    string = aux.slice(0,7) + "." + aux.slice(7,8)
                }
    
                if (string.length == 12 && string.charAt(11) != "-") {
                    let aux = string;
                    string = aux.slice(0,11) + "-" + aux.slice(11,12)
                }
            }
        }

    }
    cpf.value = string;
}

function verificarSenha() {
    let senha = document.getElementById('senha');
    let string = senha.value;

    let verificadorNumero = temNumero(senha.value);
    let verificadorLetra = temLetra(senha.value);
    let verificador = true;

    let regex = "!@#$%¨&*()_+-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'";

    for (let i = 0; i < string.length; i++) {
        if (regex.includes(string[i]))
            verificador = false;
    }

    if ((string.length > 6 && string.length < 15))
        document.getElementById('x-vermelho-tamanho').src = "img/correto-verde.png";
    else
        document.getElementById('x-vermelho-tamanho').src = "img/x-vermelho.png";

    if (verificadorLetra)
        document.getElementById('x-vermelho-letra').src = "img/correto-verde.png";
    else
        document.getElementById('x-vermelho-letra').src = "img/x-vermelho.png";

    if (verificadorNumero)
        document.getElementById('x-vermelho-numero').src = "img/correto-verde.png";
    else
        document.getElementById('x-vermelho-numero').src = "img/x-vermelho.png";

    if (!verificador)
        document.getElementById('correto-verde').src = "img/x-vermelho.png";
    else
        document.getElementById('correto-verde').src = "img/correto-verde.png";

    if (verificadorNumero && verificadorLetra && (string.length > 6 && string.length < 15) && verificador) {
        document.getElementById('confirm-senha').removeAttribute('disabled');
        document.getElementById('cointainer-alerta').setAttribute('hidden','hidden');
        document.getElementById('cointainer-alerta-confirm').removeAttribute('hidden');

        senha.removeAttribute("class");
        senha.setAttribute("class", "form-control is-valid");
    } else {
        document.getElementById('cointainer-alerta-confirm').setAttribute('hidden','hidden');
        document.getElementById('cointainer-alerta').removeAttribute('hidden');
        document.getElementById('confirm-senha').setAttribute('disabled', 'disabled');
        document.getElementById('confirm-senha').value = "";

        senha.removeAttribute("class");
        senha.setAttribute("class", "form-control is-invalid");
    }   
}

function temNumero(string) {
    let numeros = "123456789";
    for (let i = 0; i < string.length; i++) {
        if (numeros.includes(string[i]))
            return true;
    }
    return false;
}

function temLetra(string) {
    let regex = "!@#$%¨&*()_+-=´[]~;/.,<>:?}^`{/*-+.°ºª§-'";
    let numeros = "123456789";

    for (let i = 0; i < string.length; i++) {
        if (!regex.includes(string[i]) && !numeros.includes(string[i]))
            return true;
    }
    return false;
}