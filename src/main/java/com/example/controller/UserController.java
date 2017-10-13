package com.example.controller;

import com.example.dao.UserDao;
import com.example.redis.RedisClient;
import com.example.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 16:43
 * @desc
 */
@RestController
public class UserController {

  @Autowired
  private UserDao userDao;

  @Autowired
  private RedisClient redisClient;

  @RequestMapping(value = "/getuser", produces = {MediaType.APPLICATION_JSON_VALUE})
  public User getuser(ModelMap model, String uname, String jp) {
    return userDao.getByUname(uname);
  }

  @RequestMapping("/guest")
  public String hiGuest() {
    return "hi guest!";
  }

  @RequestMapping(value = "/adduser2redis", produces = {MediaType.APPLICATION_JSON_VALUE})
  String addUser2Redis(String uname) {
    redisClient.getJedis().sadd("users", uname);
    redisClient.releaseJedis();
    return "OK";
  }
}
