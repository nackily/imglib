
<p align="center">
	<strong>A lightweight image processing library.</strong>
</p>

---
[**English**](README-en.md)
---

# imglib 是什么?
imglib 是一个轻量级的 JAVA 图像处理库，旨在快速实现对图像的常用操作。imglib 在整体设计上基于 Thumbnailator，但这并不意外着 imglib 必须依赖 Thumbnailator。 另外，imglib 提供了从某些格式的文件中提取图像的能力，为此引入了其他优秀的开源项目，例如 pdfbox。 在下图中描述了 imglib 与其他开源项目之间的关系：

// todo

# imglib 能做什么？

## 图像生成
imglib 提供了图像生成的功能，包括有创建透明背景图像，创建纯色背景图像，创建 hash 点阵图像，截取屏幕图像，从文件中（PDF）提取页为图像等。

## 图像处理
借助于 Thumbnailator 提供的能力，我们能轻松实现图像的基础操作，包括设定图像的宽高、裁剪、缩放、旋转、图像水印、格式转换等等。在此基础上，imglib 扩展了添加边框、高质量的放大、马赛克、圆角化、灰度化、二值化、绘制形状等功能。

## 图像转换
imglib 提供了图像的转换能力，包括合并多个图像为一幅图像和拆分一幅图像为多个图像。当前已实现按照网格拆分和合并，支持开发者自由扩展。

# imglib 如何使用？

1）生成一个 hash 头像

为用户生成 hash 图像，头像点阵大小设定为 8px\*8px，生成后放大到 300px\*300px，并且为头像提供一个背景色 RGB=(200,200,200) ，边距为 20px 的边框。
```java
Captors.ofEmptySource()
        .addLast(new HashImageGenerator.Builder("Tracy")    // hash 图像生成器
                .gridVerticalNum(8)                         // 水平方向的像素点阵数量
                .bgColor(ColorUtils.of(240, 240, 240))      // 背景色
                .fgColor(ColorUtils.of(50, 150, 50))        // 前景色
                .build())   
        .newThumbnails()                                    // 转换为 Thumbnails
        .addFilter(new HighQualityExpandHandler.Builder()   // 图像高质量放大处理器
                .finalWidth(300)                            // 图像的宽度
                .keepAspectRatio(true)                      // 保持长宽比例
                .build())   
        .addFilter(new BorderHandler.Builder()              // 边框处理器
                .fillColor(ColorUtils.of(200, 200, 200))    // 边框填充色
                .vMargins(20)                               // 水平方向的边距
                .hMargins(20)                               // 垂直方向的边距
                .build())
        .scale(1.0)
        .toFile(".../avatar.png");
```
<div align="center">
   <img src="/example/src/main/resources/avatar/avatar.png" width="25%"/>
</div>


2) 从 pdf 中提取图像

