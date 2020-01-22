package com.ponomarev.mypictures.controllers;


import com.ponomarev.mypictures.beans.Log;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ManagedBean
@SessionScoped
public class LogController implements Serializable {

    private List<Log> logs;

    public LogController() {
    }

    public List<Log> getLogs() {
        List<Log> logs = new ArrayList<Log>();

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String path = context.getRealPath("/") + "/logs" + "/logs.txt";
        System.out.println("logPath = " + path);
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log log = new Log();
                System.out.println(line);
                String[] params = line.split("//");
                System.out.println(Arrays.toString(params));
                log.setStatus(params[0]);
                log.setDate(params[1]);
                log.setUser(params[2]);
                log.setIdUser(Long.parseLong(params[3]));
                log.setMessage(params[4]);
                logs.add(log);
            }
        } catch (IOException e) {
        }

        return logs;
    }

    public static void addLog(String message) {
        PrintWriter printWriter = getWriter();
        String log = "INFO" + "//" + getBody(message);
        printWriter.println(log);
        printWriter.close();
    }

    public static void addLogError(String message) {
        PrintWriter printWriter = getWriter();
        String log = "ERROR"  + "//" + getBody(message);
        printWriter.println(log);
        printWriter.close();
    }

    private static String getBody(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UserController userController = (UserController) request.getSession(false).getAttribute("userController");
        String username = "GUEST";
        long userId = 0;
        if (userController != null && userController.getUser() != null && userController.getUser().getId() > 0) {
            username = userController.getUser().getUsername();
            userId = userController.getUser().getId();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = dateFormat.format(new Date());
        PrintWriter printWriter = getWriter();
        String body = date + "//" + username + "//" + userId + "//" + message;
        return body;
    }

    private static PrintWriter getWriter() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String path = context.getRealPath("/") + "/logs" + "/logs.txt";
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(path, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return printWriter;
    }
}
