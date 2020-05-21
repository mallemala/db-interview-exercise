package com.acme.mytrader.execution;

public interface ExecutionService {
    enum InstructionType {BUY, SELL}
    void buy(String security, double price, int volume);
    void sell(String security, double price, int volume);
}
