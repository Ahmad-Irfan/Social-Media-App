package innovativedeveloper.com.socialapp.dataset.Office;

public class ReplyComments {
    String username;
    String usermsg;
    String uid;
    String data;
    String time;
    String commentid;
    String postId;

    public ReplyComments() {
    }

    public ReplyComments(String username, String usermsg, String uid, String data, String time, String commentid, String postId) {
        this.username = username;
        this.usermsg = usermsg;
        this.uid = uid;
        this.data = data;
        this.time = time;
        this.commentid = commentid;
        this.postId = postId;
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

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
