
[**简体中文**](FAQ.md)

------

## Frequently Asked Questions

### What is the relationship between *imglib* and *Thumbnailator*?

From a certain point of view, *imglib* and *Thumbnailator* are independent of each other, *imglib* has not modified 
any API of *Thumbnail*, but only extended some capabilities on the basis of it, and all these extensions can be found 
in module [**ext-t8s**](/ext-t8s).

In addition, *imglib* provides a very convenient way to use *Thumbnailator*, as follows:
```java
ImagePipes.ofEmptySource()
        ...  
        .toThumbnails()
        ...
        .toFile("output filename");
```


### Why am I getting `OutOfMemoryError` and what can I do about it?

OutOfMemoryError is an eternal topic. At this stage of the project, as long as the principles adopted are not 
OutOfMemoryError caused by memory leaks, they can only provide a roundabout solution. I am sorry for this.
Examples are listed below.

1. Sometimes we may need to load or create an image with a large size, which will take up a lot of memory space.
  Usually, the information of a pixel is represented by 4 bytes of `ARGB`, so its approximate memory usage can 
  be calculated by `width * height * 4`.For example, a 1000\*1000 pixels image requires 4 MB 
  (1000 * 1000 * 4 / 1000 / 1000) memory of heap.
   
If the Java Virtual Machine does not have enough heap allocated to it, you'll likely encounter 
`OutOfMemoryError`s. 

You'll need to adjust the JVM heap memory based on the expected memory sizes that your application will take up.
How to configure the JVM heap is outside the scope of the Thumbnailator project. Please consult your JVM 
documentation on how to configure it.
