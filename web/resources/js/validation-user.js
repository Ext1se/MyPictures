function validation_user() {
    let name = form[form.id + ":username"];
    if (name.value === "") {
        name.className = "form-control is-invalid";
        name.focus();
        alert("Введите имя пользователя");
        return false;
    } else {
        name.className = "form-control";
    }

    if (!/^[a-zA-Z][a-zA-Z0-9-_\.]{1,20}$/.test(name.value))
    {
        name.className = "form-control is-invalid";
        name.focus();
        alert("Логин может содержать латинские буквы и цифры, первый символ обязательно буква");
        return false;
    } else {
        name.className = "form-control";
    }

    //////////////////////////////////////////////////////////////////

    let email = form[form.id + ":email"];
    if (email.value === "") {
        email.className = "form-control is-invalid";
        email.focus();
        alert("Введите email");
        return false;
    } else {
        email.className = "form-control";
    }

    if (!/^[-\w.]+@([A-z0-9][-A-z0-9]+\.)+[A-z]{2,4}$/.test(email.value))
    {
        email.className = "form-control is-invalid";
        email.focus();
        alert("Некорректный email");
        return false;
    } else {
        email.className = "form-control";
    }

    //////////////////////////////////////////////////////////////////

    let password_1 = form[form.id + ":password_1"];
    if (password_1.value === "") {
        password_1.className = "form-control is-invalid";
        password_1.focus();
        alert("Введите пароль");
        return false;
    } else {
        password_1.className = "form-control";
    }

    if (!/^(?=^.{6,}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$/.test(password_1.value))
    {
        password_1.className = "form-control is-invalid";
        password_1.focus();
        alert("Пароль должен содержать минимум 6 символов, включая строчные и прописные латинские буквы и цифры");
        return false;
    } else {
        password_1.className = "form-control";
    }
    
    //////////////////////////////////////////////////////////////////
    
    let password_2 = form[form.id + ":password_2"];
    if (password_2.value !== password_1.value) {
        password_2.className = "form-control is-invalid";
        password_2.focus();
        alert("Пароли не совпадают");
        return false;
    } else {
        password_2.className = "form-control";
    }

    return true;
}



