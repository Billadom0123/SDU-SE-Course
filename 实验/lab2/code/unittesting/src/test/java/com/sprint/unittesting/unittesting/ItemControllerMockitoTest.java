package com.sprint.unittesting.unittesting;

import com.sprint.unittesting.unittesting.business.ItemBusinessService;
import com.sprint.unittesting.unittesting.controller.ItemController;
import com.sprint.unittesting.unittesting.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerMockitoTest {

    @Mock
    private ItemBusinessService businessService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testDummyItem() throws Exception {
        mockMvc.perform(get("/dummy-item")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ball"))
                .andExpect(jsonPath("$.width").value(10))
                .andExpect(jsonPath("$.height").value(100));
    }

    @Test
    public void testItemFromBusinessService() throws Exception {
        Item mockItem = new Item(2, "Table", 120, 200);
        given(businessService.retreiveHardcodedItem()).willReturn(mockItem);

        mockMvc.perform(get("/item-from-business-service")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Table"))
                .andExpect(jsonPath("$.width").value(120))
                .andExpect(jsonPath("$.height").value(200));

        verify(businessService, times(1)).retreiveHardcodedItem();
    }

    @Test
    public void testRetrieveAllItems() throws Exception {
        List<Item> mockItems = Arrays.asList(
                new Item(1, "TestItem0", 10, 20),
                new Item(2, "TestItem1", 20, 40),
                new Item(3, "TestItem2", 30, 60),
                new Item(4, "TestItem3", 40, 80),
                new Item(5, "TestItem4", 50, 100)
        );
        given(businessService.retrieveAllItems()).willReturn(mockItems);

        mockMvc.perform(get("/all-items-from-database")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));

        verify(businessService, times(1)).retrieveAllItems();
    }
}