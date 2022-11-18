package cn.core;


/**
 * 数据源
 *
 * @author tracy
 * @since 1.0.0
 */
public interface Source<S> {

    /**
     * 获取源
     * @return 源
     */
    S getSource();

    /**
     * 是否已读取过
     * @return true-已读取过
     */
    boolean isReadCompleted();

}
