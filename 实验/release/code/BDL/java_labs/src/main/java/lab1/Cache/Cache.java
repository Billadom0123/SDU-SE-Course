package lab1.Cache;

public interface Cache {
    Double get(int i,int j,int k);
    void put(int i,int j,int k,double value);
    void setCapacity(int capacity);
}
