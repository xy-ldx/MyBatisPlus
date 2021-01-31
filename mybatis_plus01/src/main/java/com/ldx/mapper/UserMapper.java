package com.ldx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldx.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * @author lidexin
 * @create 2021-01-23 20:38
 */
//在对应的Mapper上面继承基本的类BaseMapper
@Repository //代表持久层
public interface UserMapper extends BaseMapper<User> {
    //所有的CRUD操作都已经编写完成了
    //你不需要像以前的配置一堆文件了！
}
