package info.reportissues.communitycommunicator.models;

/**
 * Created by howardpassmore on 8/2/17.
 */

public class User {
    private int id;

    private String email;

    private String password;

    public User() {

    }

    public User(String email, String password) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}