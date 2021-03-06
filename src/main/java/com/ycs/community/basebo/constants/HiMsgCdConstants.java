package com.ycs.community.basebo.constants;

public class HiMsgCdConstants {
    public static final String SUCCESS = "0000";
    public static final String FAIL = "9999";

    /*** 接口请求报文异常 ****/
    public static final String TX_REQUESTBODY_FAIL = "T0001";

    /*** 提问失败 ****/
    public static final String ASK_QUESTION_FAIL = "B0001";
    /*** 问题浏览数累加失败 ****/
    public static final String INCREASE_QUESTION_VIEW_COUNT_FAIL = "B0002";
    /*** 问题已不存在 ****/
    public static final String QUESTION_NOT_EXIST = "B0003";
    /*** 答案已不存在 ****/
    public static final String ANSWER_NOT_EXIST = "B0004";
    /*** 评论问题失败 ****/
    public static final String COMMENT_QUESTION_FAIL = "B0005";
    /*** 评论答案失败 ****/
    public static final String COMMENT_ANSWER_FAIL = "B0006";
    /*** 问题评论数累加失败 ****/
    public static final String INCREASE_QUESTION_COMMENT_COUNT_FAIL = "B0007";
    /*** 答案评论数累加失败 ****/
    public static final String INCREASE_ANSWER_COMMENT_COUNT_FAIL = "B0008";
    /*** 回答问题失败 ****/
    public static final String ANSWER_QUESTION_FAIL = "B0009";
    /*** 添加用户失败 ****/
    public static final String ADD_USER_FAIL = "B0010";
    /*** 更新用户失败 ****/
    public static final String UPD_USER_FAIL = "B0011";
    /*** 用户不存在 ****/
    public static final String USER_NOT_EXIST = "B0012";
    /*** 密码错误 ****/
    public static final String ERROR_PASSWORD = "B0013";
    /*** 定时任务不存在 ****/
    public static final String JOB_NOT_EXIST = "B0014";
    /*** 邮件配置不存在 ****/
    public static final String EMAIL_CONFIG_NOT_EXIST = "B0015";
    /*** 更新邮件配置失败 ****/
    public static final String UPD_EMAIL_CONFIG_FAIL = "B0016";
    /*** 上传附件失败 ****/
    public static final String UPLOAD_ATTACH_FAIL = "B0017";
    /*** 附件不存在 ****/
    public static final String ATTACH_NOT_EXIST = "B0018";
    /*** 更新附件失败 ****/
    public static final String UPD_ATTACH_FAIL = "B0019";
    /*** 删除附件失败 ****/
    public static final String DEL_ATTACH_FAIL = "B0020";
    /*** 添加日志失败 ****/
    public static final String ADD_LOG_FAIL = "B0021";
    /*** 日志不存在 ****/
    public static final String LOG_NOT_EXIST = "B0022";
    /*** 添加角色菜单失败 ****/
    public static final String ADD_ROLE_MENU_FAIL = "B0023";
    /*** 删除角色菜单失败 ****/
    public static final String DEL_ROLE_MENU_FAIL = "B0024";
    /*** 添加角色失败 ****/
    public static final String ADD_ROLE_FAIL = "B0025";
    /*** 更新角色失败 ****/
    public static final String UPD_ROLE_FAIL = "B0026";
    /*** 删除角色失败 ****/
    public static final String DEL_ROLE_FAIL = "B0027";
    /*** 查询角色失败 ****/
    public static final String QRY_ROLE_FAIL = "B0028";
    /*** 查询角色菜单失败 ****/
    public static final String QRY_ROLE_MENU_FAIL = "B0029";
    /*** 查询用户角色失败 ****/
    public static final String QRY_USER_ROLE_FAIL = "B0030";
    /*** 查询角色权限失败 ****/
    public static final String QRY_ROLE_PERMISSION_FAIL = "B0031";
    /*** 更新字典失败 ****/
    public static final String UPD_DICT_FAIL = "B0032";
    /*** 删除字典详情失败 ****/
    public static final String DEL_DICT_DETAIL_FAIL = "B0033";
    /*** 删除字典失败 ****/
    public static final String DEL_DICT_FAIL = "B0034";
    /*** 添加字典失败 ****/
    public static final String ADD_DICT_FAIL = "B0035";
    /*** 添加字典详情失败 ****/
    public static final String ADD_DICT_DETAIL_FAIL = "B0036";
    /*** 更新字典详情失败 ****/
    public static final String UPD_DICT_DETAIL_FAIL = "B0037";
    /*** 更新部门失败 ****/
    public static final String UPD_DEPT_FAIL = "B0038";
    /*** 添加角色部门失败 ****/
    public static final String ADD_ROLE_DEPT_FAIL = "B0039";
    /*** 删除角色部门失败 ****/
    public static final String DEL_ROLE_DEPT_FAIL = "B0040";
    /*** 存在子部门, 不能删除 ****/
    public static final String HAS_CHILDREN_CAN_NOT_DEL_DEPT = "B0041";
    /*** 存在角色关联, 请取消关联后再试 ****/
    public static final String RELATED_ROLE_CAN_NOT_DEL_DEPT = "B0042";
    /*** 删除部门失败 ****/
    public static final String DEL_DEPT_FAIL = "B0043";
    /*** 添加部门失败 ****/
    public static final String ADD_DEPT_FAIL = "B0044";
    /*** 添加岗位失败 ****/
    public static final String ADD_JOB_FAIL = "B0045";
    /*** 删除岗位失败 ****/
    public static final String DEL_JOB_FAIL = "B0046";
    /*** 更新岗位失败 ****/
    public static final String UPD_JOB_FAIL = "B0047";
    /*** 查询部门下所有岗位失败 ****/
    public static final String QRY_DEPT_JOB_FAIL = "B0048";
    /*** 删除用户角色失败 ****/
    public static final String DEL_USER_ROLE_FAIL = "B0049";
    /*** 添加用户角色失败 ****/
    public static final String ADD_USER_ROLE_FAIL = "B0050";
    /*** 删除用户失败 ****/
    public static final String DEL_USER_FAIL = "B0052";
    /*** 存在用户关联, 请取消关联后再试 ****/
    public static final String RELATED_USER_CAN_NOT_DEL_JOB = "B0053";
    /*** 存在岗位关联, 请取消关联后再试 ****/
    public static final String RELATED_JOB_CAN_NOT_DEL_DEPT = "B0054";
    /*** 存在子菜单, 不能删除 ****/
    public static final String HAS_CHILDREN_CAN_NOT_DEL_MENU = "B0055";
    /*** 添加菜单失败 ****/
    public static final String ADD_MENU_FAIL = "B0056";
    /*** 删除菜单失败 ****/
    public static final String DEL_MENU_FAIL = "B0057";
    /*** 更新菜单失败 ****/
    public static final String UPD_MENU_FAIL = "B0058";
    /*** 更新用户头像失败 ****/
    public static final String UPD_USER_AVATAR_FAIL = "B0059";
    /*** 删除问题评论失败 ****/
    public static final String DEL_QUESTION_COMMENT_FAIL = "B0060";
    /*** 删除回答评论失败 ****/
    public static final String DEL_ANSWER_COMMENT_FAIL = "B0061";
    /*** 删除问题回答失败 ****/
    public static final String DEL_QUESTION_ANSWER_FAIL = "B0062";
    /*** 查询问题失败 ****/
    public static final String QRY_QUESTION_FAIL = "B0063";
    /*** 添加标签失败 ****/
    public static final String ADD_TAG_FAIL = "B0064";
    /*** 更新标签失败 ****/
    public static final String UPD_TAG_FAIL = "B0065";
    /*** 存在子标签, 不能删除 ****/
    public static final String HAS_CHILDREN_CAN_NOT_DEL_TAG = "B0066";
    /*** 删除标签失败 ****/
    public static final String DEL_TAG_FAIL = "B0067";
    /*** 更新问题失败 ****/
    public static final String UPD_QUESTION_FAIL = "B0068";
    /*** 文档不存在 ****/
    public static final String DOC_NOT_EXIST = "B0069";
    /*** 删除文档失败 ****/
    public static final String DEL_DOC_FAIL = "B0070";
    /*** 添加文档失败 ****/
    public static final String ADD_DOC_FAIL = "B0071";
    /*** 更新文档失败 ****/
    public static final String UPD_DOC_FAIL = "B0072";

