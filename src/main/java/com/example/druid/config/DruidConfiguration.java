package com.example.druid.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 15:54
 * @desc
 */
@Configuration
public class DruidConfiguration {

  @Bean
  public ServletRegistrationBean DruidStatViewServlet() {

    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

    servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
    servletRegistrationBean.addInitParameter("deny", "192.168.1.73");
    servletRegistrationBean.addInitParameter("loginUsername", "admin2");
    servletRegistrationBean.addInitParameter("loginPassword", "123456");
    servletRegistrationBean.addInitParameter("resetEnable", "false");

    return servletRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean druidStatFilter2() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.addInitParameter("exclusions",
        "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");

    return filterRegistrationBean;

  }
}
