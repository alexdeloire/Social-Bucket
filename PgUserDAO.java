public class PgUserDAO extends UserDAO {

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User login(String username, String password) {
        
        if (username.equals("admin") && password.equals("admin")) {
            return new User("admin", "admin");
        } else {
            return null;
        }
    }
}
