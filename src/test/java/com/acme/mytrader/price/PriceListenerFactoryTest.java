package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.impl.BuyInstructionListener;
import com.acme.mytrader.price.impl.SellInstructionListener;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PriceListenerFactoryTest {

    @Mock
    private ExecutionService executionService;

    @InjectMocks
    private PriceListenerFactory priceListenerFactory = new PriceListenerFactory();

    private final String SECURITY = "IBM";
    private final double PRICE = 55.0;
    private final int VOLUME = 100;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldCreateBuyInstructionListener() {
        PriceListener buyListener = priceListenerFactory.createListener(SECURITY, PRICE, VOLUME, "BUY");
        assertNotNull(buyListener);
        assertTrue(buyListener instanceof BuyInstructionListener);
    }

    @Test
    public void shouldCreateSellInstructionListener() {
        PriceListener sellListener = priceListenerFactory.createListener(SECURITY, PRICE, VOLUME, "SELL");
        assertNotNull(sellListener);
        assertTrue(sellListener instanceof SellInstructionListener);
    }

    @Test
    public void shouldThrowIllegalArgumentException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid Instruction Type");
        priceListenerFactory.createListener(SECURITY, PRICE, VOLUME, "INVLAID");
    }

}