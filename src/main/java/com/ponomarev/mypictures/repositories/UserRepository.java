package com.ponomarev.mypictures.repositories;

import com.ponomarev.mypictures.controllers.LogController;
import com.ponomarev.mypictures.models.User;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igorek
 */
public class UserRepository extends BaseRepository {

    public UserRepository() {
        super("queries");
    }

    public boolean checkUsernameExist(String username) {
        //LogController.addLog("UserRepository - checkUsernameExist");
        boolean isExist = false;
        try {
            String query = String.format(bundle.getString("check_username_exist"), username);
            executeWithResult(query);
            while (resultSet.next()) {
                isExist = resultSet.getBoolean("exist");
            }
        } catch (SQLException ex) {
            //LogController.addLogError("UserRepository - checkUsernameExist");
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return isExist;
    }

    public void addUser(User user) {
        //LogController.addLog("UserRepository - addUser - registration");
        try {
            String query1 = String.format(bundle.getString("add_user"),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName());
            execute(query1);
            closeConnection();

            String query2 = String.format(bundle.getString("get_user_id"), user.getUsername());
            executeWithResult(query2);
            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
            }
            closeConnection();

            String query3 = String.format(bundle.getString("add_user_group"), user.getId());
            execute(query3);
            closeConnection();
        } catch (SQLException ex) {
            //LogController.addLog("UserRepository - addUser - registration");
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public User getUser(String username) {
        //LogController.addLog("UserRepository - getUser(" + username +")");
        User user = new User();
        try {
            String query = String.format(bundle.getString("get_user"), username);
            executeWithResult(query);
            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setDescription(resultSet.getString("description"));
                user.setPhotoMin(resultSet.getBytes("photo_min"));
                user.setPhoto(resultSet.getBytes("photo"));
            }
        } catch (SQLException ex) {
            //LogController.addLogError("UserRepository - getUser(" + username +")");
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return user;
    }

    public void updateUser(User user) {
        //LogController.addLog("UserRepository - updateUser(" + user.getId() +")");
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(bundle.getString("update_user"));
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getDescription());
            preparedStatement.setBytes(5, user.getPhoto());
            preparedStatement.setBytes(6, user.getPhotoMin());
            preparedStatement.setLong(7, user.getId());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            //LogController.addLogError("UserRepository - updateUser(" + user.getId() +")");
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            closeConnection();
        }

    }
}
