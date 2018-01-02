package com.xingej.zookeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    
    //将构造方法，私有化
    private PropertiesUtil(){
        
    }
    
    public static String getValue(String name){
        //1、创建文件输入流
        InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("zkconfig.properties");
        
        
        //2、创建Properties对象
        Properties properties = new Properties();
        
        //3、将文件输入流，转换成Properties对象
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return properties.getProperty(name);
    }
    
    //测试，getValue是否好用
    public static void main(String[] args) {
        System.out.println("----->:\t" + PropertiesUtil.getValue("zk_addr"));
    }
    
}
