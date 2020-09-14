package com.piperstack;

import java.util.Map;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;

public class ObjectSizeTest {

    @Test
    public void testObjectSize() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "jsy");
        properties.put("gender", "male");
        properties.put("location", "chengdu");
        properties.put("nonsense", "chengdu");

        List<Long> neighbors = new ArrayList<>();
        neighbors.add(1L);
        neighbors.add(2L);
        neighbors.add(3L);
        neighbors.add(4L);

        properties.put("neighbors", neighbors);

        Vertex v = new Vertex(22L, properties);
        String size = RamUsageEstimator.humanSizeOf(v);
        System.out.println(size);

        System.out.println(RamUsageEstimator.sizeOf("HelloWorld"));
        System.out.println(RamUsageEstimator.sizeOf(""));
        System.out.println("size of empty hashmap create by new HashMap() => "  + RamUsageEstimator.sizeOf(new HashMap<>()));
        System.out.println("size of empty arraylist create by new ArrayList<>() => " + RamUsageEstimator.sizeOf(new ArrayList<>()));
        System.out.println("size of empty string  => " + RamUsageEstimator.sizeOf(""));
        System.out.println("size of Integer => " + RamUsageEstimator.sizeOf(Integer.valueOf(0)));
        System.out.println("size of Long => " + RamUsageEstimator.sizeOf(Long.valueOf(0L)));
        assertTrue(true);
    }

    static class Vertex {
        private long id;

        private Map<String, Object> properties;

        public Vertex(long id, Map<String, Object> properties) {
            this.id = id;
            this.properties = properties;
        }
    }
}
