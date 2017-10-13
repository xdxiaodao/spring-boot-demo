package com.example.vo;

import org.apache.ibatis.annotations.Param;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 16:37
 * @desc
 */
public class User {
  private String uid;
  private String uname;

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getUname() {
    return uname;
  }

  public void setUname(String uname) {
    this.uname = uname;
  }
}
