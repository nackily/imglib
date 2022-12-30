package cn.core.utils;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class StringUtilsTest {

    @Test
    public void test_isEmpty() {
        boolean empty = StringUtils.isEmpty("");
        Assert.assertTrue(empty);

        boolean notEmpty = StringUtils.isEmpty("any");
        Assert.assertFalse(notEmpty);
    }

    @Test
    public void test_isNotEmpty() {
        boolean notEmpty = StringUtils.isNotEmpty("any");
        Assert.assertTrue(notEmpty);

        boolean empty = StringUtils.isNotEmpty("");
        Assert.assertFalse(empty);
    }

    @Test
    public void test_join_array_withNull() {
        String join = StringUtils.join(new String[0]);
        Assert.assertEquals("", join);
    }

    @Test
    public void test_join_array() {
        String join = StringUtils.join(new String[]{"wha", "te", "ver"});
        Assert.assertEquals("whatever", join);
    }

    @Test
    public void test_join_array_withNull_withSeparator() {
        String join = StringUtils.join(new String[0], ",");
        Assert.assertEquals("", join);
    }

    @Test
    public void test_join_array_withSeparator() {
        String join = StringUtils.join(new String[]{"chen", "wang", "jiang"}, ",");
        Assert.assertEquals("chen,wang,jiang", join);
    }


    @Test
    public void test_join_collection_withNull() {
        String join = StringUtils.join(Collections.emptyList());
        Assert.assertEquals("", join);
    }

    @Test
    public void test_join_collection() {
        String join = StringUtils.join(Arrays.asList("he", "ll", "o"));
        Assert.assertEquals("hello", join);
    }

    @Test
    public void test_join_collection_withNull_withSeparator() {
        String join = StringUtils.join(Collections.emptyList(), ",");
        Assert.assertEquals("", join);
    }

    @Test
    public void test_join_collection_withSeparator() {
        String join = StringUtils.join(Arrays.asList("first", "second", "third"), "-");
        Assert.assertEquals("first-second-third", join);
    }

    @Test
    public void test_join_iterable_withNull_withSeparator() {
        // given
        StringBuilder sb = new StringBuilder();
        Iterable<String> iterable = new LinkedList<>();

        // when
        StringUtils.join(iterable, ",", sb);

        // then
        Assert.assertTrue(StringUtils.isEmpty(sb.toString()));
    }

    @Test
    public void test_join_iterable_withSeparator() {
        // given
        StringBuilder sb = new StringBuilder();
        Iterable<String> iterable = () -> {
            List<String> list = new LinkedList<>();
            list.add("1");
            list.add("2");
            list.add("3");
            return list.iterator();};

        // when
        StringUtils.join(iterable, "|", sb);

        // then
        Assert.assertEquals("1|2|3", sb.toString());
    }


    @Test
    public void test_getExtensionName() {
        String extension = StringUtils.getExtensionName("/folder/file.stg");

        Assert.assertEquals("stg", extension);
    }

    @Test
    public void test_getExtensionName_withEmptyFileName() {
        String extension = StringUtils.getExtensionName((String) null);
        Assert.assertNull(extension);
    }


    @Test
    public void test_getExtensionName_withNoDot() {
        String extension = StringUtils.getExtensionName("/folder/file");

        Assert.assertNull(extension);
    }

    @Test
    public void test_getExtensionName_withMultipleDots() {
        String extension = StringUtils.getExtensionName("/folder.font/song.ft");

        Assert.assertEquals("ft", extension);
    }
}