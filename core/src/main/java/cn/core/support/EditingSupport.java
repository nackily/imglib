package cn.core.support;


import cn.core.filter.BorderHandler;
import cn.core.filter.HighQualityExpandHandler;
import cn.core.filter.MosaicHandler;
import cn.core.filter.RoundRectHandler;

/**
 * 编辑图片操作的扩展支持
 *
 * @author tracy
 * @since 1.0.0
 */
public interface EditingSupport<W> {

    /**
     * 添加边框
     * @param handler 处理器
     * @return 泛型
     */
    W border(BorderHandler handler);

    /**
     * 图像扩张
     * @param handler 处理器
     * @return 泛型
     */
    W expand(HighQualityExpandHandler handler);

    /**
     * 马赛克
     * @param handler 处理器
     * @return 泛型
     */
    W mosaic(MosaicHandler handler);

    /**
     * 圆角
     * @param handler 处理器
     * @return 泛型
     */
    W roundRect(RoundRectHandler handler);
}
