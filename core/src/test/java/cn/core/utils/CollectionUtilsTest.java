package cn.core.utils;

import cn.core.ex.InvalidSettingException;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CollectionUtilsTest {

    @Test
    public void test_isNullOrEmpty_emptyCollection() {
        boolean nullOrEmpty = CollectionUtils.isNullOrEmpty(Collections.emptyList());
        Assert.assertTrue(nullOrEmpty);
    }

    @Test
    public void test_isNullOrEmpty_emptyArray() {
        boolean nullOrEmpty = CollectionUtils.isNullOrEmpty(new Object[0]);
        Assert.assertTrue(nullOrEmpty);
    }

    @Test
    public void testEx_excEmpty_emptyCollection() {
        List<Object> collection = Collections.emptyList();
        String exceptionMessage = "Empty collection.";

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> CollectionUtils.excEmpty(collection, exceptionMessage));
        Assert.assertEquals(exceptionMessage,
                exception.getMessage());
    }

    @Test
    public void testEx_excEmpty_emptyArray() {
        String exceptionMessage = "Empty array.";

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> CollectionUtils.excEmpty(new Object[0], exceptionMessage));
        Assert.assertEquals(exceptionMessage,
                exception.getMessage());
    }

    @Test
    public void testEx_excEmpty_emptyIterable() {
        String exceptionMessage = "Empty iterable.";
        Iterable<String> iterable = new ArrayList<>();

        InvalidSettingException exception = Assert.assertThrows(InvalidSettingException.class,
                () -> CollectionUtils.excEmpty(iterable, exceptionMessage));
        Assert.assertEquals(exceptionMessage,
                exception.getMessage());
    }

}