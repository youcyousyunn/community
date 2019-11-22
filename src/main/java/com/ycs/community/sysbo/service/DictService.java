package com.ycs.community.sysbo.service;

import com.ycs.community.sysbo.domain.dto.*;

public interface DictService {
    QryDictPageResponseDto qryDictPage(QryDictPageRequestDto request);
    QryDictDetailPageResponseDto qryDictDetailPage(QryDictDetailPageRequestDto request);
    DictDetailResponseDto qryDictDetailsByName(DictRequestDto request);
    boolean updDict(DictRequestDto request);
    boolean delDict(Long id);
    boolean addDict(DictRequestDto request);
    boolean addDictDetail(DictDetailRequestDto request);
    boolean delDictDetail(Long id);
    boolean updDictDetail(DictDetailRequestDto request);
}
