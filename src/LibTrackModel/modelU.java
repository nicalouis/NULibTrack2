package LibTrackModel;

public class modelU {

    protected int userId;
    protected String fullName;
    protected String email;
    protected String password;

    public modelU(int userId, String fullName, String email, String password) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    // GETTERS

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // SETTERS

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}