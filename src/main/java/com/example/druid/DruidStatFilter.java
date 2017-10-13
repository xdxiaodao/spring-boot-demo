package com.example.druid;

import com.alibaba.druid.support.http.WebStatFilter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 15:49
 * @desc
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*", initParams = {
    @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*") // 忽略资源
})
public class DruidStatFilter extends WebStatFilter {

}
