package cn.abelib.common.result;


/**
 *
 * @author abel
 * @date 2017/11/3
 * 元信息类, 用于构建Response
 */
public class Meta{
    // 状态码
    private int code;
    // 原因短语
    private String message;

    public Meta(int code, String message){
        this.code = code;
        this.message = message;
    }

    public Meta fillArgs(Object ...args){
         int code = this.code;
         String message = String.format(this.message, args);
         return new Meta(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}