package com.raiden;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.CustomizeJSON;
import com.alibaba.fastjson.parser.deserializer.CustomizeParserConfig;
import com.raiden.model.Administration;
import com.raiden.model.LRUList;
import com.raiden.model.Node;
import com.raiden.model.User;
import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:39 2020/1/28
 * @Modified By:
 */
public class AppTest {

    @Test
    public void test1(){
        String str = "babad";
        System.err.println(longestPalindrome(str));
    }

    public String longestPalindrome(String s) {
        if (s.equals(""))
            return "";
        String origin = s;
        String reverse = new StringBuffer(s).reverse().toString();
        int length = s.length();
        int[] arr = new int[length];
        int maxLen = 0;
        int maxEnd = 0;
        for (int i = 0; i < length; i++)
        /**************修改的地方***************************/
            for (int j = length - 1; j >= 0; j--) {
                /**************************************************/
                if (origin.charAt(i) == reverse.charAt(j)) {
                    if (i == 0 || j == 0) {
                        arr[j] = 1;
                    } else {
                        arr[j] = arr[j - 1] + 1;
                    }
                    /**************修改的地方***************************/
                    //之前二维数组，每次用的是不同的列，所以不用置 0 。
                } else {
                    arr[j] = 0;
                }
                /**************************************************/
                if (arr[j] > maxLen) {
                    int beforeRev = length - 1 - j;
                    if (beforeRev + arr[j] - 1 == i) {
                        maxLen = arr[j];
                        maxEnd = i;
                    }

                }
            }
        return s.substring(maxEnd - maxLen + 1, maxEnd + 1);
    }

    @Test
    public void testFastJosn() throws Throwable {
        String userStr = "{\n" +
                "\t\"id\":\"I18nKey:20200411001\",\n" +
                "\t\"a\":\"I18nKey:张三\",\n" +
                "\t\"student\":\"I18nKey:高三三班\",\n" +
                "\t\"contents\":[\"I18nKey:1\",\"I18nKey:2\"]\n" +
                "}";
        Map<String, String> en = new HashMap<>();
        en.put("3", "zhangsan");
        en.put("4", "20200411001");
        en.put("5", "Class three in grade three");
        en.put("1", "Hello");
        en.put("2", "Welcome home");
        Map<String, String> zh = new HashMap<>();
        zh.put("3", "张三");
        zh.put("4", "20200411001");
        zh.put("5", "高三三班");
        zh.put("1", "你好");
        zh.put("2", "欢迎回家");
//
//        User user = JSON.parseObject(userStr, User.class, new CustomizeParserConfig(zh));
//        System.err.println(user);
        String string = "{\n" +
                "\t\"users\": [{\n" +
                "\t\t\"id\": \"I18nKey:4\",\n" +
                "\t\t\"a\": \"I18nKey:3\",\n" +
                "\t\t\"student\": \"I18nKey:5\",\n" +
                "\t\t\"url\": \"www.baidu.com\",\n" +
                "\t\t\"contents\": [\"I18nKey:1\", \"I18nKey:2\"]\n" +
                "\t}],\n" +
                "\t\"memberId\":\"2020\"\n" +
                "}";
        Administration administration = CustomizeJSON.parseObject(string, Administration.class, new CoustomizeValueMutator(zh));
        System.err.println(administration);
    }

    @Test
    public void test2(){
        LRUList<String> linkedList = new LRUList<>(5);
        for (int i = 0; i < 10; i++) {
            Node<String> node = new Node<>(String.valueOf(i), String.valueOf(i));
            linkedList.save(node);
            if (i % 2 == 0){
                System.err.println(linkedList);
            }else {
                System.out.println(linkedList);
            }
        }
        linkedList.getNode("6");
        System.err.println(linkedList);
    }
}
