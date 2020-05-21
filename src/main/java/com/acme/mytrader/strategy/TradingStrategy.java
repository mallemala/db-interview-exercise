package com.acme.mytrader.strategy;

import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
@Service
public class TradingStrategy implements Strategy, PriceSource {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Map<String, List<PriceListener>> priceListeners;

    public TradingStrategy() {
        this.priceListeners = new HashMap<>();
    }

    @Override
    public void addPriceListener(PriceListener listener) {
        logger.info("Adding Price Listener");
        if (priceListeners.containsKey(listener.getSecurity())) {
            priceListeners.get(listener.getSecurity())
                    .add(listener);
        } else {
            List<PriceListener> listeners = new ArrayList<>();
            listeners.add(listener);
            priceListeners.put(listener.getSecurity(), listeners);
        }
    }

    @Override
    public void removePriceListener(PriceListener listener) {
        logger.info("Removing Price Listener");
        if (priceListeners.containsKey(listener.getSecurity())) {
            List<PriceListener> listeners = this.priceListeners.get(listener.getSecurity());
            listeners.remove(listener);
            if(listeners.size() == 0) {
                priceListeners.remove(listener.getSecurity());
            }
        } else {
            throw new IllegalArgumentException("Listener not found");
        }
    }

    @Override
    public void executeStrategy(String security, double price) {
        logger.info("Executing trading strategy");

        if (priceListeners.containsKey(security)) {
            logger.debug("Found price listener for the security " + security);

            priceListeners.get(security)
                    .forEach(listener -> listener.priceUpdate(security, price));
        }
    }
}
