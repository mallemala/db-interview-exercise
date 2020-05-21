package com.acme.mytrader.util;

import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.strategy.TradingStrategy;
import org.junit.Ignore;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Ignore
public class TestUtil {

    public static Map<String, List<PriceListener>> getPriceListeners(TradingStrategy tradingStrategy) throws NoSuchFieldException, IllegalAccessException {
        Field priceListeners = tradingStrategy.getClass().getDeclaredField("priceListeners");
        priceListeners.setAccessible(true);
        return (Map<String, List<PriceListener>>) priceListeners.get(tradingStrategy);
    }
}
