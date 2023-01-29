
[**English**](Dependencies.en.md)

------

*imglib* 的模块及功能划分如下表所示：

|       模块       |    功能简述    |           内部依赖关系            |                 第三方依赖项                  |             引入第三方依赖背景              |
|:--------------:|:----------:|:---------------------------:|:---------------------------------------:|:----------------------------------:|
|  imglib-core   |     -      |              -              |                    -                    |                 -                  |
| imglib-ext-t8s |    图像处理    |         imglib-core         |              thumbnailator              | 针对 thumbnailator 提供的扩展，包括一系列的图像处理器 |
|  imglib-pipe   | 图像收集、聚合与分裂 |         imglib-core         |        pdfbox, animated-gif-lib         |       PDF 文档的解析，GIF 文档的编码解码        |
|   imglib-all   |  流畅的任务构建器  | imglib-ext-t8s, imglib-pipe | thumbnailator, pdfbox, animated-gif-lib |                 -                  |

正如在前面提到的那样，*imglib* 并不是一个从零开始的项目，它只是站在巨人的肩膀上！它在图像处理的能力上立足于
[Thumbnailator](https://github.com/coobird/thumbnailator)，在 PDF 文档的解析上依赖了 [pdfbox](https://github.com/apache/pdfbox)，
在 GIF 文档的编码解码上依赖了 [animated-gif-lib-for-java](https://github.com/rtyley/animated-gif-lib-for-java)。
尽管在设计上我们借鉴了上述优秀的第三方库，但这并不意味着在使用中我们也必须声明所有的依赖项，事实上您完全可以按需引入。

```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("userkey")
            .gridVerticalNum(8)
            .build())
        .toFile("...avatar.png");
```

当我们想要实现生成用户头像时，上述代码片段即可完成任务，此时我们仅需要引入 *imglib-all* 依赖即可。

```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>0.2.9</version>
  </dependency>
```

#### thumbnailator
```java
ImagePipes.of(...).toThumbnails()
        .addFilter(new MosaicHandler.Builder()
            .height(160).width(180)
            .startX(480).startY(260)
            .sideLength(10)
            .build())
        .scale(1.0)
        .toFile("out.jpg");
```

如果我们需要对图像进行马赛克化处理，则需要引入 *net.coobird#thumbnailator* 的依赖包，此时我们应调整依赖为如下。

```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>0.2.9</version>
  </dependency>

  <dependency>
    <groupId>net.coobird</groupId>
    <artifactId>thumbnailator</artifactId>
    <version>0.4.8</version>
  </dependency>
```

*任何时候，只要您调用了 `AbstractSourceBuilder#toThumbnails()` 方法都应该添加 Thumbnailator 的依赖。*

#### animated-gif-lib
```java
ImagePipes.of("frame1.jpg",
            "frame2.jpg",
            "frame3.jpg",
            "frame4.jpg")
        .toFile(new GifFileEncoder.Builder()
            .filename("out.gif")
            .delay(400)
            .repeat(0)
            .build());
```

当我们需要将多幅图像写入到 GIF 格式中时，我们则需要添加 GIF 格式编解码的依赖 *com.madgag#animated-gif-lib*。

```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>0.2.9</version>
  </dependency>

  <dependency>
    <groupId>com.madgag</groupId>
    <artifactId>animated-gif-lib</artifactId>
    <version>1.4</version>
  </dependency>
```

#### pdfbox
```java
ImagePipes.ofPdf("file.pdf")
        .register(2)
        .dpi(280)
        .toFile("page_2.jpg");
```

有些时候，我们希望能从 PDF 文件中提取某页为图像，就需要添加 PDF 解析的依赖 *org.apache.pdfbox#pdfbox*。

```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>0.2.9</version>
  </dependency>

  <dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>2.0.24</version>
  </dependency>
```