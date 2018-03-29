package cn.abelib.shop.common.result;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by abel on 2017/11/7.
 * API响应类, 响应的API都以这种格式返回
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) // 如果key为空不会出现在Json字符串中
public class Response<T> {
    // 元信息
    private Meta meta;
    // 响应体
    private T body;

    private Response(Meta meta){
        this.meta = meta;
    }

    private Response(Meta meta, T body){
        this.meta = meta;
        this.body = body;
    }

    public boolean isSuccess(){
        return this.meta.getCode() == 0;
    }

    public static <T> Response<T> success(Meta meta, T body){
        return new Response<>(meta, body);
    }

    public static <T> Response<T> success(Meta meta){
        return new Response<>(meta);
    }

    public static <T> Response<T> failed(Meta meta){
        return new Response<>(meta);
    }

    public Meta getMeta() {
        return meta;
    }

    public T getBody() {
        return body;
    }
}
