package com.acme.mytrader.price.impl;

import com.acme.mytrader.execution.ExecutionService;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BuyInstructionListenerTest {

    private ExecutionService executionService = mock(ExecutionService.class);
    private final String SECURITY = "IBM";
    private final double PRICE = 55.0;
    private final int VOLUME = 100;

    private BuyInstructionListener listener = new BuyInstructionListener(SECURITY, PRICE, VOLUME, executionService);

    @Test
    public void shouldCallBuyInstructionIfSecurityPriceIsLower() {
        double price = 44.0;

        listener.priceUpdate(SECURITY, price);
        verify(executionService).buy(SECURITY, price, VOLUME);
    }

    @Test
    public void shouldNotCallBuyInstructionIfSecurityIsDifferent() {
        String testSecurity = "TEST SECURITY";
        listener.priceUpdate(testSecurity, PRICE);
        verifyZeroInteractions(executionService);
    }

    @Test
    public void shouldNotCallBuyInstructionIfPriceIsGreaterThanListenerPrice() {
        double price = 100.0;
        listener.priceUpdate(SECURITY, price);
        verifyZeroInteractions(executionService);
    }


    @Test
    public void shouldNotCallBuyInstructionIfPriceIsEqualsListenerPrice() {
        listener.priceUpdate(SECURITY, PRICE);
        verifyZeroInteractions(executionService);
    }

}