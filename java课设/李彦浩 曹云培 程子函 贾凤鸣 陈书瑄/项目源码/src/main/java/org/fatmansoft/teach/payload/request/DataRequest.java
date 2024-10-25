package org.fatmansoft.teach.payload.request;

import java.util.*;

//用于存储前端请求传入的参数， 并提供了提取参数的方法
public class DataRequest {
    private Map data;

    public DataRequest() {
        data = new HashMap();
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public void add(String key, Object obj){
        data.put(key,obj);
    }

    public Object get(String key){
        return data.get(key);
    }

    //提取字符串型参数
    //参数传递一个字符串key
    public String getString(String key){
        //指定这个字符串key，获取当前DRequest对象中的以key为键的键值对的值，当然这个值不知道是什么类型的，先用Object接收
        Object obj = data.get(key);
        //如果是空的,直接返回空
        if(obj == null)
            return null;
        //如果obj属于String 强转String
        if(obj instanceof String)
            return (String)obj;
        //强转不了就调那个类里的toString方法
        return obj.toString();
    }

    public Boolean getBoolean(String key){
        Object obj = data.get(key);
        if(obj == null)
            return false;
        if(obj instanceof Boolean)
            return (Boolean)obj;
        if("true".equals(obj.toString()))
            return true;
        else
            return false;
    }

    public List getList(String key){
        Object obj = data.get(key);
        if(obj == null)
            return new ArrayList();
        if(obj instanceof List)
            return (List)obj;
        else
            return new ArrayList();
    }

    //提取Map集参数
    //传递一个字符串进来，指定了键为key
    public Map getMap(String key){
        //获取当前对象属性中的以key为键的键值对的值，当然 不知道它是个什么类型的，用Object接收
        Object obj = data.get(key);
        //如果为空，直接返回一个空的哈希Map集
        if(obj == null)
            return new HashMap();
        //如果obj属于Map 强转Map返回
        if(obj instanceof Map)
            return (Map)obj;
        //不属于就返回一个空的Map集
        else
            return new HashMap();
    }

    //提取整数型参数
    //传递一个字符串key进来，指定了键为key
    public Integer getInteger(String key) {
        //获取当前键值对中以key为键的键值对的值，当然不知道它是个什么类型的，用Object接收
        Object obj = data.get(key);
        //如果为空，直接返回空
        if(obj == null)
            return null;
        //如果属于Integer，强转Integer
        if(obj instanceof Integer)
            return (Integer)obj;

        //两者都不是，调用该类的toString()
        String str = obj.toString();
        try {
            return new Integer(str);
        }catch(Exception e) {
            return null;
        }
    }

    public Long getLong(String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Long)
            return (Long)obj;
        String str = obj.toString();
        try {
            return new Long(str);
        }catch(Exception e) {
            return null;
        }
    }

    public Double getDouble(String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Double)
            return (Double)obj;
        String str = obj.toString();
        try {
            return new Double(str);
        }catch(Exception e) {
            return null;
        }
    }

    public Date getDate(String key) {
        return null;
    }

    public Date getTime(String key) {
        return null;
    }

}
