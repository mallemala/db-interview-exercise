package com.acme.mytrader.api;

import com.acme.mytrader.price.PriceListenerFactory;
import com.acme.mytrader.strategy.Strategy;
import com.acme.mytrader.strategy.TradingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
public class TradingStrategyApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private TradingStrategy tradingStrategy;

    @Autowired
    private PriceListenerFactory priceListenerFactory;


    @PostMapping(path = "/price/listener")
    @ResponseStatus(HttpStatus.CREATED)
    public String addPriceListener(@NotBlank @RequestParam String security, @NotNull @RequestParam double price, @NotNull @RequestParam int volume, @NotBlank @RequestParam String instruction) {
        logger.info("Adding price listener");

        tradingStrategy.addPriceListener(priceListenerFactory.createListener(security, price, volume, instruction));
        return "Price Listener created successfully";
    }

    @DeleteMapping(path = "/price/listener")
    @ResponseStatus(HttpStatus.OK)
    public String deletePriceListener(@NotBlank @RequestParam String security, @NotNull @RequestParam double price, @NotNull @RequestParam int volume, @NotBlank @RequestParam String instruction) {
        logger.info("Removing price listener");
        try {
            tradingStrategy.removePriceListener(priceListenerFactory.createListener(security, price, volume, instruction));
            return "Price Listener removed successfully";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping(path = "/price/movement")
    @ResponseStatus(HttpStatus.CREATED)
    public String addPriceMovement(@NotBlank @RequestParam String security, @NotNull @RequestParam double price) {
        logger.info("Adding price movement");

        tradingStrategy.executeStrategy(security, price);
        return "Price Movement created successfully";
    }
}


