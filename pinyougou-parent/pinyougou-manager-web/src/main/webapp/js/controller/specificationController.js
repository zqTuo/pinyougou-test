var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {specification: {}, optionList: [{}, {}, {}]},

        searchEntity: {},  ids: [],
    },
    methods: {
        searchList: function (curPage) {
            axios.post('/specification/search.shtml?pageNo=' + curPage, this.searchEntity).then(function (response) {

                //    获取数据
                app.list = response.data.list;
                //    当前页数
                app.curPage = curPage;
                //    总页数
                app.pages = response.data.pages;

            })
        },
        //查询所有品牌列表
        findPage: function (curPage) {
            axios.post('/specification/findPage.shtml?pageNo=' + curPage).then(function (response) {
                //获取数据
                app.list = response.data.list
                //获取当页的数据
                app.pageNo = curPage
                //    总页数
                app.pages = response.data.pages
            }).catch(function (error) {

            })
        },
        //查询单个数据
        findOne:function(id){
            axios.post('/specification/findOne/'+id+'.shtml').then(function (response) {
            //    获取单个数据
                app.entity= response.data   //组合对象
            })
        },
        //该方法只要不再生命周期
        add:function(){
            axios.post('/specification/add.shtml',this.entity).then(function (response) {
                if (response.data.success) {
                    app.searchList(1)
                }
            }).catch(function (error) {
                console.log("awdnawj")
            })
        },
        //修改
        update:function(){
          axios.post('/specification/update.shtml',this.entity).then(function(response){
              if (response.data.success) {
                  app.searchList(1)
              }
          }).catch(function (error) {
              console.log("awdba")
          })
        },
        //判断该方法是保存还是修改
        save:function(){
            if (this.entity.specification.id!=null) {
                this.update()
            }else {
                this.add()
            }
        },
        //删除
        dele:function(){
          axios.post('/specification/delete.shtml',this.ids).then(function (response) {
            console.log(response)
              if (response.data.success) {
                  app.searchList(1)
              }
          }).catch(function(error){
              console.log("awdna")
            })
        },


        //方法  当点击新增按钮的时候调用，向已有的数组添加一个{}
        addTableRow: function () {
            //向数组添加一个对象【1,2，3】
            this.entity.optionList.push({})
        },
        //方法 点击删除按钮的时候向已有的数组中删除对应的那个{}
        removeTableRow: function (index) {
            //第一个参数：指定的要删除的元素的索引（下标）
            //第二个参数：要删除个个数
            this.entity.optionList.splice(index, 1)
        }

    },
    //钩子函数，初始化事件
    created: function () {
        this.searchList(1);
    }

})