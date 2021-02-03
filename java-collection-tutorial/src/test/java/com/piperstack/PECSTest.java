package com.piperstack;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 */
public class PECSTest {
    private PECS pecs;

    @Before
    public void setup() {
        pecs = new PECS();
    }

    @Test
    public void testProducer() {
        List<Father> fathersProducer = new ArrayList<Father>();
        fathersProducer.add(new Father());
        List<Son> sonsProducer = new ArrayList<Son>();
        sonsProducer.add(new Son());

        // 这里可以既可以传入List<Father>也可以传入List<Son>，体现了灵活性
        // 传入之后作为一个生产者，下面写成这样这里纯粹就是为了搞一个assert满足单元测试而已
        // 其实只要编译能过，说明我们对PECS的使用就没有问题了
        assertEquals(pecs.upperBound(fathersProducer).size(), 1);
        assertEquals(pecs.upperBound(sonsProducer).size(), 1);
    }

    @Test
    public void testConsumer() {
        List<Father> fatherConsumer = new ArrayList<Father>();
        List<GrandFather> grandFatherConsumer = new ArrayList<GrandFather>();

        // 这里既可以传入List<Father>，又可以传入List<GrandFather>，体现了灵活性
        // 传入之后作为一个消费者，去接收数据
        pecs.lowerBound(fatherConsumer);
        pecs.lowerBound(grandFatherConsumer);

        assertEquals(fatherConsumer.size(), 6);
        assertEquals(grandFatherConsumer.size(), 6);
    }
}
