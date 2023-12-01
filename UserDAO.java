public abstract class UserDAO {
 // Instance unique de la classe AbstractDAOFactory
 protected static UserDAO userDAO;

 // Méthode pour obtenir l'instance unique de la classe AbstractDAOFactory
 public static UserDAO getUserDAO() {
  if(userDAO==null){
   userDAO=new PgUserDAO();
  }
  return userDAO;
 }

 public abstract User getUserByUsername(String username);
 public abstract User login(String username, String password);

}