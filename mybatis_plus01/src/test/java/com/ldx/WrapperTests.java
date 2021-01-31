package com.ldx;

/**
 * @author lidexin
 * @create 2021-01-31 12:12
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ldx.mapper.UserMapper;
import com.ldx.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class WrapperTests {


    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        //查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        /**SELECT
                id,
                name,
                age,
                email,
                version,
                deleted,
                create_time,
                update_time
        FROM
                user
        WHERE
                deleted=0
        AND name IS NOT NULL
        AND email IS NOT NULL
        AND age >= 12
         **/
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("name")
                .isNotNull("email")
                .ge("age",12);//le
        userMapper.selectList(wrapper).forEach(System.out::println);//和我们刚学习的map查询对比

    }

    @Test
    void test2() {
        //查询名字“mybatis_plus学习”
        /**
         * SELECT
         *         id,
         *         name,
         *         age,
         *         email,
         *         version,
         *         deleted,
         *         create_time,
         *         update_time
         *     FROM
         *         user
         *     WHERE
         *         deleted=0
         *         AND name = 'mybatis_plus学习'
         */
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","mybatis_plus学习");
        User user = userMapper.selectOne(wrapper);//查询一个数据，出现多个结果使用List或者Map
        System.out.println(user);

    }

    @Test
    void test3() {
        //查询年龄在20~30岁之间的用户
        /**
        *   SELECT
         *         COUNT(1)
         *     FROM
         *         user
         *     WHERE
         *         deleted=0
         *         AND age BETWEEN 20 AND 30
         *
         * Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@1e0fdb2f]
         * 4
         */
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age",20,30);//区间
        Integer count = userMapper.selectCount(wrapper);//查询结果集
        System.out.println(count);//4，包含20和30
    }

    //模糊查询
    @Test
    void test4() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //左和右  t%
        wrapper.notLike("name","e")
                .likeRight("email","t");
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    @Test
    void test5() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //id在子查询中查出来
        /**
         *  SELECT
         *         id,
         *         name,
         *         age,
         *         email,
         *         version,
         *         deleted,
         *         create_time,
         *         update_time
         *     FROM
         *         user
         *     WHERE
         *         deleted=0
         *         AND id IN (
         *             select
         *                 id
         *             from
         *                 user
         *             where
         *                 id<3
         *         )
         */
        wrapper.inSql("id","select id from user where id<3");

        List<Object> objects = userMapper.selectObjs(wrapper);
        objects.forEach(System.out::println);
    }

    @Test
    void test6() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //对id进行排序
        wrapper.orderByAsc("id");

        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }
}
