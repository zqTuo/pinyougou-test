var app = new Vue({
    el: "#app",
    data: {
        num:1,//商品的购买数量
        specificationItems:JSON.parse(JSON.stringify(skuList[0].spec)),//定义一个变量用于存储规格的数据
       //数组第一个元素的就是sku的数据展示
        item:skuList[0]
    },
    methods: {
        addNum:function(num){
            num = parseInt(num);  //转成json字符串
            this.num+=num;//加或者减
            if(this.num<=1){
                this.num=1;
            }
        },
        selectSpecifcation:function(name,value){
        //    设置值
            this.$set(this.specificationItems,name,value);
        //    调用搜索方法
            this.search()
        },
        isSelected:function(name,value){
            if (this.specificationItems[name]==value){
                return true;
            }else {
                return false
            }
        },
    //    方法 点击选项的时候调用（用于判断当前的变量，specificationItems 和sku列表中的变量的值是否一致）
    //    如果一致 需要将sku变量绑定到一致的那个对象
        search:function () {
            for (var i=0;i<shuList.length;i++){
                var object = skuList[i];
                if (JSON.stringify(this.specificationItems)==JSON.stringify(skuList[i].spec)){
                console.log(object);
                this.item=object;
                    break;
                }
            }
        }
    },

    //钩子函数 初始化了事件和
    created: function () {

    }

})