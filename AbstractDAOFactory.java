public abstract class AbstractDAOFactory {

 // Instance unique de la classe AbstractDAOFactory
 protected static AbstractDAOFactory factory;

 public static AbstractDAOFactory getFactory() {
  if(factory==null){
    factory = new PostgreSQLDAOFactory();
  }
  return factory;
 }

 public UserDAO getUserDAO(){
  return UserDAO.getUserDAO();
 };

}
