package cn.extension.tool;

import java.util.AbstractMap;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 抽象的通用建造器
 *
 * @author tracy
 * @since 1.0.0
 */
public abstract class AbstractBuilder<Children extends AbstractBuilder<Children, T>, T>
        extends AbstractMap<String, Object> {

    /**
     * 设置属性
     * @param property 属性名称
     * @param val 值
     * @return Children
     */
    public Children set(String property, Object val) {
        throw new NoSuchElementException(property);
    }

    @Override
    public Object put(String key, Object value) {
        // cannot access
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        // False in this context means that the property is NOT read only
        return false;
    }

    @Override
    public Object get(Object key) {
        // In certain cases, get is also required to return null for read-write "properties"
        return null;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        // cannot access
        throw new UnsupportedOperationException();
    }

    /**
     * 构建对象
     * @return T
     */
    public abstract T build();
}
