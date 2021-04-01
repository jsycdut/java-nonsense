package com.piperstack;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 * {@link java.util.LinkedHashMap}在内部使用了一个双向链表来维护所有数据节点的一个先后顺序，在对其进行迭代时，将会
 * 对双向链表从头到尾进行依次迭代，一般而言，双向链表的元素的顺序就是调用put时对应插入元素的顺序，但是也允许在调用get时
 * 调整这个顺序，具体行为由accessOrder这个布尔变量值进行控制，
 * 当为false时，按插入序，即put序，put重复的key以第一次put的为准
 * 为true时，put和get都会引起变动，具体表现为，将put和get的节点从原有链表位置删除，并重新追加到链表尾部，若是put的key之前
 * 不存在，直接扔链表末尾，若是get的key不存在，则不对链表做任何操作
 *
 * {@link java.util.LinkedHashMap}可以作为LRU-Cache，即least recently used cache，最近最少使用缓存
 * 具体实现方式为
 *
 * 1. 写个class LRUCache 继承{@link java.util.LinkedHashMap}
 * 2. 在构造器中设置{@code accessOrder}为true并设置缓存容量
 * 2. 重写，即override {@link java.util.LinkedHashMap#removeEldestEntry(Map.Entry)}
 *    当LRU-Cache中的元素个数即将大于既定容量时，返回true，表示移除最老的元素（即最近最少使用的）
 *
 * 讲解 {@link java.util.LinkedHashMap} http://cmsblogs.com/?p=4733
 * 
 * {@link java.util.LinkedHashMap}实现这些操作的妙处在于其父类{@link java.util.HashMap}留出了很多钩子方法，
 * 比如{@link java.util.HashMap#afterNodeAccess(HashMap.Node)}，这几个方法都叫afterXXX，值得留意
 */
public class LinkedHashMapTest {

    // TODO 编写对应的可视化顺序展示
    @Test
    public void testAccessOrder() {
        // 限定accessOrder为true
        // 即put，get都会将对应的Entry从原有位置移除，并放到LinkedHashMap的List的最后
        LinkedHashMap<Integer, Double> linkedHashMap = new LinkedHashMap<>(16, 0.75F, true);
        linkedHashMap.put(1, 1.0d);
        linkedHashMap.put(2, 2.0d);
        linkedHashMap.put(3, 3.0d);
        linkedHashMap.get(2);
        linkedHashMap.get(1);

        // 此时的访问顺序应该为3 2 1
        int[] visitedOrder = new int[3];
        visitedOrder[0] = 3;
        visitedOrder[1] = 2;
        visitedOrder[2] = 1;
        int index = 0;
        for (Map.Entry<Integer, Double> entry : linkedHashMap.entrySet()) {
            int currentVisitedKey = entry.getKey();
            System.out.println(currentVisitedKey);
            // 比较double值需要指定误差范围
            assertEquals(currentVisitedKey, visitedOrder[index++]);
        }
    }

    @Test
    public void testInsertionOrder() {
        // 限定accessOrder为false
        // 即只有put操作会影响内部的Entry排序，put操作的新元素对应的Entry将会LinkedHashMap的List的最后
        // LinkedHashMap
        LinkedHashMap<Integer, Integer> linkedHashMap = new LinkedHashMap<>(16, 0.75F, false);
        linkedHashMap.put(1, 1);
        linkedHashMap.put(2, 2);
        linkedHashMap.put(3, 3);
        linkedHashMap.put(2, 2);
        linkedHashMap.get(2);
        int[] insertionOrder = new int[3];
        insertionOrder[0] = 1;
        insertionOrder[1] = 2;
        insertionOrder[2] = 3;

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey());
            assertEquals((int)entry.getKey(), insertionOrder[index++]);
        }
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationException(){
        // 注意，构造器中accessOrder为true，即说明除了put之外，get也会改变链表元素的位置
        // 下方get和put操作后面的注释为内部链表中Entry的顺序，由于Entry中key和value相同，仅用key表示
        LinkedHashMap<Integer, Integer> linkedHashMap = new LinkedHashMap<>(16, 0.75F, true);
        linkedHashMap.put(1, 1);  // 1
        linkedHashMap.put(2, 2);  // 1 2
        linkedHashMap.put(3, 3);  // 1 2 3
        linkedHashMap.get(1);     // 2 3 1
        linkedHashMap.put(2, 2);  // 3 1 2

        // 这是增强的for循环，本质上是迭代器（javap 或者反编译看代码）
        for (int key : linkedHashMap.keySet()) {
            // 在构建LinkedHashMap时，在accessOrder为true的情况下get操作将会改变链表结构
            // 导致Map内部表示结构变化的modCount变量自增
            // 迭代器在接下来next的时候会检查自己的modCount
            // 这个modCount是创建迭代器的时候赋值在迭代器内部的
            // 如果迭代器自己的modCount和Map内部的modCount不等的时候
            // 就会抛出ConcurrentModificationException
            // 下面访问第一个元素时
            System.out.println(linkedHashMap.get(key));
        }
    }
}
