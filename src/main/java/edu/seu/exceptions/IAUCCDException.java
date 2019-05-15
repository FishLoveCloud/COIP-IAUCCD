package edu.seu.exceptions;

import edu.seu.base.CodeEnum;

public class IAUCCDException extends Exception{

    CodeEnum codeEnum;

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
