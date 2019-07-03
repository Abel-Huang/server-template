package cn.abelib.biz.controller.portal;

import cn.abelib.st.core.result.Response;
import cn.abelib.biz.service.ProductService;
import cn.abelib.biz.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author abel
 * @date 2017/11/9
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

    /**
     *  针对商品详情信息的RestFul API改造
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public Response<ProductDetailVo> getDetailRest(@PathVariable("productId") Integer productId){
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

