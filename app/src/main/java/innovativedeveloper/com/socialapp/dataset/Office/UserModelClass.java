package innovativedeveloper.com.socialapp.dataset.Office;

public class UserModelClass {
    String name,email,password,uid,username;

    public UserModelClass() {
    }

    public UserModelClass(String name, String email, String password, String uid, String username) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
