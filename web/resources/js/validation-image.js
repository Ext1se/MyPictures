function validation_image() {
    let name = form[form.id + ":name"];
    if (name.value === "") {
        name.className = "form-control is-invalid";
        name.focus();
        alert("Введите имя");
        return false;
    } else {
        name.className = "form-control";
    }

    let file = form[form.id + ":file"];
    alert(file.files.length);
    if (file.files.length == 0) {
        alert("Выберите изображение");
        return false;
    }
    return true;
}

