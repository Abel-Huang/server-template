package cn.abelib.shop.test;


import cn.abelib.shop.pojo.Shipping;
import cn.abelib.shop.cache.RedisStringService;
import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.common.constant.StatusConstant;
import cn.abelib.shop.common.constant.BusinessConstant;
import cn.abelib.shop.service.impl.CategoryServiceImpl;
import cn.abelib.shop.controller.portal.PortalProductController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by abel on 2018/2/1.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RedisStringService RedisStringService;

    @GetMapping("/redis/get/{key}")
    public Response<Integer> redisGet(@PathVariable String key){
        Integer i = RedisStringService.get(null, key, Integer.class);
        return Response.success(StatusConstant.GENERAL_SUCCESS, i);
    }

    /**
     * Created by abel on 2017/9/6.
     */
    @RestController
    @RequestMapping("/properties")
    public static class CartController {
        @Autowired
        private PortalProductController portalProductController;
        @Autowired
        private CategoryServiceImpl categoryServiceImpl;

        @GetMapping("/list")
        public Response listAllProperty(Integer categoryId, BusinessConstant page){

            return Response.failed(StatusConstant.GENERAL_SUCCESS);
        }

        @RequestMapping("/insert")
        public Response insertProperty(Shipping shipping){

            return Response.failed(StatusConstant.GENERAL_SUCCESS);
        }

        @RequestMapping("/delete")
        public Response deleteProperty(Integer id){

            return Response.failed(StatusConstant.GENERAL_SUCCESS);
        }

        @RequestMapping("/edit")
        public Response editProperty(Integer id){

            return Response.failed(StatusConstant.GENERAL_SUCCESS);
        }

        @RequestMapping("/update")
        public Response updateProper(Shipping shipping){

            return Response.failed(StatusConstant.GENERAL_SUCCESS);
        }
    }
}
