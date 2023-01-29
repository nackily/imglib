
<img src="/docs/res/imglib-logo.png" width="50%" syt height="50%" />

<strong>A lightweight image processing library</strong>

[![maven-central](https://img.shields.io/maven-central/v/io.github.nackily/imglib-all?color=blue)](https://search.maven.org/artifact/io.github.nackily/imglib-all)
[![jdk8+](https://img.shields.io/badge/jdk-8%2B-green)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[![license](https://img.shields.io/badge/license-Apache%202-blue)](https://www.apache.org/licenses/LICENSE-2.0)

[**简体中文**](README.md)

------

# What is *imglib*?

*Imglib* is a lightweight image processing library for JAVA, which committed to simplifying frequently image processing.

# What can *imglib* do?

*Imglib* mainly provides three capabilities:

* **Image Collection**

  *Imglib* provides the ability to collect images, allowing developers to create images from nothing, such as creating 
transparent image, hash image, and capturing screenshot. At the same time, developers can extract images from files, 
such as extract whole page as image from PDF file or extract frame as image from GIF file.

* **Image Processing**

  With *Thumbnailator*, we can easily implement the basic operations of images, including scaling, cropping, rotation, 
image watermarking and format conversion. On this basis, *imglib* extends processors such as adding border, lossless 
magnification, mosaics, rounded corner, graying, binarization, and drawing shapes on image.

* **Merging and Splitting**

  *Imglib* provides merging and splitting capabilities, including merging multiple images into a single image and 
splitting image into multiple images. It supports cutting and jigsaw of images according to grid, and encode multiple 
images into GIF file. Developers can also expand freely according to actual needs.

# How simple is *imglib*?
Like *Thumbnailator*, *imglib* shields developers from complex I/O operations and eliminates the need to manually 
manipulate images through Graphics2D objects. *imglib* has done all this for you. It's chained API allows you to 
configure and execute a complex image processing task step by step.

For example, the task of create a hash avatar for the user, the avatar setting to 8px\*8px, the image size setting 
to 300px\*300px, and add a border with a margin of 20px. This image generation task can be completed through the 
following code fragments:

```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("Tracy")       // add a hash image generator
                .gridVerticalNum(8)                             // number of lattice in horizontal direction
                .bgColor(ColorUtils.of(240, 240, 240))          // the background color of hash image
                .fgColor(ColorUtils.of(50, 150, 50))            // the foreground color of hash image
                .build())   
        .toThumbnails()                                         // get object of Thumbnails
        .addFilter(new HighQualityExpandHandler.Builder()       // add a filter of lossless expansion handler
                .finalWidth(300)                                // the final width after expanded
                .keepAspectRatio(true)                          // setting of keep the aspect ratio
                .build())   
        .addFilter(new BorderHandler.Builder()                  // add a filter of border handler
                .fillColor(ColorUtils.of(200, 200, 200))        // fill color of the border
                .vMargins(20)                                   // vertical margin
                .hMargins(20)                                   // horizontal margin
                .build())
        .scale(1.0)
        .toFile(".../avatar.png");
```

By executing the above code fragment, we will get the following user avatar.

[avatar](docs/res/avatar.png)

## How to use *imglib*?

**Maven**

Add the following maven dependencies in the pom.xml of your project.

```xml
  <dependency>
    <groupId>io.github.nackily</groupId>
    <artifactId>imglib-all</artifactId>
    <version>{*.*.*}</version>
  </dependency>
```

**Jar**

You can also visit [**maven-repository**](https://repo1.maven.org/maven2/io/github/nackily/imglib-all/), 
download the corresponding version of `imglib-all-*.*.*.jar` and import it into your project.

It is worth mentioning that *imglib* is not a project started from nothing, it just stands on the shoulders 
of giants! *imglib* is based on [Thumbnailator](https://github.com/coobird/thumbnailator) in image processing, 
relies on [pdfbox](https://github.com/apache/pdfbox) in PDF document parsing, and references 
[animated-gif-lib-for-java](https://github.com/rtyley/animated-gif-lib-for-java) in GIF document processing.
Although *imglib* relies on these excellent third-party dependencies in some image processing capabilities, 
*imglib* does not include relevant dependencies after packaging. If necessary, you should add corresponding 
dependencies to the project independently.Please refer to [Dependencies](/docs/Dependencies.md) for detailed
lists.

# See more
Get more information about *imglib* by visit the following links:

+ [**Examples**](/docs/Examples.en.md)
+ [**Dependencies**](/docs/Dependencies.en.md)
+ [**API Documentation**](/docs/APIs.en.md)
+ [**Frequently Asked Questions**](/docs/FAQ.en.md)
