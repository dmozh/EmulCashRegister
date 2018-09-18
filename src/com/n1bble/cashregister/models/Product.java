package com.n1bble.cashregister.models;

import javafx.beans.property.*;

public class Product {

    private final IntegerProperty id;
    private final StringProperty name;
    private final FloatProperty price;
    private final BooleanProperty haveDiscount;
    private final IntegerProperty amountDiscount;
    private final IntegerProperty nds;

    public Product(){
       this(null, null, Float.parseFloat(null), Boolean.parseBoolean(null), null, null);
   }

    public Product(Integer id, String name, float price, boolean haveDiscount, Integer amountDiscount, Integer nds){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleFloatProperty(price);
        this.haveDiscount = new SimpleBooleanProperty(haveDiscount);
        this.amountDiscount = new SimpleIntegerProperty(amountDiscount);
        this.nds = new SimpleIntegerProperty(nds);
    }


    public void setId(int id) {
        this.id.set(id);
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setPrice(float price) {
        this.price.set(price);
    }
    public void setHaveDiscount(boolean haveDiscount) {
        this.haveDiscount.set(haveDiscount);
    }
    public void setAmountDiscount(int amountDiscount) {
        this.amountDiscount.set(amountDiscount);
    }
    public void setNds(int nds) {
        this.nds.set(nds);
    }

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }

    public float getPrice() {
        return price.get();
    }
    public FloatProperty priceProperty() {
        return price;
    }


    public boolean isHaveDiscount() {
        return haveDiscount.get();
    }
    public BooleanProperty haveDiscountProperty() {
        return haveDiscount;
    }

    public int getAmountDiscount() {
        return amountDiscount.get();
    }
    public IntegerProperty amountDiscountProperty() {
        return amountDiscount;
    }

    public int getNds() {
        return nds.get();
    }
    public IntegerProperty ndsProperty() {
        return nds;
    }



    //используется для того чтобы в соизбоксе отображались не экземпляры класса, а названия продуктов
    @Override
    public String toString() {
        return getName();
    }

}
