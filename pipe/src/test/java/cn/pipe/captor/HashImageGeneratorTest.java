package cn.pipe.captor;

import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
import cn.pipe.BufferedImageComparer;
import cn.pipe.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashImageGeneratorTest {

    @Test
    public void test_generate() throws NoSuchAlgorithmException {
        // given
        String content = "test";
        String algorithm = "SHA-256";
        int times = 2;
        int gridVerticalNum = 7;
        Color bgColor = Color.WHITE;
        Color fgColor = Color.BLACK;

        // when
        HashImageGenerator generator = new HashImageGenerator.Builder(content, algorithm, times)
                .gridVerticalNum(gridVerticalNum)
                .bgColor(bgColor)
                .fgColor(fgColor)
                .build();
        BufferedImage target = generator.generate();

        // then
        // get the digest
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        for (int i = 0; i < times; i++) {
            messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
        }
        byte[] digest = messageDigest.digest();
        // generate image
        BufferedImage image = BufferedImageUtils.newBackgroundImage(gridVerticalNum, gridVerticalNum, bgColor);
        // paint foreground color
        int gridHorizontalNum = gridVerticalNum / 2 + 1;
        for (int i = 0; i < gridHorizontalNum; i++) {
            for (int j = 0; j < gridVerticalNum; j++) {
                int posIndex = gridVerticalNum * i + j;
                if ((digest[posIndex] & 1) == 1) {
                    image.setRGB(i, j, fgColor.getRGB());
                    image.setRGB(gridVerticalNum - i - 1, j, fgColor.getRGB());
                }
            }
        }

        Assert.assertTrue(BufferedImageComparer.isSame(image, target));
    }

    public static class BuilderTest {

        @Test
        public void test_build_correctUsage_withContent() throws NoSuchAlgorithmException {
            // given
            String content = "any keyword";
            String algorithm = "MD5";
            int times = 4;
            int gridNum = 5;
            Color bgColor = Color.RED;
            Color fgColor = Color.GRAY;

            // when
            HashImageGenerator.Builder builder = new HashImageGenerator.Builder(content, algorithm, times)
                    .gridVerticalNum(gridNum)
                    .bgColor(bgColor)
                    .fgColor(fgColor);
            HashImageGenerator generator = builder.build();

            // then
            Assert.assertEquals(gridNum, ReflectionUtils.get("gridVerticalNum", generator));
            Assert.assertEquals(bgColor, ReflectionUtils.get("bgColor", generator));
            Assert.assertEquals(fgColor, ReflectionUtils.get("fgColor", generator));

            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            for (int i = 0; i < times; i++) {
                messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
            }
            byte[] digest = messageDigest.digest();
            Assert.assertArrayEquals(digest, (byte[]) ReflectionUtils.get("digest", generator));
        }

        @Test
        public void test_build_correctUsage_withDigestArray() {
            // given
            byte[] digest = {
                    (byte) 0xf1, (byte) 0xf2, (byte) 0xf3, (byte) 0xf4, (byte) 0xf5, (byte) 0xf6,
                    (byte) 0xe1, (byte) 0xe2, (byte) 0xe3, (byte) 0xe4, (byte) 0xe5, (byte) 0xe6,
                    (byte) 0xd1, (byte) 0xd2, (byte) 0xd3, (byte) 0xd4, (byte) 0xd5, (byte) 0xd6};
            int gridNum = 6;
            Color bgColor = Color.RED;
            Color fgColor = Color.GRAY;

            // when
            HashImageGenerator.Builder builder = new HashImageGenerator.Builder(digest)
                    .gridVerticalNum(gridNum)
                    .bgColor(bgColor)
                    .fgColor(fgColor);
            HashImageGenerator generator = builder.build();

            // then
            Assert.assertEquals(gridNum, ReflectionUtils.get("gridVerticalNum", generator));
            Assert.assertEquals(bgColor, ReflectionUtils.get("bgColor", generator));
            Assert.assertEquals(fgColor, ReflectionUtils.get("fgColor", generator));
            Assert.assertArrayEquals(digest, (byte[]) ReflectionUtils.get("digest", generator));
        }

        @Test
        public void test_build_correctUsage_withDefaultParam() throws NoSuchAlgorithmException {
            String content = "default";
            int gridNum = 5;

            // when
            HashImageGenerator.Builder builder = new HashImageGenerator.Builder(content)
                    .gridVerticalNum(gridNum);
            HashImageGenerator generator = builder.build();

            // then assert default value
            // default background color = [220, 220, 220]
            Assert.assertEquals(ColorUtils.of(220, 220, 220).getRGB(),
                    ((Color)ReflectionUtils.get("bgColor", generator)).getRGB());
            // default foreground color is random
            Assert.assertNotNull(ReflectionUtils.get("fgColor", generator));
        }

        @Test
        public void testEx_build_notSetParam() throws NoSuchAlgorithmException {
            // not set vertical grid num
            HashImageGenerator.Builder builder1 = new HashImageGenerator.Builder("digest")
                    .bgColor(Color.RED)
                    .fgColor(Color.GRAY);
            InvalidSettingException ex1 = Assert.assertThrows(InvalidSettingException.class,
                    builder1::build);
            Assert.assertEquals("The vertical number of grids is out of bounds:[1, 8].",
                    ex1.getMessage());

            // not set digest array
            HashImageGenerator.Builder builder2 = new HashImageGenerator.Builder((byte[]) null)
                    .gridVerticalNum(5)
                    .bgColor(Color.RED)
                    .fgColor(Color.GRAY);
            NullPointerException ex2 = Assert.assertThrows(NullPointerException.class,
                    builder2::build);
            Assert.assertEquals("Empty digest.",
                    ex2.getMessage());
        }

        @Test
        public void testEx_build_noSuchAlgorithm() {
            NoSuchAlgorithmException ex = Assert.assertThrows(NoSuchAlgorithmException.class,
                    () -> new HashImageGenerator
                            .Builder("digest", "non-existent-algorithm"));
            Assert.assertEquals("non-existent-algorithm MessageDigest not available",
                    ex.getMessage());
        }

        @Test
        public void testEx_build_notEnoughDigestArray() throws NoSuchAlgorithmException {
            // The length of digest array is 16 when algorithm set to MD5.
            HashImageGenerator.Builder builder = new HashImageGenerator
                    .Builder("digest", "MD5");

            // The size of grids is 36 when gridVerticalNum set to 6.
            builder.gridVerticalNum(6);

            // At least 18 [36/2] element are required, but only 16.
            InvalidSettingException ex = Assert.assertThrows(InvalidSettingException.class,
                    builder::build);
            Assert.assertEquals("The length of the digest array is less than half " +
                    "the number of grids.Expected length is at least 18, but the actual " +
                    "length is only 16.",
                    ex.getMessage());
        }

    }

}