package innovativedeveloper.com.socialapp.dataset.Office;

public class Postmember {

    String name,url,postUri,time,uid,type,desc;

    public Postmember() {
    }

    public Postmember(String name, String url, String postUri, String time, String uid, String type, String desc) {
        this.name = name;
        this.url = url;
        this.postUri = postUri;
        this.time = time;
        this.uid = uid;
        this.type = type;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}