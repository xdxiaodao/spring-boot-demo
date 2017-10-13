package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/11 20:45
 * @desc
 */
@RestController
public class HelloController {

  @RequestMapping("/hello")
  public String hello() {
    return "hello,world";
  }
}
