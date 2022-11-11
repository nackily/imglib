package cn.core.support;


import cn.extension.MergeStrategy;
import cn.extension.SplitStrategy;

import java.io.IOException;

/**
 * 图像数量变化的扩展支持
 *
 * @author tracy
 * @since 1.0.0
 */
public interface TransitioningSupport<W> {

    /**
     * 图像合并
     *
     * @param strategy 策略
     * @return 包装器
     * @throws IOException IO异常
     */
    W merge(MergeStrategy strategy) throws IOException;

    /**
     * 图像拆分
     *
     * @param strategy 处理器
     * @return 包装器
     * @throws IOException IO异常
     */
    W split(SplitStrategy strategy) throws IOException;

}