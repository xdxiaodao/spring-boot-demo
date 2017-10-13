package com.example.dao;

import com.example.vo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 16:40
 * @desc
 */
@Repository
public interface UserDao {
  User getByUname(@Param("uname") String uname);
}
