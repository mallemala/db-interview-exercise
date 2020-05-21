package com.acme.mytrader.strategy;

import com.acme.mytrader.price.PriceListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    private final String SECURITY = "IBM";
    private PriceListener listener = mock(PriceListener.class);
    private TradingStrategy tradingStrategy = new TradingStrategy();

    @Test
    public void shouldAddPriceListener() throws NoSuchFieldException, IllegalAccessException {
        when(listener.getSecurity()).thenReturn(SECURITY);
        tradingStrategy.addPriceListener(listener);

        Field priceListeners = tradingStrategy.getClass().getDeclaredField("priceListeners");
        priceListeners.setAccessible(true);
        Map<String, List<PriceListener>> listeners = (Map<String, List<PriceListener>>) priceListeners.get(tradingStrategy);
        assertTrue(listeners.containsKey(SECURITY));
        assertNotNull(listeners.get(SECURITY));
        assertEquals(listener, listeners.get(SECURITY).get(0));
    }

    @Test
    public void shouldRemovePriceListener() throws NoSuchFieldException, IllegalAccessException {
        Field priceListeners = tradingStrategy.getClass().getDeclaredField("priceListeners");
        priceListeners.setAccessible(true);
        Map<String, List<PriceListener>> listeners = (Map<String, List<PriceListener>>) priceListeners.get(tradingStrategy);
        List<PriceListener> listenersList = new ArrayList<>();
        listenersList.add(listener);
        listeners.put(SECURITY, listenersList);

        when(listener.getSecurity()).thenReturn(SECURITY);
        tradingStrategy.removePriceListener(listener);

        assertFalse(listeners.containsKey(SECURITY));
    }

}
