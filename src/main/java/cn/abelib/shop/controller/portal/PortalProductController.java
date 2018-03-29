package cn.abelib.shop.controller.portal;

import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.service.ProductService;
import cn.abelib.shop.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * Created by abel on 2017/11/9.
 * 用户商品模块接口
 */
@RestController
@RequestMapping("/portal/product")
public class PortalProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/detail")
    public Response<ProductDetailVo> getDetail(Integer productId){
        return productService.getProductDetailPortal(productId);
    }

    @GetMapping("/list")
    public Response<PageInfo> listProduct(@RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(value = "orderBy", defaultValue = "") String orderBy){
        return productService.getProductByKeywordOrCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}

