package com.itheima.reggie.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * String类型的数据的操作
     */
    @Test
    public void testString(){
        redisTemplate.opsForValue().set("city123","guangzhou");

        String value =(String) redisTemplate.opsForValue().get("city123");
        System.out.println(value);

        redisTemplate.opsForValue().set("key1","value1",10l, TimeUnit.HOURS);

        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "shenzhen");
        System.out.println(aBoolean);

    }

    /**
     * 操作hash数据结构
     */
    @Test
    public void testHash(){
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("002","name","xiaochen");
        hashOperations.put("002","age","18");
        hashOperations.put("002","address","gz");


        String age = (String) hashOperations.get("002", "age");
        System.out.println(age);

        //获取hash结构中的所有字段
        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        //获得hash结构中的所有值
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作List数据结构
     */
    @Test
    public void testList(){
        ListOperations listOperations = redisTemplate.opsForList();

        //存值
        listOperations.leftPush("mylist1","a");
        listOperations.leftPushAll("mylist1","b","c","d","e");

        //取值
        List<String> mylist = listOperations.range("mylist1", 0, -1);
        for (String value : mylist) {
            System.out.println(value);
        }

        //获得列表长度
        Long size = listOperations.size("mylist1");
        int lSize = size.intValue();
        for (int i = 0; i < lSize; i++) {
            String element = (String)listOperations.rightPop("mylist1");
            System.out.println(element);
        }

    }

    /**
     * 操作set类型的数据
     */
    @Test
    public void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();

        //存值
        setOperations.add("myset","a","b","c","a");

        //取值
        Set<String> myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }

        //删除成员
        setOperations.remove("myset","a","b");

        //取值
        myset= setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }

    }

    /**
     * 通用操作 针对不同的数据类型都可以操作
     */
    @Test
    public void testCommon(){
        //获取Redis中所有的key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        //判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);

        //删除指定key
        redisTemplate.delete("mylist");

        //获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType);
    }






}
