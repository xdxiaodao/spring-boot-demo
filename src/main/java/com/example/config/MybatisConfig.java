package com.example.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 16:42
 * @desc
 */
@MapperScan("com.example.dao")
@Configuration
public class MybatisConfig {

}
