
[**中文文档**](FAQ-zh.md)
---

## Frequently Asked Questions

### What is the relationship between *imglib* and *Thumbnailor*?

From a certain point of view, *imglib* and *Thumbnailor* are independent of each other, imglib has not modified any API of *Thumbnail*, but only extended some capabilities on the basis of it, and all these extensions can be found in module [**ext-t8s**](/ext-t8s).

In addition, *imglib* provides a very convenient way to use *Thumbnailor*, as follows:
```java
ImagePipes.ofEmptySource()
        ...  
        .toThumbnails()
        ...
        .toFile("output filename");
```
