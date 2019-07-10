package com.itheima;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Test01 {
    public static void main(String[] args) throws IOException, TemplateException {

//        1.创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
//        设置模板所在的目录
        configuration.setDirectoryForTemplateLoading(new File("D:\\java_code\\pinyougou-test\\pinyougou-parent\\itheima_freemarker\\src\\main\\resources\\template"));
//                设置字符集
        configuration.setDefaultEncoding("utf-8");
//        加载模板
        Template template = configuration.getTemplate("demo.ftl");
//        创建数据类型
        Map map = new HashMap<>();
        map.put("name","张三" );
        map.put("message","欢迎来到神奇的品优购世界" );
        map.put("success", true);

        List goodsList = new ArrayList<>();
        HashMap good1 = new HashMap();
        good1.put("name","苹果" );
        good1.put("price","5.7" );

        HashMap good2 = new HashMap();
        good2.put("name","香蕉" );
        good2.put("price",2.5 );

        HashMap good3 = new HashMap();
        good3.put("name","橘子" );
        good3.put("price",3.2 );

        goodsList.add(good1);
        goodsList.add(good2);
        goodsList.add(good3);
        map.put("goodsList",goodsList );

        map.put("today",new Date());
        map.put("point", 102920122);
//        map.put("aaa",1 );

//        创建writer对象
        Writer out = new FileWriter(new File("D:\\java_code\\pinyougou-test\\pinyougou-parent\\itheima_freemarker\\src\\main\\resources\\html\\test.html"));

//        输出
        template.process(map,out);
//        关闭writer对象
        out.close();

    }
}
