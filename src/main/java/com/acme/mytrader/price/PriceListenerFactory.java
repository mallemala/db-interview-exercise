package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.impl.BuyInstructionListener;
import com.acme.mytrader.price.impl.SellInstructionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.acme.mytrader.execution.ExecutionService.InstructionType.BUY;
import static com.acme.mytrader.execution.ExecutionService.InstructionType.SELL;

@Service
public class PriceListenerFactory {

    @Autowired
    private ExecutionService executionService;

    public PriceListener createListener(String security, double price, int volume, String instruction) {
        if (BUY.name().equals(instruction)) {
            return new BuyInstructionListener(security, price, volume, executionService);
        } else if (SELL.name().equals(instruction)){
            return new SellInstructionListener(security, price, volume, executionService);
        } else {
            throw new IllegalArgumentException("Invalid Instruction Type");
        }
    }
}
