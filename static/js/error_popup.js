// Script para controlar o popup de erro
document.addEventListener('DOMContentLoaded', function() {
    const popup = document.getElementById('errorPopup');
    const closeButton = document.getElementById('closePopup');

    if (closeButton && popup) {
        closeButton.addEventListener('click', function() {
            popup.classList.add('hidden');
        });

        // Fechar ao clicar fora do popup
        popup.addEventListener('click', function(e) {
            if (e.target === popup) {
                popup.classList.add('hidden');
            }
        });

        // Fechar com a tecla ESC
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && !popup.classList.contains('hidden')) {
                popup.classList.add('hidden');
            }
        });
    }
});

