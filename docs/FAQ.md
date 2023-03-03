
[**English**](FAQ.en.md)

------

## 常见问题

### *Imglib* 和 *Thumbnailator* 之间是什么关系？

从某种程度上说，Imglib 和 *Thumbnailator* 之间相互独立，Imglib 并未对 *Thumbnailator* 作出修改，而仅仅只是在其基础上扩展了
部分能力，所有这些扩展内容都可以在[**ext-t8s**](/ext-t8s)中找到。

除此之外，*imglib* 提供了非常便捷的方式来使用 *Thumbnailator*，如下所示：
```java
ImagePipes.ofEmptySource()
        ...  
        .toThumbnails()
        ...
        .toFile("output filename");
```


### 为什么会 `OutOfMemoryError`，为此我能做些什么？

OOM 是一个永恒的话题，在项目的这个阶段，所采取的原则只要不是内存泄漏导致的 OOM，都只能提供迂回的解决方案，对此我表示很抱歉。例如：

1. 有时我们可能需要加载或者创建一张尺寸很大的图像，这将会占用很大的内存空间。通常情况下，一个像素点的信息由`ARGB`4个字节表示，
  所以可以通过`width * height * 4`来计算它的近似内存占用。例如一幅 1000px\*1000px 的图像，需要 4 MB
  （1000 * 1000 * 4 / 1000 / 1000）的内存占用。

如果 Java 虚拟机没有足够的内存分配，您可能会遇到 OutOfMemoryErrors。 您需要根据应用程序将要处理的图像大小来调整 JVM 
堆内存。

关于如何配置 JVM 堆超出了 Imglib 项目的范围。请查阅 JVM 文档以了解如何配置它。
