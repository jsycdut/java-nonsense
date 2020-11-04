package com.piperstack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        System.out.println(map.get(1));
        assertTrue( true );
    }
}
