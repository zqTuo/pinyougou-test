var app =new Vue({
    el:'#app',
    data:{
        pages:15,
        pageNo:1,
        list:[],
        entity:{id:"adwa"},
        ids:[],
        searchEntity:{}
    },
    methods:{
    //    增加seller
        add:function () {
            axios.post('/seller/add.shtml',this.entity).then(function (response) {
                if (response.data.success){
                    alert(response.data.message);
                //    跳转到登录页面
                    location.href='/shoplogin.html';
                }else {
                    alert(response.data.message)
                }

            })

        }
    },created:function () {

    }
})