package org.geektimes.bo;

/**
 * @author chenyue
 * @date 2021/3/10
 */
public class ReturnValue {
    private Boolean code;
    private String message;

    public ReturnValue(Boolean code, String message) {
        this.code = code;
        this.message = message;
    }
    public Boolean getCode() {
        return code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
