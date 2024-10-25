package lab1.UserFile;

import lab1.Cache.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FIFO_Cache implements Cache {
    private final List<Long> keys;
    private  int capacity;
    private final HashMap<Long,Double> dict;

    public FIFO_Cache() {
        keys = new ArrayList<>();
        capacity = 100;
        dict = new HashMap<>();
    }

    private long gen_rk(int i,int j,int k){
        return ((long) i <<32)|((long) j <<16)|k;
    }

    @Override
    public Double get(int i, int j, int k) {
        long rk = gen_rk(i, j, k);

        return dict.get(rk);
    }

    @Override
    public void put(int i, int j, int k, double value) {
        assert !hit(i,j,k);

        long rk = gen_rk(i,j,k);
        if (keys.size() >= capacity) {
            Long key = keys.remove(0);
            dict.remove(key);

        }
        keys.add(rk);
        dict.put(rk,value);
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean hit(int i,int j,int k){
        return dict.get(gen_rk(i,j,k))!=null;
    }
}
