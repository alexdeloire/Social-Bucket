public class User {

    private String username;
    private String mail;
    private String mdp;



    // Constructeur
    public User(String username, String mail, String mdp) {
        this.username = username;
        this.mail = mail;
        this.mdp = mdp;
    }

    public User(String username, String mdp) {
        this.username = username;
        this.mdp = mdp;
        
    }

    // Getter et setter pour username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter et setter pour mail
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    
    public String getMdp() {
        return mdp;
    }
}
