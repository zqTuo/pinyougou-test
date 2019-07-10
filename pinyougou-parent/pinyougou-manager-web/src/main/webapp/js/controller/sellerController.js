var app = new Vue({
    el: '#app',
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {},
        ids: [],
        searchEntity: {status: '0'}
    },
    methods: {
        //分页查询
        searchList: function (curPage) {

            axios.post('/seller/search.shtml?pageNo=' + curPage, this.searchEntity).then(function (response) {
                //    获取数据库
                app.list = response.data.list;

                //    当前页
                app.pageNo = curPage;
                //    总页数
                app.pages = response.data.pages;
            })
        },
        finOne:function(id){
            axios.get('/seller/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },

        updateStatus:function (status) {
            axios.get('/seller/updateStatus.shtml',{
                params:{
                    sellerId:this.entity.sellerId,
                    status:status
                }
            }).then(function (response) {
                if (response.data.success) {
                    app.searchList(1)
                    app.searchEntity={};
                    app.searchEntity(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }
    },
    created: function () {
        this.searchList(1)
    }
})