package cn.abelib.shop.test;

import cn.abelib.shop.cache.RedisStringService;
import cn.abelib.shop.service.UserService;
import cn.abelib.shop.vo.UserVo;
import cn.abelib.shop.common.exception.GlobalException;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

import javax.validation.Valid;

/**
 * Created by abel on 2018/2/7.
 *  用户登录注册的接口
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    RedisStringService redisStringService;

    @GetMapping("/login")
    public Response<String> login(@Valid UserVo userVo){
        log.info(userVo.toString());
        // 参数校验
       if (userVo == null){
           throw new GlobalException(StatusConstant.GENERAL_SERVER_ERROR);
       }


        return Response.failed(StatusConstant.GENERAL_SUCCESS);
    }

    @GetMapping("/register")
    public Response<String> register(){
        return Response.failed(StatusConstant.GENERAL_SUCCESS);
    }
}
