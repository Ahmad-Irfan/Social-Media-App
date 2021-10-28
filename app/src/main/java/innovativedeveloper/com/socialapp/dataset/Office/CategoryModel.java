package innovativedeveloper.com.socialapp.dataset.Office;

public class CategoryModel {
    String category;
    String categoryId;
    String image;

    public CategoryModel() {
    }

    public CategoryModel(String category, String categoryId, String image) {
        this.category = category;
        this.categoryId = categoryId;
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
