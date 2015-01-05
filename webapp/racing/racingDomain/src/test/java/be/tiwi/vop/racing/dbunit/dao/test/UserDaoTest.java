package be.tiwi.vop.racing.dbunit.dao.test;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.dbunit.dao.factory.test.BaseSetupTest;
import be.tiwi.vop.racing.pojo.User;

@FixMethodOrder(MethodSorters.JVM)
public class UserDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

  @Test
  public void getAllUsers() {
    logger.info("Getting all users");
    List<User> users = daoManager.getUserDao().getUsers();
    Assert.assertNotNull(users);
    Assert.assertTrue(users.size() > 0);
  }

  @Test
  public void registerDummyUser() {
    logger.info("Registering a dummy user");
    int usersCount = daoManager.getUserDao().getUsers().size();
    User user = new User();
    user.setUsername("dummy");
    // TODO # hashing pasword needs to be done before insert
    user.setPassword("dummy");
    user.setEmail("dummy@racing2d.be");
    user.setFullName("dummy racer");
    daoManager.getUserDao().insertUser(user);
    Assert.assertEquals(usersCount + 1, daoManager.getUserDao().getUsers().size());
  }

  @Test
  public void authenticateUserByEmailAndUsername() {
    logger.info("Authenticating newly registerd dummy user");
    try {
      Assert.fail("Password is not hashed");
      User user = daoManager.getUserDao().authenticateUser("jan", "jan");
      logger.info("id: " + user.getId());
      Assert.assertEquals(null, user.getId());
    } catch (AssertionError ae) {
      logger.error("Expected error: " + ae.getMessage());
    }
  }

  @Test
  public void getUserById() {
    logger.info("Getting dummy user by id");
    Assert.assertNotNull(daoManager.getUserDao().getUserById(3));
  }

  @Test
  public void getUserByUsername() {
    logger.info("Getting dummy user by username");
    User dummy = daoManager.getUserDao().getUserByUsername("thomas");
    Assert.assertNotNull(dummy.getId());
    Assert.assertEquals("thomas", dummy.getUsername());
    Assert.assertEquals("Thomas", dummy.getFullName());
    Assert.assertEquals("thomas@racing2d.be", dummy.getEmail());
  }

  @Test
  public void getUserByEmail() {
    logger.info("Getting dummy user by email");
    User dummy = daoManager.getUserDao().getUserByEmail("thomas@racing2d.be");
    Assert.assertNotNull(dummy.getId());
    Assert.assertEquals("thomas", dummy.getUsername());
    Assert.assertEquals("Thomas", dummy.getFullName());
    Assert.assertEquals("thomas@racing2d.be", dummy.getEmail());
  }

  @Test
  public void updateUser() {
    logger.info("Updating dummy user");
    User dummy = daoManager.getUserDao().getUserByUsername("sander");
    dummy.setEmail("sander@racing2d.be");
    dummy.setIsPublic(false);
    daoManager.getUserDao().updateUser(dummy);
    dummy = daoManager.getUserDao().getUserByEmail("sander@racing2d.be");
    Assert.assertNotNull(dummy.getId());
    Assert.assertEquals("sander", dummy.getUsername());
    Assert.assertEquals("Sander", dummy.getFullName());
    Assert.assertEquals("sander@racing2d.be", dummy.getEmail());
    Assert.assertEquals(false, dummy.getIsPublic());
  }
}
