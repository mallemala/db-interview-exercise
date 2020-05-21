package com.acme.mytrader.price;

public interface PriceListener {

    String getSecurity();

    void priceUpdate(String security, double price);
}
