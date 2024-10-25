package org.fatmansoft.teach.payload.response;

import java.util.Map;

//用于存储返回前端数据的对象
public class DataResponse {
    private String code;
    private Map data;

    /*
    //返回一个数据相应类的对象
    public static DataResponse getReturnData(Object obj, String msg){
        //初始化一个映射集
        Map data = new HashMap();
        //两个键值对 第一个<data 参数列表里的对象> 第二个 <msg 参数列表里的字符串>
        data.put("data",obj);
        data.put("msg",msg);

        //返回一个DResponse对象 第一个参数:"0" 第二个参数:本方法内更新的映射集
        return new   DataResponse("0",data);
    }
     */

    //为什么要在前面传个String参数
    public DataResponse(String code, Map data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
