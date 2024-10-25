package lab1.UserFile;

import lab1.Cache.Cache;

public class DummyCache implements Cache {
    public DummyCache(){
        System.out.println("This is a Dummy Cache,useless");
    }
    @Override
    public Double get(int i, int j, int k) {
        System.out.println("Dummy Cache get");
        return null;
    }

    @Override
    public void put(int i, int j, int k, double value) {
        System.out.println("Dummy Cache put");
    }

    @Override
    public void setCapacity(int capacity) {
        System.out.println("Dummy Cache set capacity");
    }
}
