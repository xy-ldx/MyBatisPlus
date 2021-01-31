package com.ldx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldx.mapper.UserMapper;
import com.ldx.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MybatisPlus01ApplicationTests {

    //继承了BaseMapper,所有的方法都来自于自己的父类
    //我们也可以编写自己的扩展方法
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        //参数是一个wrapper,条件构造器，这里我们先不用 null
        //查询全部用户
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }


    //测试插入
    @Test
    public void testInsert() {
        User user = new User();
        user.setName("ldx学习mybatis_plus");
        user.setAge(30);
        user.setEmail("2460273221@qq.com");

        int result = userMapper.insert(user);//帮我们自动生成id
        System.out.println(result);//受影响的行数
        System.out.println(user);//发现，id会自动回填
    }


    //测试更新
    @Test
    public void testUpdate() {
        User user = new User();
        //通过条件自动拼接动态SQL
        user.setId(1352994810700046341L);
        user.setName("mybatis_plus学习");
        user.setAge(1);

        //注意：updateById 但是参数是一个对象
        int i = userMapper.updateById(user);
        System.out.println(i);

    }

    //测试乐观锁成功！
    @Test
    public void testOptimisticLocker() {
        //1.查询用户信息
        User user = userMapper.selectById(1L);
        //2.修改用户信息
        user.setName("ldx");
        user.setEmail("lidexin929@163.com");
        //3.执行更新操作
        userMapper.updateById(user);
    }


    //测试乐观锁失败！ 多线程下
    @Test
    public void testOptimisticLocker2() {

        //线程1
        User user = userMapper.selectById(1L);
        user.setName("ldx111");
        user.setEmail("lidexin929@163.com");

        //模拟另一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("ldx222");
        user2.setEmail("lidexin929@163.com");
        userMapper.updateById(user2);

        //自旋锁来多次尝试提交！
        userMapper.updateById(user);//如果没有乐观锁就会覆盖插队线程的值！
    }

    //测试查询
    @Test
    public void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    //测试批量查询
    @Test
    public void testSelectByBatchId() {
        //SELECT id,name,age,email,version,create_time,update_time FROM user WHERE id IN ( ? , ? , ? )
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    //按条件查询之一使用map操作
    @Test
    public void testMap() {
        HashMap<String, Object> map = new HashMap<>();
        //自定义要查询
        //SELECT id,name,age,email,version,create_time,update_time FROM user WHERE name = ? AND age = ?
        map.put("name","mybatis_plus学习");
        map.put("age",1);

        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    //测试分页查询
    @Test
    public void  testPage() {
        //参数一：当前页
        //参数二：页面大小
        //使用了分页插件之后，所有的分页操作也变得很简单
        Page<User> page = new Page(2,5);
        userMapper.selectPage(page,null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }

    //测试删除
    @Test
    public void testDeleteById() {
        // DELETE FROM user WHERE id=?
        userMapper.deleteById(1L);
    }

    //根据id批量删除
    @Test
    public void testDeleteBatchId() {
        //DELETE FROM user WHERE id IN ( ? , ? )
        userMapper.deleteBatchIds(Arrays.asList(1352994810700046340L,1352994810700046339L));
    }

    //通过map条件删除
    @Test
    public void testDeleteMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","ldx学习mybatis_plus");
        map.put("age",3);
        //DELETE FROM user WHERE name = ? AND age = ?
        userMapper.deleteByMap(map);
    }



}
