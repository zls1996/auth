var ret=document.getElementById('ret');
var s=setInterval(function(){
    if(ret.innerText<=0){
        clearInterval(s);
        location.assign('/login');
    }
    ret.innerText-=1;
},1000);