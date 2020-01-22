package com.ponomarev.mypictures.beans;

import com.ponomarev.mypictures.controllers.ImageController;
import com.ponomarev.mypictures.controllers.UserController;
import com.ponomarev.mypictures.models.Image;
import com.ponomarev.mypictures.models.User;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import net.coobird.thumbnailator.Thumbnails;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
public class FileUploader {

    private float ratio = 1.5f;
    private int minWidth = 256;
    private int minHeight = 384;

    public void upload(FileUploadEvent event) {
        try {
            BufferedImage imageBig = ImageIO.read(event.getFile().getInputstream());
            //BufferedImage imageSmall = resizeImage(imageBig);
            BufferedImage imageSmall = resizeImageCenter(imageBig, ratio);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            ImageController imageController = (ImageController) request.getSession(false).getAttribute("imageController");
            Image image = imageController.getImage();

            String filename = event.getFile().getFileName();
            String formatName = filename.substring(filename.lastIndexOf(".") + 1);

            ByteArrayOutputStream baosBig = new ByteArrayOutputStream();
            ImageIO.write(imageBig, formatName, baosBig);
            image.setData(baosBig.toByteArray());

            ByteArrayOutputStream baosSmall = new ByteArrayOutputStream();
            ImageIO.write(imageSmall, formatName, baosSmall);
            image.setDataMin(baosSmall.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FileUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void uploadUserPhoto(FileUploadEvent event) {
        if (event == null) {
            return;
        }
        try {
            BufferedImage imageBig = ImageIO.read(event.getFile().getInputstream());
            BufferedImage imageSmall = resizeImageCenter(imageBig, 1);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            UserController userController = (UserController) request.getSession(false).getAttribute("userController");
            User user = userController.getUser();

            String filename = event.getFile().getFileName();
            String formatName = filename.substring(filename.lastIndexOf(".") + 1);

            ByteArrayOutputStream baosBig = new ByteArrayOutputStream();
            ImageIO.write(imageBig, formatName, baosBig);
            user.setPhotoTemp(baosBig.toByteArray());

            ByteArrayOutputStream baosSmall = new ByteArrayOutputStream();
            ImageIO.write(imageSmall, formatName, baosSmall);
            user.setPhotoMinTemp(baosSmall.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FileUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedImage resizeImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float k = width / minWidth;
        int scaledWidth = minWidth;
        int scaledHeight = (int) (height / k);
        try {
            return Thumbnails.of(image).size(scaledWidth, scaledHeight).asBufferedImage();
        } catch (IOException ex) {
            Logger.getLogger(FileUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private BufferedImage resizeImageCenter(BufferedImage image, float ratio) {
        float width = image.getWidth();
        float height = image.getHeight();
        float requiredWidth = height / ratio;
        if (requiredWidth > width) {
            requiredWidth = width;
            height = requiredWidth * ratio;
        }
        float start = width / 2 - requiredWidth / 2;
        BufferedImage centerImage = image.getSubimage((int) start, 0, (int) requiredWidth, (int) height);
        height = (height > minHeight) ? height : minHeight;
        float k = height / minHeight;
        int scaledHeight = minHeight;
        int scaledWidth = (int) (minWidth * k);
        try {
            return Thumbnails.of(centerImage).size(scaledWidth, scaledHeight).asBufferedImage();
        } catch (IOException ex) {
            Logger.getLogger(FileUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
