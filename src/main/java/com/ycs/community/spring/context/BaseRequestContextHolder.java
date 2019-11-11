package com.ycs.community.spring.context;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;

public class BaseRequestContextHolder {
    private static ThreadLocal<BaseRequestDto> baseRequestContextHolder = new ThreadLocal<>();

    public static void setBaseRequest(BaseRequestDto baseRequestDto) {
        baseRequestContextHolder.set(baseRequestDto);
    }

    public static BaseRequestDto getBaseRequest() {
        return baseRequestContextHolder.get();
    }

}