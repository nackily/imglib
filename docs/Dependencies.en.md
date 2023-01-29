
[**English**](Dependencies.md)

------

The module and function division of *imglib* are shown in the following table.

|     Module     |             Functions             |    Internal Dependencies    |        Third-party dependencies         |                Reasons for introducing third-party dependency                |
|:--------------:|:---------------------------------:|:---------------------------:|:---------------------------------------:|:----------------------------------------------------------------------------:|
|  imglib-core   |                 -                 |              -              |                    -                    |                                      -                                       |
| imglib-ext-t8s |         image processing          |         imglib-core         |              thumbnailator              | Extensions provided for Thumbnailor, including a series of image processors. |
|  imglib-pipe   | collection, merging and splitting |         imglib-core         |        pdfbox, animated-gif-lib         |         PDF document parsing and GIF document encoding and decoding.         |
|   imglib-all   |        Smooth task builder        | imglib-ext-t8s, imglib-pipe | thumbnailator, pdfbox, animated-gif-lib |                                      -                                       |

As mentioned earlier that *imglib* is not a project started from nothing, it just stands on the shoulders
of giants! *imglib* is based on [Thumbnailator](https://github.com/coobird/thumbnailator) in image processing,
relies on [pdfbox](https://github.com/apache/pdfbox) in PDF document parsing, and references
[animated-gif-lib-for-java](https://github.com/rtyley/animated-gif-lib-for-java) in GIF document processing.
Although we use the excellent third-party libraries mentioned above for reference in design, this does not 
mean that we must declare all dependencies in use. In fact, you can import them on demand.

```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("userkey")
            .gridVerticalNum(8)
            .build())
        .toFile("...avatar.png");
```

When we want to generate user avatars, the above code fragments can complete the task. At this time, 
we only need to introduce dependencies of *imglib-all*.

```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>0.2.9</version>
  </dependency>
```

#### thumbnailator
```java
Thumbnails.of("in.jpg")
        .addFilter(new MosaicHandler.Builder()
            .height(160).width(180)
            .startX(480).startY(260)
            .sideLength(10)
            .build())
        .toThumbnails()
            .scale(1.0)
            .toFile("out.jpg");
```

If we need to mosaic the image, we need to introduce the dependency package of *net.coobird#thumbnailor*. 
At this time, we should adjust the dependency to the following.

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

*At any time, as long as you call the method of 'AbstractSourceBuilder#toThumbnails()',
you should add the dependency of Thumbnailor.*

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

When we need to write multiple images into a GIF file, we need to add *com.madgag#animated-gif-lib*
for encoding and decoding of GIF format.

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

Sometimes, if we want to extract a page from a PDF file as an image, we need to add a PDF 
parsing dependency *org.apache.pdfbox#pdfbox*.

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