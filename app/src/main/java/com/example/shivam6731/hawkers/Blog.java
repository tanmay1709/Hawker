package com.example.shivam6731.hawkers;

/**
 * Created by shivam6731 on 1/21/2017.
 */

public class Blog {
    private String title;
    private String image;
    private String desc;
    private String latitude,longitude,cat;



    private String username;
    public Blog(){

    }

    public Blog(String title, String desc, String image,String latitude,String longitude,String cat) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username=username;
        this.latitude=latitude;
        this.longitude=longitude;
        this.cat=cat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }
    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

public String getCat()
{
    return cat;
}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
