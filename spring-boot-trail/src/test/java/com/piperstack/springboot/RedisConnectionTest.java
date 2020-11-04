package com.piperstack.springboot;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConnectionTest {
  @Autowired
  private RedisTemplate<String, String> strRedisTemplate;

  @Autowired
  private RedisTemplate<String, Serializable> serializableRedisTemplate;

  @Test
  public void testString() {
    strRedisTemplate.opsForValue().set("str-key", "str-value");
    System.out.println(strRedisTemplate.opsForValue().get("str-key"));
  }

  @Test
  @SuppressWarnings({"unchecked"})
  public void testSerializable() {
    HashMap<String, String> map = new HashMap<>();
    map.put("name", "jsy");
    map.put("age", "24");
    map.put("gender", "male");

    serializableRedisTemplate.opsForValue().set("map", map);
    Object obj = serializableRedisTemplate.opsForValue().get("map");

    assertTrue(obj instanceof HashMap);

    ((HashMap<String, String>)obj).forEach((k, v) -> {
      System.out.println(k + " => " + v);
    });
  }
}
