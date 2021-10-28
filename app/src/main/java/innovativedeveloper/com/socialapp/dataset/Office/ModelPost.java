package innovativedeveloper.com.socialapp.dataset.Office;

public class ModelPost {

    String Uid, pTitle, pLikes, pDescr, pImage,pId,type,uName,categoryId;

    public ModelPost() {

    }

    public ModelPost(String uid, String pTitle, String pLikes, String pDescr, String pImage, String pId, String type, String uName, String categoryId) {
        Uid = uid;
        this.pTitle = pTitle;
        this.pLikes = pLikes;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.pId = pId;
        this.type = type;
        this.uName = uName;
        this.categoryId = categoryId;
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

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
