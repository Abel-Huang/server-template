package cn.abelib.st.core.cache;

/**
 *
 * @author abel
 * @date 2018/2/1
 */
public interface KeyPrefix {
    int expire() ;

    String getPrefix();
}