    /*** 流程图不存在 ****/
    public static final String FLOW_IMG_NOT_EXIST = "F0002";
    /*** 流程已不存在 ****/
    public static final String FLOW_NOT_EXIST = "F0003";
    /*** 流程定义表中已存在 ****/
    public static final String FLOW_HAS_EXIST = "F0004";
    /*** 添加请假申请失败 ****/
    public static final String ADD_VACATION_TASK_FAIL = "F0005";
    /*** 更新请假申请失败 ****/
    public static final String UPD_VACATION_TASK_FAIL = "F0006";
    /*** 运行流程失败 ****/
    public static final String RUN_FLOW_FAIL = "F0007";
    /*** 删除请假申请失败 ****/
    public static final String DEL_VACATION_TASK_FAIL = "F0008";
    /*** 添加流程定义失败 ****/
    public static final String ADD_FLOW_DEF_FAIL = "F0009";
    /*** 更新请假单审批人失败 ****/
    public static final String UPD_VACATION_TASK_ASSIGNEE_FAIL = "F0010";
    /*** 更新请假单状态失败 ****/
    public static final String UPD_VACATION_TASK_STATE_FAIL = "F0011";
    /*** 添加流程过程日志失败 ****/
    public static final String ADD_PROCESS_LOG_FAIL = "F0012";
    /*** 流程任务已不存在 ****/
    public static final String FLOW_TASK_NOT_EXIST = "F0013";
    /*** 更新流程定义失败 ****/
    public static final String UPD_FLOW_DEF_FAIL = "F0014";
}
