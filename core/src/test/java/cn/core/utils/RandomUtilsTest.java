package cn.core.utils;

import org.junit.Assert;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void test_randomInt() {
        int randomInt = RandomUtils.randomInt(1, 8);
        Assert.assertTrue(randomInt <= 8);
        Assert.assertTrue(randomInt >= 1);
    }

    @Test
    public void test_randomFloat() {
        float randomFloat = RandomUtils.randomFloat(0.2f, 2.7f);
        Assert.assertTrue(randomFloat <= 2.7f);
        Assert.assertTrue(randomFloat >= 0.2f);
    }
}