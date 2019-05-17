package edu.seu.base;

public enum CodeEnum {

    /**
     * OK
     */
    SUCCESS(200),

    /**
     * 文件上传错误
     * */
    FILE_UPLOAD_ERROR(401),

    /**
     * 用户错误
     */
    USER_ERROR(500),

    /**
     * 计算错误
     */
    CALCULATE_ERROR(600),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR(1000);

    int value;

    CodeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
