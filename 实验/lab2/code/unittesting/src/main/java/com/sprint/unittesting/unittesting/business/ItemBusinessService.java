package com.sprint.unittesting.unittesting.business;

import com.sprint.unittesting.unittesting.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemBusinessService {
    public Item retreiveHardcodedItem(){
        return new Item(2, "Table", 120, 200);
    }

    public List<Item> retrieveAllItems(){
        List<Item> list = new ArrayList<>();

        for(int i = 0;i < 5;i++){
            list.add(new Item(i+1,"TestItem" + i,(i+1)*10,(i+1)*20));
        }

        return list;
    }
}
