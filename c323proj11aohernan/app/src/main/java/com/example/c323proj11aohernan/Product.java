package com.example.c323proj11aohernan;

public class Product {

    private String _name;
    private String _price;
    private String _category;
    private String _date;



    public String get_productName() {
        return _name;
    }

    public void set_productName(String _name) { this._name = _name; }


    public String get_productPrice() { return _price;}

    public void set_productPrice(String _price) {
        this._price = _price;
    }


    public String get_productCategory() { return _category;}

    public void set_productCategory(String _category) {
        this._category = _category;
    }

    public String get_productDate() { return _date; }

    public void set_productDate(String _date) { this._date= _date; }


    public Product(String _name, String _price, String _category, String _date) {
        this._name = _name;
        this._price = _price;
        this._category = _category;
        this._date = _date;
    }

    public Product() {
    }



}