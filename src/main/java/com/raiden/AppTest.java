package com.raiden;

import com.alibaba.fastjson.parser.deserializer.CustomizeJSON;
import com.raiden.model.*;
import com.raiden.util.SnowFlakeUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public String longestPalindrome2(String s) {
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
                "\t\"memberId \":\"2020\"\n" +
                "}";
        Map map = CustomizeJSON.parseObject(string, CustomizeMap.class);
        System.err.println(map);
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

    @Test
    public void test3() throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Peple.class;
        Peple p = new Peple();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods){
            System.out.println(method.getName());
            Object[] args = new Object[0];
            Object invoke = method.invoke(p, args);
            System.out.println(invoke);
        }
    }

    @Test
    public void test4(){
        String str = "aaaa";
        System.err.println(longestPalindrome(str));
    }
    public String longestPalindrome(String s){
        if (s == null){
            return s;
        }
        int length = s.length();
        if (length == 0 || length == 1){
            return s;
        }
        char[] chars = s.toCharArray();
        if (length == 2 && chars[0] == chars[1]){
            return s;
        }
        int maxlen = 1;
        int index = 0;
        int right;
        int left;
        for (int i = 0,n = chars.length; i < n; i++) {
            right = i + 1;
            left = i - 1;
            while (left > -1 && right < n && chars[left] == chars[right]){
                if (right - left + 1 > maxlen){
                    maxlen = right - left + 1;
                    index = left;
                }
                left--;
                right++;
            }
            right = i + 1;
            left = i;
            while (left > -1 && right < n && chars[left] == chars[right]){
                if (right - left + 1 > maxlen){
                    maxlen = right - left + 1;
                    index = left;
                }
                left--;
                right++;
            }
        }
        return s.substring(index, index + maxlen);
    }

    @Test
    public void test5(){
        String str = "PAYPALISHIRING";
        System.err.println(convert2(str, 3));
        assert "PAHNAPLSIIGYIR".equals(convert2(str, 3));
    }

    public String convert1(String s, int numRows) {
        if (numRows == 1){
            return s;
        }
        int length = s.length();
        if (s == null || length < numRows){
            return s;
        }
        char[] cs = s.toCharArray();
        char[] chars = new char[length];
        int size = 0;
        for (int i = 0; i < numRows; i++) {
            int sign = 0;
            int index;
            while ((index = sign * (numRows - 1) + i) < length){
                chars[size++] = cs[index];
                if (i != 0 && i != numRows - 1 && (index = index + ((numRows - 1 - i) << 1)) < length){
                    chars[size++] = cs[index];
                }
                sign+=2;
            }
        }
        return new String(chars);
    }

    public String convert2(String s, int numRows) {
        if (numRows == 1){
            return s;
        }
        int length = s.length();
        if (s == null || length < numRows){
            return s;
        }
        StringBuilder[] builders = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) {
            builders[i] = new StringBuilder(i == 0 ? length : length / numRows + 1);
        }
        int index = 0;
        boolean sign = true;
        for (int i = 0; i < length; i++) {
            builders[index].append(s.charAt(i));
            if (index == 0){
                sign = true;
            }else if (index == numRows - 1){
                sign = false;
            }
            if (sign){
                index++;
            }else {
                index--;
            }
        }
        StringBuilder builder = builders[0];
        for (int i = 1; i < numRows; i++) {
            builder.append(builders[i]);
        }
        return builder.toString();
    }


    @Test
    public void test6(){
        String[] str = {"1","2","3"};
        String[] s = new String[5];
        System.arraycopy(str, 0, s, 0, 3);
        for (String sss : s) {
            System.err.println(sss);
        }
    }

}
