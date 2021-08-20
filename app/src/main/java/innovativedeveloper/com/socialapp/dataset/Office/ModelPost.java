package innovativedeveloper.com.socialapp.dataset.Office;

public class ModelPost {

    String Uid,pTitle,pdescr,pImage;

    public ModelPost() {
    }

    public ModelPost(String Uid, String pTitle, String pdescr, String pImage) {
        this.Uid = Uid;
        this.pTitle = pTitle;
        this.pdescr = pdescr;
        this.pImage = pImage;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
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
}
