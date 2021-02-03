package com.piperstack;

import java.util.ArrayList;
import java.util.List;

/*
 * PECS是和泛型的上下界有关的额外主题，首先需要理解上下界
 * 可以参考Oracle的文档
 *
 * ? extends T => https://docs.oracle.com/javase/tutorial/java/generics/upperBounded.html
 * ? super T => https://docs.oracle.com/javase/tutorial/java/generics/lowerBounded.html
 *
 * 其次是上下界带来的限制，直接催生了所谓的PECS，可以参考下面链接中的Bert F的回答
 * https://stackoverflow.com/questions/4343202/difference-between-super-t-and-extends-t-in-java
 * 隐藏在这一切的背后是所谓的协变，逆变的概念，可以参考下面链接中Naman的回答
 * https://stackoverflow.com/questions/2723397/what-is-pecs-producer-extends-consumer-super
 *
 * 注意泛型的上下界其实是为了使得对应的方法参数可以更加灵活多变而设计的
 */
public class PECS {
    /**
     * 这里的list参数的类型是List<? super Father>
     * 意为所有Father及其父类比如GrandFather的List，
     * 即传入List<Father>, List<GrandFather>, List<Object>作为这个方法的参数都是可以的
     * 其实这也是下界这一机制的作用：使得传入的参数不仅仅局限于某个特定的类型，而是某个类型及其所有父类型
     * 这大大的增加了方法调用的灵活性
     * 
     * 但是正是由于传入所有Father的父类的List作为参数都可以，导致在这个方法内部根本无法判断实际传来
     * 的究竟是哪个类型，因此读取其中的数据是很难的，但是由于隐式类型转换的存在，使得往里面写入所有Father
     * 及其子类的实例都是可以的，使得其成为一个只能往里面写入但是无法往外读的所谓的数据消费者
     * 基于以上的使用<? super T>的使其成为消费者的特性，使其获得了Consumer super的称谓，
     * 即PECS中的CS    
     */
    void lowerBound(List<? super Father> list) {
        for (int i = 0; i < 3; i++) {
            list.add(new Father());
            list.add(new Son()); // 这里实际上有一个隐式类型转换，即(Father)(new Son())
        }
    }

    /**
     * 这里的list的参数类型是List<? extends Father>
     * 意为所有Father及其子类比如Son的List
     * 即传入List<Father>和List<Son>作为这个方法的参数都是可以的
     * 其实这也是上界这一机制的作用：使得传入的参数不仅仅局限于某个类型，而是某个类型及其所有子类型
     * 这也是用于增加方法调用时的灵活性
     * 但是正是由于传入所有Father及其子类的List作为参数都可以，导致在这个方法内部也无法判断实际传来
     * 的究竟是哪个类型，因此往里面写入任何一个类型都是难以做到的，比如打算往里面写入一个Father，但实际传入的
     * 是个List<Son>，当你打算写入一个Son，实际传入的是List<GrandSon>，面对这种不确定型，往里面写数据是不可能的
     * 但是，不管传入的实际是个啥类型，可以确定的是该类型是Father及其子类型，都可以作为Father看待
     * 所以其读出来的类型是可以确定的，大不了隐式类型转换为父类损失点东西，但是读出来是没问题的
     * <? extends T>使得该类型是T的子类型，而且使得读出来的所有都可以是作为T，使其成为一个所谓的生产者
     * 即Producer extends，即PECS中的PE    
     */
    List<Father> upperBound(List<? extends Father> list) {
        List<Father> res = new ArrayList<Father>();
        for (Father father : list) {
            res.add(father);
        }

        return res;
    }

    /**
     * 其实下面的方法就是 {@link java.util.Collections#copy}，一个特定类型T及其子类型作为生产者
     * T及其父类型作为消费者，实现一个拷贝
     */
    <T> void copy(List<? extends T> producer, List<? super T> consumer){
        for (T t : producer) {
            consumer.add(t);
        }
    }
}

class GrandFather {}
class Father extends GrandFather {}
class Son extends Father{}
