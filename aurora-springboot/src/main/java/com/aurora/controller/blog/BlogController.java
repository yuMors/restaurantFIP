package com.aurora.controller.blog;

import com.aurora.domain.dto.AuroraHomeInfoDTO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.service.AuroraInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这里放的是前台初始化调用的一些接口
 */
@RestController
public class BlogController {
    @Autowired
    private AuroraInfoService auroraInfoService;

    /**
     * 前台
     * 获取系统信息
     */
    @GetMapping("/getWebConfig")
    public ResultVo<AuroraHomeInfoDTO> getBlogHomeInfo() {
        return ResultVo.ok(auroraInfoService.getAuroraHomeInfo());
    }

}
