package com.n1bble.cashregister.models;

import javafx.collections.ObservableList;


public class CashVoucher{
    private final ObservableList<VoucherPosition> positions;
//    private final LocalDateTime timeStamp;
    private final String timeStampDate;
    private final String timeStampTime;
    private final String paymentMethod;
    private final Float sumForVouchar;
    private final String address;
    private final String idCashRegister;
    private final String nameCashier;
    private final Integer numVoucher;

    public CashVoucher(ObservableList<VoucherPosition> positions, String timeStampDate, String timeStampTime,
                       String paymentMethod, Float sumForVouchar,
                       String address, String idCashRegister, String nameCashier, Integer numVoucher) {
        this.positions = positions;
        this.timeStampDate = timeStampDate;
        this.timeStampTime = timeStampTime;
        this.paymentMethod = paymentMethod;
        this.sumForVouchar = sumForVouchar;
        this.address = address;
        this.idCashRegister = idCashRegister;
        this.nameCashier = nameCashier;
        this.numVoucher = numVoucher;
    }

    public ObservableList<VoucherPosition> getPositions() {
        return positions;
    }

    public String getDate() {
        return timeStampDate;
    }

    public String getTime() {
        return timeStampTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Float getSumForVouchar() {
        return sumForVouchar;
    }

    public String getAddress() {
        return address;
    }

    public String getIdCashRegister() {
        return idCashRegister;
    }

    public String getNameCashier() {
        return nameCashier;
    }

    public Integer getNumVoucher() {
        return numVoucher;
    }
}
