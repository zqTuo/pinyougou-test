var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {},
        ids: [],
        searchEntity: {},
        searchMap: {
            'keywords': '',
            'category': '',
            'brand': '',
            spec: {},
            'price': '',
            'pageNo': 1,
            'pageSize': 40,
            'preDott': '',
            'nextDott': ''
        },//作为条件查询的对象
        resultMap: {brandList:[]}, //搜索的结果的封装对象
        pageLabels: [],//用于设置分页标签展示  页码的存储变量
        preDott: false,
        nextDott: false

    },
    methods: {
        searchList: function () {
            axios.post('/itemSearch/search.shtml', this.searchMap).then(function (response) {

                //获取数据
                //app.list=response.data.list;
                app.resultMap = response.data;
                app.buildPageLabel();
                console.log(response.data);
            });
        },
        //查询所有品牌列表
        findAll: function () {
            console.log(app);
            axios.get('/item/findAll.shtml').then(function (response) {
                alert(response)
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data;

            }).catch(function (error) {

            })
        },
        findPage: function () {
            var that = this;
            axios.get('/item/findPage.shtml', {
                params: {
                    pageNo: this.pageNo
                }
            }).then(function (response) {
                console.log(app);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data.list;
                app.pageNo = curPage;
                //总页数
                app.pages = response.data.pages;
            }).catch(function (error) {

            })
        },
        //该方法只要不在生命周期的
        add: function () {
            axios.post('/item/add.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update: function () {
            axios.post('/item/update.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save: function () {
            if (this.entity.id != null) {
                this.update();
            } else {
                this.add();
            }
        },
        findOne: function (id) {
            axios.get('/item/findOne/' + id + '.shtml').then(function (response) {
                app.entity = response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele: function () {
            axios.post('/item/delete.shtml', this.ids).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        search: function () {
            axios.post('/itemSearch/search.shtml', this.searchMap).then(function (response) {
                //    获取数据
                app.resultMap = response.data;
                //    默认获取第一个值
                console.log(response.data)
            })

        },
        addSearchItem: function (key, value) {

            if (key == 'category' || key == 'brand' || key == 'price') {
                this.searchMap[key] = value;
            }
            else {
                this.searchMap.spec[key] = value;
            }
            //发送请求
            this.searchList();

        },
        removeSearchItem: function (key) {
            if (key == 'category' || key == 'brand' || key == 'price') {
                this.searchMap[key] = '';
            } else {
                delete this.searchMap.spec[key];
            }
            this.searchList()
        },
        buildPageLabel: function () {
            this.pageLabels = [];
            //显示以当前页为中心的5个页码
            let firstPage = 1;
            let lastPage = this.resultMap.totalPages;//总页数

            if (this.resultMap.totalPages > 5) {
                //判断 如果当前的页码 小于等于3  pageNo<=3      1 2 3 4 5  显示前5页
                if (this.searchMap.pageNo <= 3) {
                    firstPage = 1;
                    lastPage = 5;
                    this.preDott = false;
                    this.nextDott = true;
                } else if (this.searchMap.pageNo >= this.resultMap.totalPages - 2) {//如果当前的页码大于= 总页数-2    98 99 100
                    firstPage = this.resultMap.totalPages - 4;
                    lastPage = this.resultMap.totalPages;
                    this.preDott = true;
                    this.nextDott = false;
                } else {
                    firstPage = this.searchMap.pageNo - 2;
                    lastPage = this.searchMap.pageNo + 2;
                    this.preDott = true;
                    this.nextDott = true;

                }
            } else {
                this.preDott = false;
                this.nextDott = false;
            }
            for (let i = firstPage; i <= lastPage; i++) {
                this.pageLabels.push(i);
            }
        },
        queryByPage: function (pageNo) {
            pageNo = parseInt(pageNo);
            this.searchMap.pageNo = pageNo
            this.searchList();
        },
        clear: function () {
            this.searchMap = {
                'keywords': this.searchMap.keywords,
                'category': '',
                'brand': '',
                spec: {},
                'price': '',
                'pageNo': 1,
                'pageSize': 40,
                'sortField': '', 'sortType': ''
            };
        },
        doSort: function (sortField, sortType) {
            this.searchMap.sortField = sortField
            this.searchMap.sortType = sortType;
            this.searchList();
        },
        isKeywordsIsBrand:function () {
            if(this.resultMap.brandList!=null && this.resultMap.brandList.length>0) {
                for (var i = 0; i < this.resultMap.brandList.length; i++) {
                    if (this.searchMap.keywords.indexOf(this.resultMap.brandList[i].text) != -1) {
                        this.searchMap.brand = this.resultMap.brandList[i].text;
                        return true;
                    }
                }
            }
            return false;
        },
        doSearch:function () {
            window.location.href="http://localhost:9104/search.html?keywords="+encodeURIComponent(this.keywords);
        }
    },
    //钩子函数 初始化了事件和
    created: function () {
        var urlParam = this.getUrlParam();
        if (urlParam.keywords !=undefined && urlParam !=null){
            this.searchMap.keywords= decodeURIComponent(urlParam.keywords);
        this.searchList();
        }
    }

})
