package com.acme.mytrader.price.impl;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SellInstructionListener implements PriceListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private String security;
    private double price;
    private int volume;
    private ExecutionService executionService;

    public SellInstructionListener( String security, double price, int volume, ExecutionService executionService) {
        this.security = security;
        this.price = price;
        this.volume = volume;
        this.executionService = executionService;
    }

    @Override
    public String getSecurity() {
        return security;
    }

    @Override
    public void priceUpdate(String security, double price) {
        logger.info("Executing price update");
        if (this.security.equals(security) && price > this.price) {

            logger.info("Executing sell instruction for security " + security);
            executionService.sell(security, price, volume);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellInstructionListener that = (SellInstructionListener) o;
        return Double.compare(that.price, price) == 0 &&
                volume == that.volume &&
                security.equals(that.security) &&
                executionService.equals(that.executionService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(security, price, volume, executionService);
    }
}
