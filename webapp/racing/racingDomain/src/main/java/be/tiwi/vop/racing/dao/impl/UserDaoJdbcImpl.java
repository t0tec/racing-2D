package be.tiwi.vop.racing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import be.tiwi.vop.racing.DaoUtility;
import be.tiwi.vop.racing.dao.UserDao;
import be.tiwi.vop.racing.core.model.User;

public class UserDaoJdbcImpl implements UserDao {
  private Connection connection;

  public UserDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<User> getUsers() {
    List<User> users = new ArrayList<User>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM users");
      rs = ps.executeQuery();

      while (rs.next()) {
        User user = mapToUser(rs);
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return users;
  }

  @Override
  public User getUserByUsername(String username) {
    User user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM users WHERE username = ?");
      ps.setString(1, username);

      rs = ps.executeQuery();

      while (rs.next()) {
        user = mapToUser(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return user;
  }

  private String hashPassword(String password) {
    return DigestUtils.sha512Hex(password);
  }

  @Override
  public User authenticateUser(String username, String password) {
    String hashed = this.hashPassword(password);
    User user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
      ps.setString(1, username);
      ps.setString(2, hashed);

      rs = ps.executeQuery();

      while (rs.next()) {
        user = mapToUser(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return user;
  }

  @Override
  public User getUserById(int id) {
    User user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM users WHERE id = ?");
      ps.setInt(1, id);

      rs = ps.executeQuery();

      while (rs.next()) {
        user = mapToUser(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return user;
  }

  @Override
  public User getUserByEmail(String email) {
    User user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM users WHERE email = ?");
      ps.setString(1, email);

      rs = ps.executeQuery();

      while (rs.next()) {
        user = mapToUser(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return user;
  }

  @Override
  public void insertUser(User user) {
    PreparedStatement ps = null;
    try {
      ps =
          this.connection
              .prepareStatement("INSERT INTO users (username, password, email, full_name) VALUES (?, ?, ?, ?)");
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setString(3, user.getEmail());
      ps.setString(4, user.getFullName());
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

  @Override
  public void updateUser(User user) {
    PreparedStatement ps = null;
    try {
      ps =
          connection
              .prepareStatement("UPDATE users SET username = ?, password = ?, email = ?, full_name = ?, public = ? WHERE id = ?");
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setString(3, user.getEmail());
      ps.setString(4, user.getFullName());
      ps.setBoolean(5, user.getIsPublic());
      ps.setInt(6, user.getId());
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

  public static User mapToUser(ResultSet rs) {
    User user = new User();
    try {
      user.setId(rs.getInt("id"));
      user.setUsername(rs.getString("username"));
      user.setPassword(rs.getString("password"));
      user.setEmail(rs.getString("email"));
      user.setFullName(rs.getString("full_name"));
      user.setIsPublic(rs.getBoolean("public"));
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    }

    return user;
  }

}
