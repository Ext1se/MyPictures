package com.ponomarev.mypictures.controllers;

import com.ponomarev.mypictures.beans.ImagesDataModel;
import com.ponomarev.mypictures.beans.Pager;
import com.ponomarev.mypictures.models.Image;
import com.ponomarev.mypictures.repositories.ImageRepository;
import com.ponomarev.mypictures.utils.Navigation;
import org.primefaces.model.LazyDataModel;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(eager = true)
@SessionScoped
public class ImageController {

    private long idUser = 0;
    private boolean hasNext = false;
    private boolean hasPrevious = false;
    private String search;

    private Image image;
    private ImageRepository repository;
    private LazyDataModel<Image> imagesDataModel;
    private Pager pager;

    public ImageController() {
        LogController.addLog("Constructor ImageController");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UserController userController = (UserController) request.getSession(false).getAttribute("userController");
        if (userController != null && userController.getUser() != null) {
            idUser = userController.getUser().getId();
        }
        image = new Image();
        pager = new Pager();
        repository = new ImageRepository(pager, idUser);
        imagesDataModel = new ImagesDataModel(pager, repository);

    }

    public void updateImages() {
        pager.setShowFavoriteImages(false);
        pager.setSearch(null);
        pager.setIdUser(0);
    }

    public void updateImagesUser() {
        pager.setShowFavoriteImages(false);
        pager.setSearch(null);
        pager.setIdUser(idUser);
    }

    public void updateFavoriteImages() {
        pager.setFavoriteLinks(repository.getFavoriteImageLinks(idUser));
        pager.setShowFavoriteImages(true);
        pager.setSearch(null);
        pager.setIdUser(idUser);
    }

    public void searchImages() {
        LogController.addLog("ImageController - Search Images");
        pager.setSearch(search);
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public Image getImage() {
        return image;
    }

    public LazyDataModel<Image> getImagesDataModel() {
        return imagesDataModel;
    }

    public Pager getPager() {
        return pager;
    }

    public void clearImage() {
        image = new Image();
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImageById(long id) {
        //LogController.addLog("ImageController - getImageById (" + id + ")");
        if (imagesDataModel == null) {
            return null;
        }
        return imagesDataModel.getWrappedData().stream()
                .filter(image -> id == image.getId())
                .findFirst()
                .get();
    }


    public void likeImage(long idImage) {
        LogController.addLog("ImageController - likeImage (" + idImage+ ")");
        repository.setLike(idUser, idImage);
        Image image = getImageById(idImage);
        image.setIsFavorite(true);
        image.setLikes(image.getLikes() + 1);
        pager.getFavoriteLinks().add(idImage);
    }

    public void dislikeImage(long idImage) {
        LogController.addLog("ImageController - dislikeImage (" + idImage+ ")");
        repository.setDislike(idUser, idImage);
        Image image = getImageById(idImage);
        image.setIsFavorite(false);
        image.setLikes(image.getLikes() - 1);
        pager.getFavoriteLinks().remove(idImage);
    }

    public void addView(long idImage) {
        LogController.addLog("ImageController - addView (" + idImage+ ")");
        repository.addView(idImage);
        Image image = getImageById(idImage);
        image.setViews(image.getViews() + 1);
        setImage(image);
        System.out.println("view");
    }

    public byte[] getImageData(long idImage) {
        return repository.getImageData(idImage);
    }

    public boolean isHasNext() {
        if (image == null || imagesDataModel == null || imagesDataModel.getWrappedData().size() == 0) {
            return false;
        }
        int position = imagesDataModel.getWrappedData().indexOf(image);
        if (position == imagesDataModel.getWrappedData().size() - 1) {
            hasNext = false;
        } else {
            hasNext = true;
        }
        return hasNext;
    }

    public boolean isHasPrevious() {
        if (image == null || imagesDataModel == null || imagesDataModel.getWrappedData().size() == 0) {
            return false;
        }
        int position = imagesDataModel.getWrappedData().indexOf(image);
        if (position == 0) {
            hasPrevious = false;
        } else {
            hasPrevious = true;
        }
        return hasPrevious;
    }

    public void getNextImage() {
        System.out.println("getNextImage 1");
        int position = imagesDataModel.getWrappedData().indexOf(image);
        image = imagesDataModel.getWrappedData().get(position + 1);
        setImage(image);
        System.out.println("getNextImage 2");
    }

    public void getPreviousImage() {
        System.out.println("getPreviousImage 1");
        int position = imagesDataModel.getWrappedData().indexOf(image);
        image = imagesDataModel.getWrappedData().get(position - 1);
        setImage(image);
        System.out.println("getPreviousImage 2");
    }

    public String addImage() {
        LogController.addLog("ImageController - addImage");
        if (image.getData() == null) {
            //LogController.addLogError("ImageController - addImage - image is empty");
            sendMessage("Выберите изображение");
            return Navigation.ADD_IMAGE;
        }
        repository.addImage(idUser, image);
        return Navigation.MY_IMAGES;
    }

    public String editImage(long idImage) {
        LogController.addLog("ImageController - editImage(" + idImage  +")");
        Image image = getImageById(idImage);
        setImage(image);
        return Navigation.EDIT_IMAGE;
    }

    public String updateImage() {
        LogController.addLog("ImageController - editImage(" + image.getId()  +")");
        repository.updateImage(image.getId(), image.getName(), image.getDescription());
        return Navigation.MY_IMAGES;
    }

    public void deleteImage(long idImage) {
        LogController.addLog("ImageController - deleteImage(" + idImage  +")");
        repository.deleteImage(idImage);
    }

    private void sendMessage(String m) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(m);
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage("messages", message);
    }
}
