package com.piperstack;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit test for jdk Map Interface, for daily use
 */
public class MapOPTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testValueGetWithNonKeyType()
    {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        assertNull(map.get(1));
        assertNotNull(map.get(String.valueOf(1)));
        Object objKey = String.valueOf(1);
        assertNotNull(map.get(objKey));
    }
}
