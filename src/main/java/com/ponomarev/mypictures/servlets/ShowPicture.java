/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ponomarev.mypictures.servlets;

import com.ponomarev.mypictures.controllers.ImageController;
import com.ponomarev.mypictures.controllers.UserController;
import com.ponomarev.mypictures.models.Owner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "ShowPicture", urlPatterns = {"/ShowPicture"})
public class ShowPicture extends HttpServlet {

    public static final String IMAGE_MAX = "IMAGE_MAX";
    public static final String IMAGE_MIN = "IMAGE_MIN";
    public static final String IMAGE_PREVIEW = "IMAGE_PREVIEW";
    public static final String OWNER = "OWNER";
    public static final String USER_MAX = "USER_MAX";
    public static final String USER_MIN = "USER_MIN";

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte data[] = getData(request);
        if (data == null) {
            return;
        }
        response.setContentType("image/jpeg");
        OutputStream out = response.getOutputStream();
        try {
            response.setContentLength(data.length);
            out.write(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }

    private byte[] getData(HttpServletRequest request) {
        long id = 0;
        if (request.getParameter("id") != null) {
            id = Long.valueOf(request.getParameter("id"));
        }
        String image = request.getParameter("image");
        image = image.toUpperCase();
        ImageController imageController = (ImageController) request.getSession(false).getAttribute("imageController");
        UserController userController = (UserController) request.getSession(false).getAttribute("userController");
        switch (image) {
            case (IMAGE_MAX):
                return imageController.getImageData(id);
            case (IMAGE_MIN):
                return imageController.getImageById(id).getDataMin();
            case (IMAGE_PREVIEW):
                return imageController.getImage().getData();
            case (OWNER):
                Owner owner = imageController.getImageById(id).getOwner();
                if (owner == null) {
                    return null;
                }
                return owner.getPhotoMin();
            case (USER_MAX):
                return userController.getUser().getPhotoTemp();
            case (USER_MIN):
                return userController.getUser().getPhotoMin();
            default:
                return null;
        }
    }


// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
