<p align="center">
	<img src="/docs/res/imglib-logo.png" width="40%"></a>
</p>


<p align="center">
	<strong>A lightweight image processing library</strong>
</p>
<br>

<p align="center">
    <a target="_blank" href="https://search.maven.org/artifact/io.github.nackily/imglib-all">
		<img src="https://img.shields.io/maven-central/v/io.github.nackily/imglib-all?color=blue" 
              alt="maven-central"/>
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/jdk-8%2B-green" 
              alt="jdk8+"/>
	</a>
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0">
        <img src="https://img.shields.io/badge/license-Apache%202-blue" 
              alt="license-Apache2">
    </a>
</p>
<br>

---

[**English Documentation**](README.md)

---

## *imglib* 是什么?

*imglib* 是一个轻量级的 JAVA 图像处理库，立足于简化对图像的常见处理。

## *imglib* 能做什么？

*imglib* 主要提供三部分的能力：

* **图像收集**

  *imglib* 提供了图像收集的能力，允许开发者从无到有创建图像，例如创建透明图像、hash 点阵图像、截取屏幕图像等。同时开发者可从文件中提取图像，例如从 PDF 文件中提取整页图像，从 GIF 文件中提取图像帧等。

* **图像处理**

  借助于 *Thumbnailator*，我们能轻松实现图像的基础操作，包括缩放、裁剪、旋转、图像水印和格式转换等等。在此基础上，*imglib* 扩展了添加边框、无损放大、马赛克、圆角、灰度化、二值化、绘制形状等处理器。

* **聚合与分裂**

  *imglib* 提供了图像的聚合与分裂能力，包括归并多个图像为单个图像，以及拆分图像为多个图像。支持按照网格对图像进行剪切和拼图，合并多个图像到 GIF 文件中，开发者还可根据实际需求，进行自由扩展。

值得一提的是，*imglib* 并不是一个从零开始的项目，它只是站在巨人的肩膀上！*imglib* 在图像处理的能力上立足于 [Thumbnailator](https://github.com/coobird/thumbnailator)，在 PDF 文档的解析上依赖了 [pdfbox](https://github.com/apache/pdfbox)，在 GIF 文档的处理上引用了 [animated-gif-lib-for-java](https://github.com/rtyley/animated-gif-lib-for-java)...

## *imglib* 有多简单？
同 *Thumbnailator* 一样，*imglib* 为开发者屏蔽了复杂的 I/O 操作，以及无需再通过 Graphics2D 对象来手动操作图像，*imglib* 已经替您完成了所有这些工作。*imglib* 的链式 API 使得你可以将一个复杂的图像处理任务逐步的配置并执行。

例如，为用户创建一个 hash 图像，头像点阵设定为 8px\*8px，头像大小为 300px\*300px，并增加一个边距为 20px 的边框。该图像生成任务，可以通过以下操作完成：
```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("Tracy")       // hash 图像生成器
                .gridVerticalNum(8)                             // 水平方向的点阵数量
                .bgColor(ColorUtils.of(240, 240, 240))          // 背景色
                .fgColor(ColorUtils.of(50, 150, 50))            // 前景色
                .build())   
        .toThumbnails()                                         // 转换为 Thumbnails
        .addFilter(new HighQualityExpandHandler.Builder()       // 图像无损放大处理器
                .finalWidth(300)                                // 图像的宽度
                .keepAspectRatio(true)                          // 保持长宽比例
                .build())   
        .addFilter(new BorderHandler.Builder()                  // 边框处理器
                .fillColor(ColorUtils.of(200, 200, 200))        // 边框填充色
                .vMargins(20)                                   // 垂直方向的边距
                .hMargins(20)                                   // 水平方向的边距
                .build())
        .scale(1.0)
        .toFile(".../avatar.png");
```

执行上面的代码片段，我们将得到如下的用户头像。

<div align="center">
   <img src="docs/res/avatar.png" width="18%"/>
</div>

## *imglib* 如何引入？

**Maven**

在项目的 pom.xml 中添加以下 maven 依赖：
```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>{*.*.*}</version>
</dependency>
```

**Jar**

您也可以访问 [**maven-repository**](https://repo1.maven.org/maven2/io/github/nackily/imglib-all/) ，下载对应版本的`imglib-all-*.*.*.jar`并引入到项目中即可。

## 更多内容
以下的链接提供了更多关于 *imglib* 信息：

+ [**Examples**](/docs/Examples-zh.md)
+ [**API Documentation**](/docs/APIs-zh.md)
+ [**Frequently Asked Questions**](/docs/FAQ-zh.md)
