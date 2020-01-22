package com.ponomarev.mypictures.old;

import com.ponomarev.mypictures.models.User;
import com.ponomarev.mypictures.repositories.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepositoryOld extends BaseRepository {

    public UserRepositoryOld() {
        super("queries");
    }

    public boolean checkUsernameExist(String username) {
        boolean isExist = false;
        try {
            String query = bundle.getString("check_username_exist");
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isExist = resultSet.getBoolean("exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRepositoryOld.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return isExist;
    }

    public void addUser(User user) {
        try {
            String query1 = bundle.getString("add_user");
            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();

            String query2 = bundle.getString("get_user_id");
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, user.getUsername());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
            }
            preparedStatement.close();
            closeConnection();

            String query3 = bundle.getString("add_user_group");
            preparedStatement = getConnection().prepareStatement(query3);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserRepositoryOld.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public User getUser(String username) {
        User user = new User();
        try {
            String query = bundle.getString("get_user");
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
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
            preparedStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserRepositoryOld.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return user;
    }

    public void updateUser(User user) {
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
            Logger.getLogger(UserRepositoryOld.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            closeConnection();
        }

    }
}
