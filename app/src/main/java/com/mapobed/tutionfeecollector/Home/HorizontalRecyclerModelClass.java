package com.mapobed.tutionfeecollector.Home;

public class HorizontalRecyclerModelClass {
    private String id;
    int image;

    public HorizontalRecyclerModelClass() {
    }

    public HorizontalRecyclerModelClass(String id, int image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
