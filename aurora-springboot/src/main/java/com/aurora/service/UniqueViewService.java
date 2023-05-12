package com.aurora.service;

import com.aurora.domain.dto.UniqueViewDTO;
import com.aurora.domain.entity.UniqueView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UniqueViewService extends IService<UniqueView> {

    List<UniqueViewDTO> listUniqueViews();

}
