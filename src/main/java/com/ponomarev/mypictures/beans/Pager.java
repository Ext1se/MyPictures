package com.ponomarev.mypictures.beans;

import com.ponomarev.mypictures.models.Image;
import java.util.List;
import javax.faces.bean.SessionScoped;

@SessionScoped
public class Pager {

    private int totalCount;
    private List<Image> images;
    private List<Long> favoriteLinks;
    private long idUser = 0;
    private String search = null;
    private boolean showFavoriteImages = false;
    private int from;
    private int to;

    public Pager() {
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        System.out.println("size=" + images.size());
        this.images = images;
    }

    public List<Long> getFavoriteLinks() {
        return favoriteLinks;
    }

    public void setFavoriteLinks(List<Long> favoriteLinks) {
        this.favoriteLinks = favoriteLinks;
    }

    public void checkLike(Image image) {
        if (favoriteLinks.contains(image.getId())) {
            image.setIsFavorite(true);
        } else {
            image.setIsFavorite(false);
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public boolean isShowFavoriteImages() {
        return showFavoriteImages;
    }

    public void setShowFavoriteImages(boolean showFavoriteImages) {
        this.showFavoriteImages = showFavoriteImages;
    }

}
