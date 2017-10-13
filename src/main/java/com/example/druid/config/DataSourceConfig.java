package com.example.druid.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 16:26
 * @desc
 */
@Configuration
public class DataSourceConfig {

  @Bean(name = "primaryDataSource")
  @Qualifier("primaryDataSource")
  @ConfigurationProperties(prefix="spring.datasource.primary")
  public DataSource primaryDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "secondaryDataSource")
  @Qualifier("secondaryDataSource")
  @Primary
  @ConfigurationProperties(prefix="spring.datasource.secondary")
  public DataSource secondaryDataSource() {
    return DataSourceBuilder.create().build();
  }
}
