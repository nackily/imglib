
---

[**中文文档**](Examples-zh.md)

---

This article will list some examples so that developers can quickly understand the functions of imglib and easily use them. All these examples can be found in [example/.../Setting.java](/example/src/main/java/cn/example/Setting.java), and you can run the **example** module to execute the corresponding functions.

## Image Collection

### Create a transparent image
```java
ImagePipes.ofEmptySource()
        .register(new TransparentImageGenerator.Builder()
             .width(300)
             .height(200)
             .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/transparent.png"));
```


In this example, an image with size of 300px \* 200px and transparent background will be created and saved to the file of `.../out/transparent.png`.

### Create a mono color image
```java
ImagePipes.ofEmptySource()
        .register(new MonoColorImageGenerator.Builder()
            .width(300)
            .height(200)
            .alpha(0.2f)
            .color(ColorUtils.random())
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/mono_color.png"));
```

In this example, an image with size of 300px \* 200px, transparency of 0.2 and random background color will be created and saved to file of `.../out/mono_color.png`. Tip: the transparency value is between \[0, 1\], when it is equal to 0, the image is completely opaque, and when it is equal to 1, the image is completely transparent.

### Take a screenshot
```java
ImagePipes.ofEmptySource()
        .register(new ScreenshotGenerator.Builder()
            .startPoint(600, 300)
            .width(280)
            .height(190)
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/screenshot.png"));
```

In this example, a screenshot will be captured. The screenshot range is a rectangular area with the upper left corner coordinate of \[600, 300\], width of 280px and height of 190px. The captured screenshot will be saved to the file of `.../out/screenshot.png`.

### Create a hash image
```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("imglib-user")
            .gridVerticalNum(5)
            .fgColor(ColorUtils.of(50, 150, 50))
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/hash.png"));
```

In this example, a two-dimensional matrix with size of 5px \* 5px will be created. If the value of the pixel at the corresponding position in the matrix is true, the foreground color of the pixel will be set to \[(R) 50, (G) 150, (B) 50\], and the dot matrix image will be saved to the file of `.../out/hash.png`.

### Extract image from page of PDF file
```java
ImagePipes.ofPdf(ExampleUtils.tmpFileNameOf("in/jvms8.pdf"))
        .register(0)
        .dpi(280)
        .toFile(ExampleUtils.tmpFileNameOf("out/page_1_of_jvms8.jpg"));
```

In this example, the specified pdf file of `.../in/jvms8.pdf` will be parsed, and the page whose index is 0 is extracted as an image, and the DPI (pixels per inch) is set to 280 when extracting the image, and the image is saved to the file of `.../out/page_1_of_jvms8.jpg`.

### Extract image from frame of GIF file
```java
ImagePipes.ofGif(ExampleUtils.tmpFileNameOf("in/duck.gif"))
        .registerAll()
        .toFiles(ExampleUtils.tmpFileNameOf("out/gif/frame_1.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_2.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_3.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_4.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_5.jpg"));
```

In this example, the specified gif file of `.../in/duck.gif` will be parsed, all frames of the gif file will be extracted as a list of images, and these images will be saved to file directory of `.../out/gif/` one by one in the order of frames.

## Merging and Splitting

### Split image with grid
```java
ImagePipes.of(ExampleUtils.tmpFileNameOf("in/before_split.jpg"))
        .addFilter(new GridSplitHandler.Builder()
            .gridWidth(400)
            .gridHeight(250)
            .build())
        .toFiles(ExampleUtils.tmpFileNameOf("out/split/slice_1.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_2.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_3.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_4.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_5.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_6.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_7.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_8.jpg"),
            ExampleUtils.tmpFileNameOf("out/split/slice_9.jpg"));
```

In this example, a rectangular area with size of 400px \* 250px will be used to extract images from the original image line by line from left to right until the rectangle touches (or contains) the image boundary, and the extracted images will be saved to the file directory of `.../out/split/` in order.

### Merge images with grid
```java
ImagePipes.of(ExampleUtils.tmpFileNameOf("in/to_merge/spring.jpg"),
            ExampleUtils.tmpFileNameOf("in/to_merge/summer.jpg"),
            ExampleUtils.tmpFileNameOf("in/to_merge/winter.jpg"),
            ExampleUtils.tmpFileNameOf("in/to_merge/autumn.jpg"))
        .addFilter(new GridMergeHandler.Builder()
            .horizontalNum(2)
            .fillColor(ColorUtils.of(240, 240, 240))
            .gridWidth(530)
            .gridHeight(530)
            .autoAdapts(true)
            .alignCenter(true)
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/after_merge.jpg"));
```

In this example, four images `spring.jpg`,`summer.jpg`,`winter.jpg`,`autumn.jpg` will be loaded from the file directory of `.../in/to_merge/`, and these images will be in order splicing in one image, the splicing rules are as follows:

+ use a grid with a fixed size of 530px \* 530px to hold each image;
+ enable automatic grid adjustment, reset the size of the grid when the grid is not enough to accommodate an image;
+ set image center alignment, including horizontal and vertical;
+ the number of grids placed in each row is set to 2;
+ the blank part is filled with the color \[(R) 240, (G) 240, (B) 240\] if an image cannot cover the entire grid area;

The stitched image will be saved to file of `.../out/after_merge.jpg`.

### Encode image(s) to GIF file
```java
ImagePipes.of(ExampleUtils.tmpFileNameOf("in/to_merge/spring.jpg"),
            ExampleUtils.tmpFileNameOf("in/to_merge/summer.jpg"),
            ExampleUtils.tmpFileNameOf("in/to_merge/winter.jpg"),
            ExampleUtils.tmpFileNameOf("in/to_merge/autumn.jpg"))
        .toFile(new GifFileEncoder.Builder()
            .filename(ExampleUtils.tmpFileNameOf("out/seasons.gif"))
            .delay(400)
            .repeat(0)
            .build());
```

In this example, four images of `spring.jpg`,`summer.jpg`,`winter.jpg`,`autumn.jpg` will be loaded from the file directory of `.../in/to_merge/` and encoded into the file of `.../out/seasons.gif`, the output file switches to the next frame every 400ms, and has been set to repeat mode.

## Image Processing

// todo