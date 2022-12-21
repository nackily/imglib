package cn.example;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * 功能设定
 *
 * @author tracy
 * @since 0.2.1
 */
public enum Setting {

    /**
     * 生成透明背景图像
     */
    GENERATE_TRANSPARENT_IMG("C", "1", ImagePipeExamples::generateTransparentImg),

    /**
     * 生成纯色背景图像
     */
    GENERATE_MONO_COLOR_IMG("C", "2", ImagePipeExamples::generateMonoColorImg),

    /**
     * 截取屏幕图像
     */
    GENERATE_SCREENSHOT("C", "3", ImagePipeExamples::generateScreenshot),

    /**
     * 生成哈希图像
     */
    GENERATE_HASH_IMG("C", "4", ImagePipeExamples::generateHashImg),

    /**
     * 从PDF文件中提取图像
     */
    EXTRACT_IMG_FROM_PDF("C", "5", ImagePipeExamples::extractImgFromPdf),

    /**
     * 从GIF文件中提取图像
     */
    EXTRACT_IMG_FROM_GIF("C", "6", ImagePipeExamples::extractImgFromGif),

    /**
     * 拆分图像
     */
    SPLIT_IMG("Y", "1", ImagePipeExamples::splitImg),

    /**
     * 合并图像
     */
    MERGE_IMG("Y", "2", ImagePipeExamples::mergeImg),

    /**
     * 合并图像为GIG
     */
    MERGE_TO_GIF("Y", "3", ImagePipeExamples::mergeToGif),

    /**
     * 添加边框
     */
    ADD_BORDER("T", "1", ThumbnailExamples::addBorder),

    /**
     * 马赛克
     */
    MOSAIC("T", "2", ThumbnailExamples::mosaic),

    /**
     * 圆角化
     */
    ROUND_RECT("T", "3", ThumbnailExamples::roundRect),

    /**
     * 无损扩大
     */
    EXPAND("T", "4", ThumbnailExamples::expand),

    /**
     * 灰度化
     */
    GRAYING("T", "5", ThumbnailExamples::graying),

    /**
     * 二值化
     */
    BINARY("T", "6", ThumbnailExamples::binary),

    /**
     * 叠加线条
     */
    OPENING_SHAPE("T", "7", ThumbnailExamples::paintOpeningShape),

    /**
     * 叠加形状
     */
    CLOSED_SHAPE("T", "8", ThumbnailExamples::paintClosedShape)
    ;

    final String type;
    final String key;
    final Function func;
    Setting(String type, String key, Function func) {
        this.type = type;
        this.key = key;
        this.func = func;
    }

    public static Function getFunc(String type, String key) {
        Setting[] values = Setting.values();
        for (Setting val : values) {
            if (val.type.equalsIgnoreCase(type) && val.key.equals(key)) {
                return val.func;
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface Function {
        void apply() throws IOException, NoSuchAlgorithmException;
    }
}