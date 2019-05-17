package edu.seu.exceptions;

import edu.seu.base.CodeEnum;

/**
 * @Author: yxl
 * @Date: 2019-05-15 13:57
 */
public class IAUCCDException extends Exception {

    private CodeEnum codeEnum;

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(CodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }

    public IAUCCDException() {
    }

    public IAUCCDException(CodeEnum codeEnum, String msg) {
        super(msg);
        this.codeEnum = codeEnum;
    }

    public IAUCCDException(String msg) {
        super(msg);
    }
}
