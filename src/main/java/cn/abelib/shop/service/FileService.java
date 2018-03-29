package cn.abelib.shop.service;

import cn.abelib.shop.common.result.Response;
import cn.abelib.shop.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by abel on 2018/1/31.
 */
public interface FileService {
    Response<FileVo> upload(MultipartFile file);

    Response<Object> download();

    Response remove();
}
