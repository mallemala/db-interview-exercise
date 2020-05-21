package com.acme.mytrader.price.impl;

import com.acme.mytrader.execution.ExecutionService;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SellInstructionListenerTest {

    private ExecutionService executionService = mock(ExecutionService.class);
    private final String SECURITY = "IBM";
    private final double PRICE = 55.0;
    private final int VOLUME = 100;

    private SellInstructionListener listener = new SellInstructionListener(SECURITY, PRICE, VOLUME, executionService);

    @Test
    public void shouldCallSellInstructionIfSecurityPriceIsGreaterThanListenerPrice() {
        double price = 60;

        listener.priceUpdate(SECURITY, price);
        verify(executionService).sell(SECURITY, price, VOLUME);
    }

    @Test
    public void shouldNotCallSellInstructionIfSecurityIsDifferent() {
        String testSecurity = "TEST SECURITY";
        listener.priceUpdate(testSecurity, PRICE);
        verifyZeroInteractions(executionService);
    }

    @Test
    public void shouldNotCallSellInstructionIfPriceIsLessThanListenerPrice() {
        double price = 10;
        listener.priceUpdate(SECURITY, price);
        verifyZeroInteractions(executionService);
    }


    @Test
    public void shouldNotCallSellInstructionIfPriceIsEqualsListenerPrice() {
        listener.priceUpdate(SECURITY, PRICE);
        verifyZeroInteractions(executionService);
    }

}