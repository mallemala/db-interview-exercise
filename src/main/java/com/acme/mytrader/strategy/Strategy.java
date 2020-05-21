package com.acme.mytrader.strategy;

public interface Strategy {
    void executeStrategy(String security, double price);
}
