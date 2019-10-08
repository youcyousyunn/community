package com.ycs.community.basebo.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    ANSWER(2);
    private int type;

    public int getType () {
        return type;
    }

    CommentTypeEnum(int type) {
        this.type = type;
    }

    public static boolean isExist (int type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
