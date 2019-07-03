package cn.abelib.biz.service;

import cn.abelib.st.core.result.Response;
import cn.abelib.biz.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abel
 * @date 2018/1/31
 */
public interface FileService {
    Response<FileVo> upload(MultipartFile file);

    Response<Object> download();

    Response remove();
}
