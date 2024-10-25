export default{
    map:{},
    $emit(name,params){
        if(this.map[name]!=null){
            for(let work of this.map[name]){
                if(work!=null){
                    work(params);
                }
            }
        }else{
            console.warn("正在触发无效的监听事件，事件名称为："+name);
        }
    },
    $on(name,work){
        if(this.map[name]==null)this.map[name]=[];
        this.map[name].push(work);
    },
}