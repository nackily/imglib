package cn.core.utils;

import java.util.Random;

/**
 * An util class for random。
 *
 * @author tracy
 * @since 0.2.1
 */
public final class RandomUtils {
    private RandomUtils(){}

    private static final Random RANDOM = new Random();

    /**
     * Get a random integer.
     *
     * @param min The lower limit of the value range.
     * @param max The upper limit of the value range.
     * @return The random integer which limited to [min, max].
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * Get a random float.
     *
     * @param min 下边界
     * @param max 上边界
     * @return The random integer which limited to [min, max).
     */
    public static float randomFloat(float min, float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }
}
