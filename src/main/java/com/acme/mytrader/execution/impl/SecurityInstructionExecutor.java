package com.acme.mytrader.execution.impl;

import com.acme.mytrader.execution.ExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityInstructionExecutor implements ExecutionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void buy(String security, double price, int volume) {
        logger.debug("Buying [security: " + security + ", price: " + price + ", volume: " + volume + "]");
    }

    @Override
    public void sell(String security, double price, int volume) {
        logger.debug("Selling [security: " + security + ", price: " + price + ", volume: " + volume + "]");
    }
}
