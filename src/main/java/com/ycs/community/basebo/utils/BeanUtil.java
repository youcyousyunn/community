package com.ycs.community.basebo.utils;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import org.springframework.beans.BeanUtils;

public class BeanUtil {
    public static <T> T trans2Entity(BaseRequestDto requestDto, Class<T> entityClazz) {
        if (null == requestDto) {
            return null;
        } else {
            T entity = null;
            try {
                entity = entityClazz.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            BeanUtils.copyProperties(requestDto, entity);
            return entity;
        }
    }
}
