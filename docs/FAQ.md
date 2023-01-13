
[**English**](FAQ.en.md)

------

## 常见问题

### *Imglib* 和 *Thumbnailator* 之间是什么关系？

从某种程度上说，imglib 和 *Thumbnailator* 之间相互独立，imglib 并未对 *Thumbnailator* 作出修改，而仅仅只是在其基础上扩展了部分能力，所有这些扩展内容都可以在[**ext-t8s**](/ext-t8s)中找到。

除此之外，*imglib* 提供了非常便捷的方式来使用 *Thumbnailator*，如下所示：
```java
ImagePipes.ofEmptySource()
        ...  
        .toThumbnails()
        ...
        .toFile("output filename");
```
