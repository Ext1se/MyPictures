/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ponomarev.mypictures.controllers;

import com.ponomarev.mypictures.models.User;
import com.ponomarev.mypictures.repositories.UserRepository;
import com.ponomarev.mypictures.utils.Navigation;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(eager = true)
@SessionScoped
public class UserController {

    private User user;
    private UserRepository repository;

    private String message;

    public UserController() {
        LogController.addLog("Constructor - UserController");
        repository = new UserRepository();
        user = new User();
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void clearPhotoTemp() {
        user.setPhotoMinTemp(user.getPhotoMin());
        user.setPhotoTemp(user.getPhoto());
    }

    public String login() {
        try {
            LogController.addLog("UserController - login");
            HttpServletRequest request = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
            ImageController imageController = (ImageController) request.getSession(false).getAttribute("imageController");
            request.getSession().removeAttribute("imageController");
            if (request.getSession(false) != null) {
                request.logout();
            }
            request.login(user.getUsername(), user.getPassword());
            user = repository.getUser(user.getUsername());
            return Navigation.IMAGES_GENERAL;
        } catch (ServletException e) {
            LogController.addLogError("UserController - login - error data");
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
            sendMessage("Неверный логин или пароль");
        }
        return Navigation.SIGNIN;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            LogController.addLog("UserController - logout");
            request.logout();
        } catch (ServletException e) {
            LogController.addLogError("UserController - logout");
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println("logout1");
        user = new User();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return Navigation.SIGNIN;
    }

    public String validateUser() {
        LogController.addLog("UserController - validateUser ");
        if (!user.getPassword().equals(user.getPasswordRepeat())) {
            LogController.addLogError("UserController - validateUser - different password");
            sendMessage("Пароли не совпадают");
            return Navigation.SIGNUP;
        }
        boolean isExist = repository.checkUsernameExist(user.getUsername());
        if (isExist) {
            LogController.addLogError("UserController - validateUser - different password");
            sendMessage("Пользователь с таким именем уже зарегистрирован");
            return Navigation.SIGNUP;
        } else {
            repository.addUser(user);
            login();
            return Navigation.IMAGES_GENERAL;
        }
    }
    
    public String updateUser()
    {
        LogController.addLog("UserController - updateUser");
        user.setPhotoMin(user.getPhotoMinTemp());
        user.setPhotoMinTemp(null);
        user.setPhoto(user.getPhotoTemp());
        user.setPhotoTemp(null);
        repository.updateUser(user);
        return Navigation.MY_IMAGES;
    }

    private void sendMessage(String m) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(m);
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage("messages", message);
    }
}
