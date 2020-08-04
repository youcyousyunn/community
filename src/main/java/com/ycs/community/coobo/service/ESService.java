package com.ycs.community.coobo.service;

import com.ycs.community.coobo.domain.dto.ESPageRequestDto;
import com.ycs.community.coobo.domain.dto.ESPageResponseDto;
import com.ycs.community.coobo.domain.dto.ESRequestDto;

import java.io.IOException;

public interface ESService {
    ESPageResponseDto qryContentPage(ESPageRequestDto request) throws IOException;
    boolean addDoc(ESRequestDto request) throws IOException;
    boolean delDocById(String id) throws IOException;
    boolean updDoc(ESRequestDto request) throws IOException;
}
