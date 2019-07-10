var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity:{customAttributeItems:[]},//初始化
        brandOptions: [],  //绑定品牌列表下拉框数据   品牌描述
        specOptions: [{id: 1, text: "机身内存"}],// 绑定规格数据列表   规格描述
        ids:[],
        searchEntity: {}
    },
    methods: {
        //    查询所有品牌列表
        findAll: function () {
            console.log(app);
            axios.get('/typeTemplate/findAll.shtml').then(function (response) {
                console.log(response);
                //    注意  this在axios中就不在是vue实例了。
                app.list = response.data
            })
        },
        searchList: function (curPage) {
            axios.post('/typeTemplate/search.shtml?pageNo='+ curPage, this.searchEntity).then(function (response) {
                //获取数据
                app.list = response.data.list;
                //获取总页数
                app.pages = response.data.pages;
                // 获取当前页数
                app.pageNo = curPage
            })
        },
        //页面加载的时候调用，发送请求，获取所有的品牌列表数据  赋值个变量brandOptions 要求：{id：text}
        findBrandIds: function () {
            axios.get('/brand/findAll.shtml').then(function (response) {
                    //response.data[{id,name,firstchar}]
                    //我们需要 {id：1，text：‘联想’}，{id：2，text：‘华为’}
                    for (var i = 0; i < response.data.length; i++) {
                        var obj = response.data[i];   //{id;name.text:firstChar}
                        app.brandOptions.push({"id": obj.id, "text": obj.name})
                    }
                }
            )
        },
        //页面加载的时候调用，发送请求 获取所有的规格的数据列表，赋值给变量 specOptions 要求 {id： text}
        findSpecList: function () {
            axios.get('/specification/findAll.shtml').then(function (response) {
                //response.data={id,specName} 获取的是 规格列表
                //同样我们需要的是{id：1，text：机身内存}
                for(var i =0;i<response.data.length;i++){
                var obj = response.data[i];
                app.specOptions.push({"id":obj.id,"text":obj.specName});
            }
            })


        },

        jsonToString: function (list, key) {
            //    用户循环遍历  获取对象中的属性值，拼接字符串  返回
            var jsonObj = JSON.parse(list);
            //  循环遍历
            var str = "";
            for (var i = 0; i < jsonObj.length; i++) {
                str += jsonObj[i][key] + ",";   // 就是获取{id,text}
            }
            if (str.length > 0) {
                str = str.substring(0, str.length - 1)
            }
            // 获取里面的text文本的值 通过逗号分割返回
            return str;
        },
        findOne:function(id){
            axios.get('/typeTemplate/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
          app.entity.brandIds=JSON.parse(app.entity.brandIds);
          app.entity.specIds=JSON.parse(app.entity.specIds)
          app.entity.customAttributeItems=JSON.parse(app.entity.customAttributeItems)
            })
       /*     1。讲字符串个转成json 赋值给原来的变量
          *   json.pare（）将字符串转成json对象
          *    json。String（json）对象，将json对象转成字符串
          *    var stringify=json.stringify(app.entity.brandIds)
          * */

        },
        //增加
        add:function(){
            axios.post('/typeTemplate/add.shtml',this.entity).then(function (response) {
                console.log(response)
                if (response.data.success){
                    app.searchList(1)
                }
            }).catch(function (error) {
                console.log("dawdwd")
            })
        },
        //修改方法
        update:function(){
            axios.post('/typeTemplate/update.shtml',this.entity).then(function (response) {
                if (response.data.success){
                    app.searchList(1)
                }
            })
        },
        save:function(){
            if (this.entity.id!=null){
                this.update();
            } else {
                this.add()
            }
        },
        dele:function(){
          axios.post('/typeTemplate/delete.shtml',this.ids).then(function (response) {
              if (response.data.success){
                  app.searchList(1)
              }
          })
        },


        //向数组中添加js对象
        addTableRow:function () {
            this.entity.customAttributeItems.push({});
        },
        //向数组中删除js对象
        removeTableRow:function (index) {
            this.entity.customAttributeItems.splice(index,1)
        }

    },
    //钩子函数  初始化事件
    created: function () {

        this.searchList(1);
        this.findBrandIds();
        this.findSpecList();
        this.findAll()

    }
})