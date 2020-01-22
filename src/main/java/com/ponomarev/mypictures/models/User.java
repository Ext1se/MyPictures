package com.ponomarev.mypictures.models;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class User implements Serializable {

    private long id;
    private String username;
    private String email;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
    private String description;
    private byte[] photo;
    private byte[] photoMin;
    private byte[] photoTemp;
    private byte[] photoMinTemp;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getPhotoMin() {
        return photoMin;
    }

    public void setPhotoMin(byte[] photoMin) {
        this.photoMin = photoMin;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public byte[] getPhotoMinTemp() {
        return photoMinTemp;
    }

    public void setPhotoMinTemp(byte[] photoMinTemp) {
        this.photoMinTemp = photoMinTemp;
    }

    public byte[] getPhotoTemp() {
        return photoTemp;
    }

    public void setPhotoTemp(byte[] photoTemp) {
        this.photoTemp = photoTemp;
    }

}
