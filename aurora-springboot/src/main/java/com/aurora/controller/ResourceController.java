package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.LabelOptionDTO;
import com.aurora.domain.dto.ResourceDTO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.service.ResourceService;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.ResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "资源模块")
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "查看资源列表")
    @GetMapping("/admin/resources")
    public ResultVo<List<ResourceDTO>> listResources(ConditionVO conditionVO) {
        return ResultVo.ok(resourceService.listResources(conditionVO));
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除资源")
    @DeleteMapping("/admin/resources/{resourceId}")
    public ResultVo<?> deleteResource(@PathVariable("resourceId") Integer resourceId) {
        resourceService.deleteResource(resourceId);
        return ResultVo.ok();
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/admin/resources")
    public ResultVo<?> saveOrUpdateResource(@RequestBody @Valid ResourceVO resourceVO) {
        resourceService.saveOrUpdateResource(resourceVO);
        return ResultVo.ok();
    }

    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/admin/role/resources")
    public ResultVo<List<LabelOptionDTO>> listResourceOption() {
        return ResultVo.ok(resourceService.listResourceOption());
    }
}