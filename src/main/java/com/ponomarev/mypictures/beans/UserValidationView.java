/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ponomarev.mypictures.beans;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Igorek
 */
@ManagedBean
public class UserValidationView {

    @NotNull(message = "Введите имя")
    @Size(max = 30, message = "Превышена длина")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$", message = "Логин может содержать латинские буквы и цифры, первый символ обязательно буква")
    private String username;

    @NotNull(message = "Введите email")
    @Pattern(regexp = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$", message = "Некорректный email")
    private String email;

    @NotNull(message = "Введите пароль")
    @Pattern(regexp = "^(?=^.{6,}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", message = "Пароль должен содержать минимум 6 символов, включая строчные и прописные латинские буквы и цифры")
    private String password;

    @NotNull(message = "Введите пароль")
    private String passwordRepeat;

    @Size(max = 30, message = "Превышена длина")
    private String firstName;

    @Size(max = 30, message = "Превышена длина")
    private String lastName;

    @Size(max = 400, message = "Превышена длина")
    private String description;

    public UserValidationView() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordRepeat(String password) {
        this.passwordRepeat = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
