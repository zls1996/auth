package com.whxl.user_manage;

import com.whxl.user_manage.entity.User;
import com.whxl.user_manage.service.IUserService;
import com.whxl.user_manage.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class UserManagerShiroApplicationTests {

    @Autowired
    private RedisUtil redisUtil;


    @Test
    public void contextLoads() {
        //User user = (User) redisUtil.get("296D951931A9B9C8756AC75B05FE116C");
        //System.out.println(redisUtil.hasKey("296D951931A9B9C8756AC75B05FE116C"));
        //redisUtil.del("296D951931A9B9C8756AC75B05FE116C");
        //System.out.println(user);
//        String str = new String("朱立松");
//        redisUtil.set("name", str);

        System.out.println(redisUtil.get("name"));
    }

}

