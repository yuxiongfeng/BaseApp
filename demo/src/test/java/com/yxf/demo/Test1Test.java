package com.yxf.demo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: yxf
 * @CreateDate: 2020/4/7 15:51
 * @UpdateUser: yxf
 * @UpdateDate: 2020/4/7 15:51
 */
public class Test1Test {
    Test1 test1;

    @Before
    public void setUp() {
        test1 = new Test1();
    }

    @Test
    public void add() {
//        Assert.assertEquals(4, test1.add(2, 3), 0);
        Assert.assertEquals(true,test1.add(2,4)==6);
    }

    @Test
    public void sub() {
//        Assert.assertEquals(4, test1.add(2, 3), 0);
        Assert.assertEquals(true,test1.sub(4,3)==1);
    }


    //判断是否是空白字符
    @Test
    public void judgeIsWhiteSpace(){
        System.out.println(Character.isWhitespace('c'));
        System.out.println(Character.isWhitespace(' '));
        System.out.println(Character.isWhitespace('\n'));
        System.out.println(Character.isWhitespace('\t'));
    }
}