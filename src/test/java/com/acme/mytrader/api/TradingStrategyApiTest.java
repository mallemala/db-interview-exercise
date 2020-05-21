package com.acme.mytrader.api;

import com.acme.mytrader.Application;
import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceListenerFactory;
import com.acme.mytrader.strategy.TradingStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.acme.mytrader.util.TestUtil.getPriceListeners;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TradingStrategyApiTest {

    @MockBean
    private ExecutionService executionService;

    @Autowired
    private TradingStrategy strategy;

    @Autowired
    private PriceListenerFactory priceListenerFactory;

    @Autowired
    private MockMvc mvc;

    private final String SECURITY = "IBM";
    private final double PRICE = 55.0;
    private final int VOLUME = 100;
    private final String INSTRUCTION = "BUY";
    private PriceListener priceListener;

    @Before
    public void setup() {
        priceListener = priceListenerFactory.createListener(SECURITY, PRICE, VOLUME, INSTRUCTION);
    }

    @Test
    public void shouldAddPriceListener() throws Exception {

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isCreated())
                .andExpect(content().string("Price Listener created successfully"));

        Map<String, List<PriceListener>> listeners = getPriceListeners(strategy);

        assertTrue(listeners.containsKey(SECURITY));
        assertNotNull(listeners.get(SECURITY));
        assertEquals(priceListener, listeners.get(SECURITY).get(0));
    }

    @Test
    public void shouldReturnBadRequestExceptionWhenInvalidInputIsPassedToAddListener() throws Exception {

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", "INVALID")
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("price", "INVALID")
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/price/listener")
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRemovePriceListener() throws Exception {

        Map<String, List<PriceListener>> listeners = getPriceListeners(strategy);
        List<PriceListener> listenersList = new ArrayList<>();
        listenersList.add(priceListener);
        listeners.put(SECURITY, listenersList);

        mvc.perform(delete("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isOk())
                .andExpect(content().string("Price Listener removed successfully"));

        assertFalse(listeners.containsKey(SECURITY));
    }

    @Test
    public void shouldReturnBadRequestExceptionWhenInvalidInputIsPassedToDeleteListener() throws Exception {

        mvc.perform(delete("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", "INVALID")
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/price/listener")
                .param("security", SECURITY)
                .param("price", "INVALID")
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/price/listener")
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/price/listener")
                .param("security", SECURITY)
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAddPriceMovement() throws Exception {

        mvc.perform(post("/price/listener")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME))
                .param("instruction", INSTRUCTION))
                .andExpect(status().isCreated())
                .andExpect(content().string("Price Listener created successfully"));
    }

    @Test
    public void shouldCallListenerWhenListenerIsPresentAndAddPriceMovementIsCalled() throws Exception {
        Map<String, List<PriceListener>> listeners = getPriceListeners(strategy);
        List<PriceListener> listenersList = new ArrayList<>();
        listenersList.add(priceListener);
        listeners.put(SECURITY, listenersList);

        double price = 10.0;
        mvc.perform(post("/price/movement")
                .param("security", SECURITY)
                .param("price", Double.toString(price)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Price Movement created successfully"));

        verify(executionService).buy(SECURITY, price, VOLUME);
    }

    @Test
    public void shouldNotCallBuyInstructionWhenPriceMovementDontBreachTriggerLevel() throws Exception {
        Map<String, List<PriceListener>> listeners = getPriceListeners(strategy);
        List<PriceListener> listenersList = new ArrayList<>();
        listenersList.add(priceListener);
        listeners.put(SECURITY, listenersList);

        mvc.perform(post("/price/movement")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Price Movement created successfully"));

        verifyZeroInteractions(executionService);
    }

    @Test
    public void shouldNotCallListenerWhenListenerIsNotPresentAndAddPriceMovementIsCalled() throws Exception {
        mvc.perform(post("/price/movement")
                .param("security", SECURITY)
                .param("price", Double.toString(PRICE)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Price Movement created successfully"));

        verifyZeroInteractions(executionService);
    }


    @Test
    public void shouldReturnBadRequestWhenInvalidInputIsPassedToAddPriceMovement() throws Exception {
        mvc.perform(post("/price/movement")
                .param("price", Double.toString(PRICE))
                .param("volume", Integer.toString(VOLUME)))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/price/movement")
                .param("security", SECURITY)
                .param("volume", Integer.toString(VOLUME)))
                .andExpect(status().isBadRequest());
    }
}