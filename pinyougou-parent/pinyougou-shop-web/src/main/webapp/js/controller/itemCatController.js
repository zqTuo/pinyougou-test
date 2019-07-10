var app=new Vue({
    el:"#app",
    data:{
        list:{},
        entity:{parentId:0},
        ids:[],
        entity_1:{},   //变量1
        entity_2:{},   //变量2
        grade:1,  //当前等级
        searchEntity:{},


    },
    methods: {
        /*  findByParentId: function (parenId) {
              axios.get('/itemCat/findByParentId/' + parenId + '.shtml').then(function (response) {
                  app.list = response.data;
              }).catch(function (error) {
                  console.log("wdawadawd")
              })
          },*/
        selectList: function (p_entity) {
            //    如果当前的等级是1
            if (this.grade == 1) {
                this.entity_1 = {};
                this.entity_2 = {};
            }
            if (this.grade == 2) {
                this.entity_1 = p_entity;
                this.entity_2 = {};
            }

            if (this.grade == 3) {
                this.entity_2 = p_entity;
            }
            this.findByParentId(p_entity.id)
        },

        findByParentId: function (parentId) {
            axios.get('/itemCat/findByParentId/' + parentId + '.shtml').then(function (response) {
                app.list = response.data;
                //记录下来
                app.entity.parentId = parentId;

            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        findOne: function (id) {
            axios.get('/itemCat/findOne/' + id + '.shtml').then(function (response) {
                app.entity = response.data;
            })
        },
        add: function () {
            axios.post('/itemCat/add.shtml', this.entity).then(function (response) {
                if (response.data.success) {
                    app.selectList({id: 0})
                }
            }).catch(function (error) {
                alert(error.data.message)
            })
        },
        update: function () {
            axios.post('/itemCat/update.shtml', this.entity).then(function (response) {
                if (response.data.success) {
                    app.selectList({id: 0})
                }
            })
        },
        save: function () {
            if (this.entity.id == null) {
                this.add();
            } else {
                this.update()
            }
        },
        dele: function () {
            axios.post('/itemCat/delete.shtml', this.ids).then(function (response) {
                if (response.data.success) {
                    app.selectList({id: 0})
                }
            })

        },
    },
    //钩子函数  初始化事件的和
    created:function () {

        this.selectList({id:0})
    }
})