package indi.mofan.api;

import feign.Param;
import feign.Request;
import feign.RequestLine;
import indi.mofan.pojo.User;

/**
 * @author mofan
 * @date 2023/8/25 10:35
 */
public interface UserApi {

    @RequestLine("GET /user/{userId}")
    User queryUser(@Param("userId") Integer userId);

    @RequestLine("GET /user/{name}")
    User queryUserByName(@Param("name") String name, Request.Options options);
}
