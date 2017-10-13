package com.example.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 15:43
 * @desc
 */

@WebServlet(urlPatterns = "/druid/*",
initParams = {@WebInitParam(name = "allow", value = "127.0.0.1"),
@WebInitParam(name = "deny", value = "192.168.16.111"),
@WebInitParam(name = "loginUsername", value = "admin"),
@WebInitParam(name = "loginPassword", value = "admin"),
@WebInitParam(name = "resetEnable", value = "false")})
public class DruidStatViewServlet extends StatViewServlet{

}
