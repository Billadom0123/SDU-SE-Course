package lab1.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LRU_Cache implements Cache{

    private final List<Long> keys;
    private  int capacity;
    private final HashMap<Long,Double> dict;

    public LRU_Cache(int capacity){
        dict = new HashMap<>();
        keys = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    private long gen_rk(int i,int j,int k){
        return ((long) i <<32)|((long) j <<16)|k;
    }

    @Override
    public Double get(int i, int j, int k) {
        long rk = gen_rk(i, j, k);
        if(hit(i,j,k)){
            keys.remove(rk);
            keys.add(0,rk);
        }

        return dict.get(rk);
    }

    @Override
    public void put(int i, int j, int k, double value) {
        assert !hit(i,j,k);

        long rk = gen_rk(i, j, k);
        if(keys.size()<capacity){
            keys.add(rk);
            dict.put(rk,value);
        }else{
            Long key = keys.remove(keys.size() - 1);
            dict.remove(key);

            keys.add(0,rk);
            dict.put(rk,value);
        }
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean hit(int i,int j,int k){
        return dict.get(gen_rk(i,j,k))!=null;
    }
}
