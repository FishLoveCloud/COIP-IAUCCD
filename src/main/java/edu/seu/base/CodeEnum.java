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
     * 账户错误
     */
    USER_ERROR(500),

    /**
     * 文档相关错误
     */
    DOCUMENT_ERROR(600),

    /**
     * 归属相关错误
     */
    AFFILIATION_ERROR(700),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR(1000);

    int value;
    CodeEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
