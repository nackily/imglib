package cn.core;


/**
 * A superclass of generic builder.
 *
 * @param <T> The type to build.
 * @author tracy
 * @since 0.2.1
 */
public interface GenericBuilder<T> {
    
    /**
     * Build a product.
     *
     * @return T The object to build.
     */
     T build();
}
