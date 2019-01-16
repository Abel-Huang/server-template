package cn.abelib.shop.service.impl;

import cn.abelib.common.constant.StatusConstant;
import cn.abelib.common.result.Response;
import cn.abelib.common.tools.DateUtil;
import cn.abelib.common.tools.Md5Util;
import cn.abelib.shop.config.ServerConfig;
import cn.abelib.data.mongo.IdGenerator;
import cn.abelib.data.mongo.MongoService;
import cn.abelib.shop.pojo.File;
import cn.abelib.shop.service.FileService;
import cn.abelib.shop.vo.FileVo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author abel
 * @date 2018/1/31
 */
@Service
public class FileServiceImpl implements FileService{
    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private MongoService mongoService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Response<FileVo> upload(MultipartFile file) {
        File saveFile = new File();
        try {
            saveFile.setId(IdGenerator.generator());
            saveFile.setName(file.getOriginalFilename());
            saveFile.setContentType(file.getContentType());
            saveFile.setSize(file.getSize());
            saveFile.setUploadDate(new Date());
            saveFile.setMd5(Md5Util.getMd5(file.getInputStream()));
            saveFile.setContent(file.getBytes());
             String path = "//" + serverConfig.getHost() + ":" + serverConfig.getPort() + "/file/" + saveFile.getId();
            saveFile.setPath(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Object> saveMap = Maps.newHashMap();
        saveMap.put("_id", saveFile.getId());
        saveMap.put("name", saveFile.getName());
        saveMap.put("contentType", saveFile.getContentType());
        saveMap.put("size", saveFile.getSize());
        saveMap.put("uploadDate", saveFile.getUploadDate());
        saveMap.put("md5", saveFile.getMd5());
        saveMap.put("content", saveFile.getContent());
        saveMap.put("path", saveFile.getPath());
        mongoService.insert("product", saveMap);

        System.err.println(saveFile.toString());
        FileVo fileVo = this.assembleFileVo(saveFile);
        return Response.success(StatusConstant.GENERAL_SUCCESS, fileVo);
    }

    private FileVo assembleFileVo(File file){
        FileVo fileVo = new FileVo();
        fileVo.setId(file.getId());
        fileVo.setName(file.getName());
        fileVo.setContentType(file.getContentType());
        fileVo.setSize(file.getSize());
        fileVo.setUploadDate(DateUtil.dateToStr(file.getUploadDate()));
        fileVo.setMd5(file.getMd5());
        fileVo.setPath(file.getPath());
        return fileVo;
    }
    @Override
    public Response<Object> download() {
        return null;
    }

    @Override
    public Response remove() {
        return null;
    }
}
