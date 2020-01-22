package com.ponomarev.mypictures.repositories;

import com.ponomarev.mypictures.beans.Pager;
import com.ponomarev.mypictures.controllers.LogController;
import com.ponomarev.mypictures.models.Image;
import com.ponomarev.mypictures.models.Owner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Igorek
 */
public class ImageRepository extends BaseRepository {

    private Pager pager;

    public ImageRepository() {
        super("queries");
    }

    public ImageRepository(Pager pager, long idUser) {
        super("queries");
        this.pager = pager;
        getFavoriteImageLinks(idUser);
        updatePage();
    }

    public List<Long> getFavoriteImageLinks(long idUser) {
        //LogController.addLog("ImageRepository - getFavoriteImageLinks(" + idUser +")");
        List<Long> links = new ArrayList<Long>();
        try {
            String query = new String().format(bundle.getString("get_favorite_links"), idUser);
            executeWithResult(query);
            while (resultSet.next()) {
                links.add(resultSet.getLong("id_image"));
            }
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - getFavoriteImageLinks(" + idUser +")");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        pager.setFavoriteLinks(links);
        return links;
    }

    public void setLike(long idUser, long idImage) {
        //LogController.addLog("ImageRepository - setLike(" + idUser +", " + idImage + ")");
        try {
            String query1 = new String().format(bundle.getString("add_like"), idImage);
            execute(query1);
            closeConnection();
            String query2 = new String().format(bundle.getString("add_user_image"), idUser, idImage);
            execute(query2);
            closeConnection();
        } finally {
            closeConnection();
        }
    }

    public void setDislike(long idUser, long idImage) {
        //LogController.addLog("ImageRepository - setDislike(" + idUser +", " + idImage + ")");
        try {
            String query1 = new String().format(bundle.getString("add_dislike"), idImage);
            execute(query1);
            closeConnection();
            String query2 = new String().format(bundle.getString("delete_user_image"), idUser, idImage);
            execute(query2);
            closeConnection();
        } finally {
            closeConnection();
        }
    }

    public void addView(long idImage) {
        try {
            //LogController.addLog("ImageRepository - addView(" + idImage +", " + idImage + ")");
            String query = new String().format(bundle.getString("add_view"), idImage);
            execute(query);
        } finally {
            closeConnection();
        }
    }

    public byte[] getImageData(long idImage) {
        //LogController.addLog("ImageRepository - getImageData(" + idImage + ")");
        byte[] data = null;
        try {
            String query = new String().format(bundle.getString("get_image_data"), idImage);
            executeWithResult(query);
            while (resultSet.next()) {
                data = resultSet.getBytes("data");
            }
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - getImageData(" + idImage + ")");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return data;
    }

    public void addImage(long idUser, Image image) {
        //LogController.addLog("ImageRepository - addImage");
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(bundle.getString("add_image"));
            preparedStatement.setLong(1, idUser);
            preparedStatement.setString(2, image.getName());
            preparedStatement.setString(3, image.getDescription());
            preparedStatement.setBytes(4, image.getData());
            preparedStatement.setBytes(5, image.getDataMin());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - addImage");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public void updateImage(long idImage, String name, String description) {
        //LogController.addLog("ImageRepository - updateImage(" + idImage + ")");
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(bundle.getString("update_image"));
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setLong(3, idImage);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - updateImage(" + idImage + ")");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public void deleteImage(long idImage) {
        //LogController.addLog("ImageRepository - deleteImage(" + idImage + ")");
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(bundle.getString("delete_image"));
            preparedStatement.setLong(1, idImage);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - deleteImage(" + idImage + ")");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    ///////////----------------///////////////////
    public void updatePage() {
        if (pager.getSearch() != null) {
            updateTotalCountSearch(pager.getSearch());
            updatePageImagesSearch(pager.getSearch());
            return;
        }
        if (pager.getIdUser() != 0 && pager.isShowFavoriteImages()) {
            updateTotalCountFavorites(pager.getIdUser());
            updatePageImagesFavorites(pager.getIdUser());
            return;
        }

        if (pager.getIdUser() != 0) {
            updateTotalCountSearchUser(pager.getIdUser());
            updatePageImagesSearchUser(pager.getIdUser());
            return;
        }

        updateTotalCount();
        updatePageImages();
    }

    ///////////----------------///////////////////
    private void updatePageImages() {
        System.out.println("repository updatePageImages");
        List<Image> images = new ArrayList<Image>();
        try {
            String query = bundle.getString("get_images");
            query = query + " LIMIT " + pager.getFrom() + ", " + pager.getTo();
            executeWithResult(query);
            while (resultSet.next()) {
                Image image = getImage(resultSet);
                pager.checkLike(image);
                images.add(image);
            }
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - updatePageImages");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        pager.setImages(images);
    }

    public void updatePageImagesSearch(String search) {
        search = "%" + search + "%";
        System.out.println("updatePageImageSearch");
        List<Image> images = new ArrayList<Image>();
        try {
            String query = bundle.getString("search_images");
            query = query + " LIMIT " + pager.getFrom() + ", " + pager.getTo();
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, search);
            preparedStatement.setString(2, search);
            preparedStatement.setString(3, search);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Image image = getImage(resultSet);
                pager.checkLike(image);
                images.add(image);
            }
            preparedStatement.close();
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - updatePageImagesSearch");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        pager.setImages(images);
    }

    private void updatePageImagesSearchUser(long idUser) {
        List<Image> images = new ArrayList<Image>();
        try {
            String query = new String().format(bundle.getString("search_images_user"), idUser);
            query = query + " LIMIT " + pager.getFrom() + ", " + pager.getTo();
            executeWithResult(query);
            while (resultSet.next()) {
                Image image = getImage(resultSet);
                pager.checkLike(image);
                images.add(image);
            }
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - updatePageImagesSearchUser");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        pager.setImages(images);
    }


    private void updatePageImagesFavorites(long idUser) {
        List<Image> images = new ArrayList<Image>();
        try {
            String query = bundle.getString("search_images_favorites");
            query = query + " LIMIT " + pager.getFrom() + ", " + pager.getTo();
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, idUser);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Image image = getImage(resultSet);
                pager.checkLike(image);
                images.add(image);
            }
            preparedStatement.close();
        } catch (SQLException ex) {
            //LogController.addLogError("ImageRepository - updatePageImagesFavorites");
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        pager.setImages(images);
    }

    private Image getImage(ResultSet resultSet) {
        Image image = new Image();
        try {
            image.setId(resultSet.getLong("img.id"));
            image.setName(resultSet.getString("img.name"));
            image.setDescription(resultSet.getString("img.description"));
            image.setDataMin(resultSet.getBytes("img.data_min"));
            image.setLikes(resultSet.getInt("img.likes"));
            image.setViews(resultSet.getInt("img.views"));

            Owner owner = new Owner();
            owner.setUsername(resultSet.getString("u.username"));
            owner.setEmail(resultSet.getString("u.email"));

            owner.setPhotoMin(resultSet.getBytes("u.photo_min"));
            image.setOwner(owner);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return image;
    }

    ///////////----------------///////////////////

    private void updateTotalCount() {
        try {
            String query = new String().format(bundle.getString("get_total_count"));
            executeWithResult(query);
            while (resultSet.next()) {
                pager.setTotalCount(resultSet.getInt("count"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    private void updateTotalCountSearch(String search) {
        search = "%" + search + "%";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(bundle.getString("get_total_count_search"));
            preparedStatement.setString(1, search);
            preparedStatement.setString(2, search);
            preparedStatement.setString(3, search);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pager.setTotalCount(resultSet.getInt("count"));
            }
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    private void updateTotalCountSearchUser(Long idUser) {
        try {
            String query = new String().format(bundle.getString("get_total_count_user"), idUser);
            executeWithResult(query);
            while (resultSet.next()) {
                pager.setTotalCount(resultSet.getInt("count"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImageRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    private void updateTotalCountFavorites(Long idUser) {
        pager.setTotalCount(pager.getFavoriteLinks().size());
    }

}
