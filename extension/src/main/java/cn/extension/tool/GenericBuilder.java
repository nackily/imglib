package cn.extension.tool;


/**
 * 抽象的通用建造器
 *
 * @author tracy
 * @since 1.0.0
 */
public interface GenericBuilder<T> {
    
    /**
     * 构建对象
     * @return T
     */
     T build();
    
}
