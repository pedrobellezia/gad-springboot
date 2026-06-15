var check = function () {
    const senha = document.getElementById('nova_senha').value;
    const repetir = document.getElementById('repetir_senha').value;
    const message = document.getElementById('message');
    const submitBtn = document.getElementById('submit-btn');

    if (!senha && !repetir) {
        message.className = '';
        message.textContent = '';
        submitBtn.disabled = true;
        return;
    }

    if (senha === repetir && senha.length > 0) {
        message.className = 'show success';
        message.textContent = 'Senhas coincidem';
        submitBtn.disabled = false;
    } else {
        message.className = 'show error';
        message.textContent = 'Senhas não coincidem';
        submitBtn.disabled = true;
    }
};

document.getElementById('nova_senha').addEventListener('input', check);
document.getElementById('repetir_senha').addEventListener('input', check);