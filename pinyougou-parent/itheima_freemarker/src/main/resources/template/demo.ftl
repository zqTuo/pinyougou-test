<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门小DEMO </title>
</head>
<body>
<#include "head.ftl"><br>
<#--我只是一个注释，我不会有任何输出  -->
${name},你好。${message} <br>
<#assign linkman="周先生">
联系人：${linkman}
<#assign info={"mobile":"13301231212",'address':'北京市昌平区王府街'} >
电话：${info.mobile}  地址：${info.address}<br>

<#list goodsList as goods>
    ${goods_index+1} 商品名称： ${goods.name} 价格：${goods.price}<br>
</#list>
共  ${goodsList?size}  条记录<br>

<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval />
开户行：${data.bank}  账号：${data.account}

当前日期：${today?date} <br>
当前时间：${today?time} <br>
当前日期+时间：${today?datetime} <br>
日期格式化：  ${today?string("yyyy年MM月")}
累计积分：${point}<br>
累计积分：${point?c}<br>
${aaa!'-'}
<#if aaa??>
aaa变量存在
<#else>
aaa变量不存在
</#if>

</body>
</html>