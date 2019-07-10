var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{},
        ids:[],
        smsCode:'',
        searchEntity:{}
    },
    methods: {

        //该方法只要不在生命周期的
        add:function () {
            axios.post('/user/add/'+this.smsCode+'.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                //    跳转到其用户后台的首页
                    window.location.href="home-index.html";  //跳转到用户中心首页
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        createSmsCode:function () {
            axios.get('/user/sendCode.shtml?phone='+this.entity.phone).then(function (response) {
                if (response.data.success) {
                    alert(response.data.message);//显示数据

                }else {
                //    发送失败

                    alert(response.data.message);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        formSubmit:function () {
            var that = this;
            this.$validator.validate.then(function (result) {
                if (result){
                    console.log(that)
                    axios.post('/user/add/'+that.smsCode+'.shtml',that.entity).then(function (response) {
                        if (response.data.success){
                        //    跳转到其用户后台的首页
                            window.location.href="home-index.html";
                        }else {
                            that.$validator.errors.add(response.data.errorsList)
                        }
                    }).catch(function (error) {
                        console.log("1231312131321");
                    });
                }
            })

        }

    },
    //钩子函数 初始化了事件和
    created: function () {
      


    }

})
