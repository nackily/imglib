package cn.core;


/**
 * Data source.
 *
 * @param <S> the type of source
 * @author tracy
 * @since 0.2.1
 */
public interface Source<S> {

    /**
     * Get the source.
     * @return The source of this.
     */
    S getSource();

    /**
     * Whether the source has been read.
     * @return true If the source has been read.
     */
    boolean isReadCompleted();

}
