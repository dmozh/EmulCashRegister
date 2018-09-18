package com.n1bble.cashregister.models;

public class VoucherPosition {
//    private final Integer
    private final float price;
    private final String name;
    private Integer count;
    private final float discount;
    private final float priceWithNDS;

    public VoucherPosition(Float price, String name, Integer count, Float discount, Float priceWithNDS) {
        this.price = price;
        this.name = name;
        this.count = count;
        this.discount = discount;
        this.priceWithNDS = priceWithNDS;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public Float getDicount() {
        return discount;
    }

    public Float getPriceWithNDS(){
        return priceWithNDS;
    }

    public void setCount(int count){
        this.count = count;
    }

}
