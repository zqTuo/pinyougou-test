var app = new Vue({
    el: "#app",
    data: {
        list: [],  // 数组
        entity: {},
        ids:[],
        pages: 15, //总页数
        pageNo: 1, //当前页码
        searchEntity:{ }  //搜索条件对象
    },
    methods: {
        //查询所有品牌列表
        findAll: function () {
            //发送请求，过去列表的数据，赋值给变量
            axios.get('/brand/findAll.shtml').then(
                function (response) {
                    app.list = response.data;
                })
        },
        //分页查询
        searchList: function (curPage) {

            axios.post('/brand/findPage.shtml?pageNo=' + curPage,this.searchEntity).then(function (response) {
                //    获取数据库
                app.list = response.data.list;

                //    当前页
                app.pageNo = curPage;
                //    总页数
                app.pages = response.data.pages;
            })
        },
        add: function () {
            axios.post('/brand/add.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1)
                }
            }).catch(function (error) {
                console.log("1565615 ")
            })
        },
        update:function () {
            axios.post('/brand/update.shtml',this.entity).then(function (response) {
                console.log(response)
                if (response.data.success){
                    app.searchList(1)
                }
            }).catch(function (erroe) {
                console.log("15616")
            })
        },
        findOne:function (id) {
            axios.post('/brand/findOne.shtml?id='+id).then(function (response) {
                 app.entity = response.data;
            }).catch(function (error) {
                console.log("16516")
            })
        },
        save:function () {
            if (this.entity.id!=null){
                this.update()
            } else {
                this.add();
            }
        },
        dele:function () {
            axios.post('/brand/delete.shtml',this.ids).then(function (response) {
                if (response.data.success) {
                    app.searchList(1)
                }
            }).catch(function (error) {
                console.log("1651")
            })
        }
    },


    //	钩子函数
    created: function () {

        this.searchList(1);
    }

});