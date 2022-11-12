package cn.core.support;

import cn.core.filter.ShapeOverlayAdaptor;

/**
 * 图像叠加的支持
 *
 * @author tracy
 * @since 1.0.0
 */
public interface OverlayingSupport<W> {

    /**
     * 叠加形状
     * @param shapeAdaptor 形状叠加适配器
     * @return W 泛型
     */
    W shape(ShapeOverlayAdaptor shapeAdaptor);
}
