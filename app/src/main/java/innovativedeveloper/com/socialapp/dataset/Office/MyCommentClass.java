package innovativedeveloper.com.socialapp.dataset.Office;

public class MyCommentClass {

    String username;
    String usermsg;
    String uid;
    String data;
    String time;

    public MyCommentClass() {
    }

    public MyCommentClass(String username, String usermsg, String uid, String data, String time) {
        this.username = username;
        this.usermsg = usermsg;
        this.uid = uid;
        this.data = data;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}