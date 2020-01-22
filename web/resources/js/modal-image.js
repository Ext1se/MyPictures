function showImageModal () {
    var modal = document.getElementById('modalImage');
    modal.style.display = "block";
}

function closeImageModal () {    
    var modal = document.getElementById('modalImage');
    modal.style.display = "none";
}

window.onclick = function (event) {
    var modal = document.getElementById('modalImage');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}