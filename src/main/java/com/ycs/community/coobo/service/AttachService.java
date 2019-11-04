package com.ycs.community.coobo.service;

import com.ycs.community.coobo.domain.dto.AttachRequestDto;
import com.ycs.community.coobo.domain.dto.QryAttachPageRequestDto;
import com.ycs.community.coobo.domain.dto.QryAttachPageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AttachService {
    QryAttachPageResponseDto qryAttachPage(QryAttachPageRequestDto request);
    boolean upload(MultipartFile multipartFile, String name);
    boolean updAttach(AttachRequestDto request);
    boolean delAttach(Long id);
    boolean downAttach(AttachRequestDto request, HttpServletResponse response);
    boolean delAllAttach(Long[] ids);
}
