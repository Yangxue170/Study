package com.learn.log.dubbo;

import com.alibaba.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Filter;

import java.util.List;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/10 18:44
 */
public class Test {
    public static void main(String[] args){
        ExtensionLoader extensionLoader = ExtensionLoader.getExtensionLoader(Filter.class);

        URL url=new URL("","",3);
        url=url.addParameter("filterValue", "test5");
        List list=extensionLoader.getActivateExtension(url,"", Constants.PROVIDER);
        System.out.println(list);
        list=extensionLoader.getActivateExtension(url,"",Constants.CONSUMER);
        System.out.println(list);
        list=extensionLoader.getActivateExtension(url,"filterValue",Constants.CONSUMER);
        System.out.println(list);
    }
}
