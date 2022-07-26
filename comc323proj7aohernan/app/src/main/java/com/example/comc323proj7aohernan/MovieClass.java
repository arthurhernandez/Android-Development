package com.example.comc323proj7aohernan;

public class MovieClass {
    String name;
    String image;

    public MovieClass(String name, String image){
        this.name = name;
        this.image = image;
    }

    public MovieClass(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
