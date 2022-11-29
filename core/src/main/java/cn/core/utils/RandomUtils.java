package cn.core.utils;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author tracy
 * @since 1.0.0
 */
public final class RandomUtils {
    private RandomUtils(){}

    private static final Random RANDOM = new Random();

    /**
     * 生成一个随机整数
     * @param min 下边界
     * @param max 上边界
     * @return 整数：[min, max]
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * 生成一个随机小数
     * @param min 下边界
     * @param max 上边界
     * @return 小数：[min, max)
     */
    public static float randomFloat(float min, float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }
}
