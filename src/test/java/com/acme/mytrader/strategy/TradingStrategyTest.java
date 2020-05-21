package com.acme.mytrader.strategy;

import com.acme.mytrader.price.PriceListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.acme.mytrader.util.TestUtil.getPriceListeners;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    private final String SECURITY = "IBM";
    private PriceListener listener = mock(PriceListener.class);
    private TradingStrategy tradingStrategy = new TradingStrategy();

    @Test
    public void shouldAddPriceListener() throws NoSuchFieldException, IllegalAccessException {
        when(listener.getSecurity()).thenReturn(SECURITY);
        tradingStrategy.addPriceListener(listener);

        Map<String, List<PriceListener>> listeners = getPriceListeners(tradingStrategy);
        assertTrue(listeners.containsKey(SECURITY));
        assertNotNull(listeners.get(SECURITY));
        assertEquals(listener, listeners.get(SECURITY).get(0));
    }

    @Test
    public void shouldRemovePriceListener() throws NoSuchFieldException, IllegalAccessException {
        Map<String, List<PriceListener>> listeners = getPriceListeners(tradingStrategy);
        List<PriceListener> listenersList = new ArrayList<>();
        listenersList.add(listener);
        listeners.put(SECURITY, listenersList);

        when(listener.getSecurity()).thenReturn(SECURITY);
        tradingStrategy.removePriceListener(listener);

        assertFalse(listeners.containsKey(SECURITY));
    }

    @Test
    public void shouldUpdatePriceWhenListerIsAvailable() throws NoSuchFieldException, IllegalAccessException {

        Map<String, List<PriceListener>> listeners = getPriceListeners(tradingStrategy);
        List<PriceListener> listenersList = new ArrayList<>();
        listenersList.add(listener);
        listeners.put(SECURITY, listenersList);

        double price = 55.0;
        tradingStrategy.executeStrategy(SECURITY, price);

        verify(listener).priceUpdate(SECURITY, price);
    }

    @Test
    public void shouldNotUpdatePriceWhenListerIsNotAvailable() throws NoSuchFieldException, IllegalAccessException {
        Map<String, List<PriceListener>> listeners = getPriceListeners(tradingStrategy);
        listeners.clear();

        double price = 55.0;
        tradingStrategy.executeStrategy(SECURITY, price);
        verifyZeroInteractions(listener);
    }




}
