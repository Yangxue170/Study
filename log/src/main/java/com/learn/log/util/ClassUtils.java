package com.learn.log.util;



import java.util.Map;
import com.google.common.collect.Maps;
import org.apache.dubbo.common.utils.StringUtils;

/**
 * 工具类
 * @author
 *
 */
public class ClassUtils {

    private static Map<String, String> acronymPackageMap = Maps.newConcurrentMap();

    /**
     * 获取首字母包名
     * @param interfaceName 包名+类名
     * @return 首字母包名+类名
     */
    public static String getAcronymPackage(String interfaceName) {
        if (StringUtils.isBlank(interfaceName)) {
            return interfaceName;
        }
        String s = acronymPackageMap.get(interfaceName);
        if (StringUtils.isBlank(s)) {
            s = "";
            String[] strs = StringUtils.split(interfaceName, '.');
            for (int i = 0; i < strs.length; i++) {
                if (i == strs.length - 1) {
                    s += strs[i];
                } else {
                    s += strs[i].substring(0, 1) + ".";
                }
            }
            acronymPackageMap.put(interfaceName, s);
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(ClassUtils.getAcronymPackage(ClassUtils.class.toString()));
    }
}
