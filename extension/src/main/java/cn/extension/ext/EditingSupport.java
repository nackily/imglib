package cn.extension.ext;

import cn.extension.filter.BorderHandler;
import cn.extension.filter.HighQualityExpandHandler;

/**
 * 编辑图片操作的扩展支持
 *
 * @author tracy
 * @since 1.0.0
 */
public interface EditingSupport<W> {

    /**
     * 添加边框
     * @param hdl 处理器
     * @return 包装器
     */
    W border(BorderHandler hdl);

    /**
     * 图像扩张
     * @param hdl 处理器
     * @return 包装器
     */
    W expand(HighQualityExpandHandler hdl);
}
