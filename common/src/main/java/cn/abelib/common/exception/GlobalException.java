package cn.abelib.common.exception;

import cn.abelib.common.result.Meta;

/**
 *
 * @author abel
 * @date 2018/2/6
 *  定义一个全局的业务异常
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 4682381314363710782L;

    private Meta meta;
    public GlobalException(Meta meta) {
        super(meta.toString());
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
    }
}
