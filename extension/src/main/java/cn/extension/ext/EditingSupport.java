package cn.extension.ext;

import cn.extension.filter.BorderHandler;

/**
 * 编辑图片操作的扩展支持
 *
 * @author tracy
 * @since 1.0.0
 */
public interface EditingSupport<W> {

    /**
     * 添加边框
     * @param bh 处理器
     * @return 包装器
     */
    W border(BorderHandler bh);


}
