package innovativedeveloper.com.socialapp.dataset.Office;

public class ModelPost {

    String Uid, pTitle, pLikes, pdescr, pImage,pId;

    public ModelPost() {

    }

    public ModelPost(String uid, String pTitle, String pLikes, String pdescr, String pImage, String pId) {
        this.Uid = uid;
        this.pTitle = pTitle;
        this.pLikes = pLikes;
        this.pdescr = pdescr;
        this.pImage = pImage;
        this.pId = pId;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getPdescr() {
        return pdescr;
    }

    public void setPdescr(String pdescr) {
        this.pdescr = pdescr;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
