package com.aurora.controller;

import com.aurora.domain.dto.AuroraAdminInfoDTO;
import com.aurora.domain.dto.WebsiteConfigDTO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.domain.vo.WebsiteConfigVO;
import com.aurora.service.AuroraInfoService;
import com.aurora.util.qiniuYunOss.config.QiniuYunOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api(tags = "aurora信息")
@RestController
public class AuroraInfoController {
    @Autowired
    private AuroraInfoService auroraInfoService;

    @Autowired
    private QiniuYunOssService qiniuUploadService;

    @ApiOperation(value = "获取系统后台信息")
    @GetMapping("/admin")
    public ResultVo<AuroraAdminInfoDTO> getBlogBackInfo() {
//        LinkedList
        return ResultVo.ok(auroraInfoService.getAuroraAdminInfo());
    }


    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    public ResultVo<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        auroraInfoService.updateWebsiteConfig(websiteConfigVO);
        return ResultVo.ok();
    }

    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    public ResultVo<WebsiteConfigDTO> getWebsiteConfig() {
        return ResultVo.ok(auroraInfoService.getWebsiteConfig());
    }



    @ApiOperation(value = "上传博客配置图片")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    public ResultVo<String> savePhotoAlbumCover(MultipartFile file) {
        //return ResultVo.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
        return ResultVo.ok(qiniuUploadService.uploadImg(file));
    }

}
