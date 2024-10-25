// 图片播放

window.addEventListener('load',function(){
    var arrowl=document.querySelector('.arrow-l')
    var arrowr=document.querySelector('.arrow-r')
    var box=this.document.querySelector('.box')
    var imglist=this.document.querySelector('.imglist ul')
    var lilist=this.document.querySelector('.lilist ul')
    var imgwith=box.offsetWidth
    var num=0
    var cir=0
    function move(obj,target,fun){
        clearTimeout(obj.timer)
        obj.timer=setInterval(function(){
            var step=(target-obj.offsetLeft)/10
            
            step= step>0 ? Math.ceil(step):Math.floor(step)
             if(obj.offsetLeft==target){
                
                clearTimeout(obj.timer)
               if(fun){
                fun()
               }
            }
                obj.style.left = obj.offsetLeft + step + 'px';
            
        },15)
    }
 
    box.addEventListener('mouseenter',function(){
        arrowl.style.display='block'
        arrowr.style.display='block'
    })
    box.addEventListener('mouseleave',function(){
        arrowl.style.display='none'
        arrowr.style.display='none'
    })
    for(var i=0;i<imglist.children.length;i++){
        var li=this.document.createElement('li')
        li.setAttribute('index',i)
        lilist.append(li)
        li.addEventListener('click',function(){
            for(var j=0;j<lilist.children.length;j++){
                lilist.children[j].className=''
            }
            this.className='current'
            var index=this.getAttribute('index')
            num=index
            cir=index
            move(imglist,-imgwith*index)
        })
    }
    lilist.children[0].className='current'
    lilist.removeChild(lilist.children[lilist.children.length-1])
    
    arrowr.addEventListener('click',function(){
        if(num==lilist.children.length){
            imglist.style.left=0 + 'px'
            num=0
        }
        num++;
        
        cir++
        if(cir==lilist.children.length){
            cir=0
        }
        move(imglist,-num*imgwith)
        change()
       
    })
    arrowl.addEventListener('click',function(){
        if(num==0){
            
            num=lilist.children.length
            imglist.style.left=-num*imgwith + 'px'
        }
        if(cir==0){
            cir=lilist.children.length
        }
        num--;
        cir--;
        move(imglist,-num*imgwith)
        change()
    })
    function change(){
        for(var i=0;i<lilist.children.length;i++){
            lilist.children[i].className=''
        }
        lilist.children[cir].className='current'
    }
    var timer=setInterval(function(){
        arrowr.click();
    },2000)
    box.addEventListener('mouseenter',function(){
        clearInterval(timer)
    })
    box.addEventListener('mouseleave',function(){
        timer=setInterval(function(){
            arrowr.click();
        },2000)
    })
    
 
 
 
})