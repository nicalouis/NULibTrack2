package LibTrackModel;

public class modelU {

    protected int userId;
    protected String fullName;
    protected String email;
    protected String password;

    // 🔥 NEW: role support
    protected String role;

    // 🔥 OPTIONAL FUTURE USE
    protected boolean active = true;

    // ================= ORIGINAL CONSTRUCTOR =================
    public modelU(int userId, String fullName, String email, String password) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;

        this.role = "PATRON"; // default role
    }

    // ================= NEW CONSTRUCTOR (WITH ROLE) =================
    public modelU(int userId, String fullName, String email, String password, String role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ================= GETTERS =================

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

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    // ================= SETTERS =================

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

    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}