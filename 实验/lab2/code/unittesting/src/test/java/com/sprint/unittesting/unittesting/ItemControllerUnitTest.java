package com.sprint.unittesting.unittesting;

import com.sprint.unittesting.unittesting.controller.ItemController;
import com.sprint.unittesting.unittesting.model.Item;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ItemControllerUnitTest {
    private static final Logger logger = LogManager.getLogger(ItemControllerUnitTest.class);

    @Resource
    private ItemController itemController;

    // cmp Item src and target
    // both of them should not be null
    private boolean itemEqual(Item src,Item target){
        assertNotNull(target);

        logger.info("testing source item is not null");
        assertNotNull(src);

        logger.info("testing source item id is "+target.getId());
        assertEquals(src.getId(),target.getId());

        logger.info("testing source item name is "+target.getName());
        assertEquals(src.getName(),target.getName());

        logger.info("testing source item width is "+target.getWidth());
        assertEquals(src.getWidth(),target.getWidth());

        logger.info("testing source item height is "+target.getHeight());
        assertEquals(src.getHeight(),target.getHeight());

        logger.info("2 items are equal");

        return true;
    }

    public void fieldNotEqual(String methodName,Object srcValue,Object targetValue){
        logger.error("Field accessed by {} is not equal. Src value: {}, Target value: {}",
                methodName, srcValue, targetValue);
    }

    // use Reflective to cmp src and target
    public boolean ReflectiveItemEqual(Item src,Item target){
        assertNotNull(target);
        logger.info("testing source item is not null");
        assertNotNull(src);

        Class<?> clazz = src.getClass();
        Method[] methods = clazz.getMethods();

        boolean isEqual = true;

        for (Method method : methods) {
            String methodName = method.getName();
            // get* method should have a signature which length is greater than 3 and have no param
            // exclude getClass()
            if (methodName.startsWith("get") && methodName.length() > 3 && method.getParameterCount() == 0 && !methodName.equals("getClass")) {
                try {
                    Object srcValue = method.invoke(src);
                    Object targetValue = method.invoke(target);

                    // for case:String
                    if (srcValue == null && targetValue == null) {
                        continue;
                    }
                    // for case:String
                    if (srcValue == null || targetValue == null) {
                        fieldNotEqual(methodName,srcValue,targetValue);
                        isEqual = false;
                        continue;
                    }

                    if(!srcValue.equals(targetValue)){
                        fieldNotEqual(methodName,srcValue,targetValue);
                        isEqual = false;
                        continue;
                    }

                    logger.info("Testing field accessed by {} is equal", methodName);
                } catch (Exception e) {
                    logger.error("Error while invoking method {}", methodName, e);
                    isEqual = false;
                }
            }
        }

        if (isEqual) {
            logger.info("2 items are equal");
        } else {
            logger.error("2 items are not equal");
        }
        return isEqual;
    }

    @Test
    public void testDummyItem() {
        Item dummy = itemController.dummyItem();

//        itemEqual(dummy,new Item(1, "Ball", 10, 100));
        boolean flag = ReflectiveItemEqual(dummy,new Item(1, "Ball", 10, 100));

        if (flag){
            logger.info("API:/dummy-item has successfully passed");
        }else{
            logger.error("API:/dummy-item has failed");
        }
    }

    @Test
    public void testDummyItem1e6(){
        long start = System.nanoTime();

        Item cmp = new Item(1, "Ball", 10, 100);
        boolean flag = true;
        for (int i = 0; i < (int) 1e6; i++) {
//            itemController.dummyItem();
            flag &= ReflectiveItemEqual(itemController.dummyItem(),cmp);
        }

        long end = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(end-start);
        if(!flag){
            logger.error("API:/dummy-item has failed during its test");
        }else{
            logger.info("API:/dummy-item has successfully passed");
        }

        logger.info("test duration(milliseconds):"+duration);
    }

    @Test
    public void testItemFromBusinessService(){
        boolean flag = ReflectiveItemEqual(itemController.itemFromBusinessService(), new Item(2, "Table", 120, 200));

        if (flag){
            logger.info("API:/item-from-business-service has successfully passed");
        }else{
            logger.error("API:/item-from-business-service has failed");
        }

    }

    @Test
    public void testItemFromBusinessService1e6(){
        long start = System.nanoTime();

        Item cmp = new Item(2, "Table", 120, 200);
        boolean flag = true;
        for (int i = 0; i < (int) 1e6; i++) {
//            itemController.itemFromBusinessService()
            flag &= ReflectiveItemEqual(itemController.itemFromBusinessService(),cmp);
        }

        long end = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(end-start);
        if(!flag){
            logger.error("API:/item-from-business-service has failed during its test");
        }else{
            logger.info("API:/item-from-business-service has successfully passed");
        }

        logger.info("test duration(milliseconds):"+duration);
    }

    @Test
    public void testRetrieveAllItems(){
        List<Item> items = itemController.retrieveAllItems();

        boolean flag = true;
        for (int i = 0; i < items.size(); i++) {
            flag &= ReflectiveItemEqual(items.get(i),new Item(i+1,"TestItem" + i,(i+1)*10,(i+1)*20));
        }

        if(!flag){
            logger.error("API:/all-items-from-database has failed during its test");
        }else{
            logger.info("API:/all-items-from-database has successfully passed");
        }
    }

    @Test
    public void testRetrieveAllItems1e6(){
        long start = System.nanoTime();

        List<Item> list = new ArrayList<>();

        for(int i = 0;i < 5;i++){
            list.add(new Item(i+1,"TestItem" + i,(i+1)*10,(i+1)*20));
        }


        boolean flag = true;
        for (int i = 0; i < (int) 1e6; i++) {
//            itemController.retrieveAllItems()
            List<Item> items = itemController.retrieveAllItems();
            for (int j = 0; j < items.size(); j++) {
                flag &= ReflectiveItemEqual(items.get(j),list.get(j));
            }
        }

        long end = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(end-start);
        if(!flag){
            logger.error("API:/all-items-from-database has failed during its test");
        }else{
            logger.info("API:/all-items-from-database has successfully passed");
        }

        logger.info("test duration(milliseconds):"+duration);
    }
}