package be.tiwi.vop.racing.domain.dao;

import java.util.List;

import be.tiwi.vop.racing.core.model.User;

public interface UserDao {
  List<User> getUsers();

  User authenticateUser(String username, String password);

  User getUserByUsername(String username);

  User getUserById(int id);

  User getUserByEmail(String email);

  void insertUser(User user);

  void updateUser(User user);
}
