package com.ycs.community.sysbo.domain.dto;

import com.ycs.community.basebo.domain.dto.BaseRequestDto;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class EmailRequestDto extends BaseRequestDto {
    // 邮件配置信息
    private Long id;
    private String host; // 邮件服务器SMTP地址
    private Integer port; // 邮件服务器SMTP端口
    private String user; // 发件人用户名, 默认为发件人邮箱前缀
    private String fromUser; // 发件人
    private String password;

    // 发送邮件信息
    private List<String> receivers; // 收件人(复数)
    private String subject;
    private String content;

    /**
     * 更新邮件配置接口请求报文检查
     * @return
     */
    public boolean checkMailConfigRequestDto() {
        if (null == host || StringUtils.isEmpty(host)) {
            return false;
        }
        if (null == port || StringUtils.isEmpty(port)) {
            return false;
        }
        if (null == user || StringUtils.isEmpty(user)) {
            return false;
        }
        if (null == fromUser || StringUtils.isEmpty(fromUser)) {
            return false;
        }
        if (null == password || StringUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    /**
     * 发送邮件接口请求报文检查
     * @return
     */
    public boolean checkSendMailRequestDto() {
        if (CollectionUtils.isEmpty(receivers)) {
            return false;
        }
        if (null == subject || StringUtils.isEmpty(subject)) {
            return false;
        }
        if (null == content || StringUtils.isEmpty(content)) {
            return false;
        }
        return true;
    }
}
