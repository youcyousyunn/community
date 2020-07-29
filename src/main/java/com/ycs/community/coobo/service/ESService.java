package com.ycs.community.coobo.service;

import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;

import java.io.IOException;

public interface ESService {
    ESPageResponseDto qryContentPage(ESPageRequestDto request) throws IOException;
}
