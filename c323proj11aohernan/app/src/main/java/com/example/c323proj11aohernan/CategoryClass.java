package com.example.c323proj11aohernan;

/**
 * This class is our constructor and its methods to create
 * objects of string name, url string image, and string id (for recipes activity)
 */
public class CategoryClass {

        String name;
        String image;
        String id;

        public CategoryClass(String name, String image,String id){
            this.name = name;
            this.image = image;
            this.id = id;
        }

        public CategoryClass(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
        return id;
    }

        public void setId(String id) {
        this.id = id;
    }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
}
