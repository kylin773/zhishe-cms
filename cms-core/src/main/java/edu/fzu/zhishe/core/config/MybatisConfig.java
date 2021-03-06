package edu.fzu.zhishe.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liang on 4/11/2020.
 * @version 1.0
 */
@Configuration
@MapperScan({"edu.fzu.zhishe.cms.mapper", "edu.fzu.zhishe.core.dao"})
public class MybatisConfig {

}
