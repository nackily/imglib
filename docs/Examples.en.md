
[**简体中文**](Examples.md)

------

This article will list some examples so that developers can quickly understand the functions of *imglib* and easily use 
them. All these examples can be found in [example/.../Setting.java](/example/src/main/java/cn/example/Setting.java), 
and you can run the **example** module to execute the corresponding functions.

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


In this example, an image with size of 300px \* 200px and transparent background will be created and saved to the file
of `.../out/transparent.png`.

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

In this example, an image with size of 300px \* 200px, transparency of 0.2 and random background color will be created
and saved to file of `.../out/mono_color.png`. Tip: the transparency value is between \[0, 1\], when it is equal to 1, 
the image is completely opaque, and when it is equal to 0, the image is completely transparent.

<div align="center"><img src="/example/res/out/mono_color.png"/></div>

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

In this example, a screenshot will be captured. The screenshot range is a rectangular area with the upper left corner 
coordinate of \[600, 300\], width of 280px and height of 190px. The captured screenshot will be saved to the file of 
`.../out/screenshot.png`.

<div align="center"><img src="/example/res/out/screenshot.png"/></div>

### Create a hash image
```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("imglib-user")
            .gridVerticalNum(5)
            .fgColor(ColorUtils.of(50, 150, 50))
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/hash.png"));
```

In this example, a two-dimensional matrix with size of 5px \* 5px will be created. If the value of the pixel at the 
corresponding position in the matrix is true, the foreground color of the pixel will be set to \[(R) 50, (G) 150, (B) 50\], 
and the dot matrix image will be saved to the file of `.../out/hash.png`.

<div align="center"><img src="/docs/res/5x5hash_matrix.png"/></div>

For the convenience of observation, the displayed image is not the image `out/hash.png` but the enlarged image, because 
the generated image has just only 25 pixels.

### Extract image from page of PDF file
```java
ImagePipes.ofPdf(ExampleUtils.tmpFileNameOf("in/jvms8.pdf"))
        .register(0)
        .dpi(280)
        .toFile(ExampleUtils.tmpFileNameOf("out/page_1_of_jvms8.jpg"));
```

In this example, the specified pdf file of `.../in/jvms8.pdf` will be parsed, and the page whose index is 0 is extracted 
as an image, and the DPI (pixels per inch) is set to 280 when extracting the image, and the image is saved to the file 
of `.../out/page_1_of_jvms8.jpg`.

<div align="center"><img src="/example/res/out/page_1_of_jvms8.jpg" width="50%"/></div>

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

In this example, the specified gif file of `.../in/duck.gif` will be parsed, all frames of the gif file will be 
extracted as a list of images, and these images will be saved to file directory of `.../out/gif/` one by one in the 
order of frames.

|       original GIF file       |       list of extracted images       |
|:-----------------------------:|:------------------------------------:|
| ![](/example/res/in/duck.gif) | ![](/docs/res/all_parsed_frames.jpg) |

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

In this example, a rectangular area with size of 400px \* 250px will be used to extract images from the original image 
line by line from left to right until the rectangle touches (or contains) the image boundary, and the extracted images 
will be saved to the file directory of `.../out/split/` in order.

|         original image         |  list of images after split   |
|:------------------------------:|:-----------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/docs/res/all_slices.jpg) |

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
            .autoAdapts()
            .alignCenter()
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/after_merge.jpg"));
```

In this example, four images `spring.jpg`,`summer.jpg`,`winter.jpg`,`autumn.jpg` will be loaded from the file directory 
of `.../in/to_merge/`, and these images will be in order splicing in one image, the splicing rules are as follows:

+ use a grid with a fixed size of 530px \* 530px to hold each image;
+ enable automatic grid adjustment, reset the size of the grid when the grid is not enough to accommodate an image;
+ set image center alignment, including horizontal and vertical;
+ the number of grids placed in each row is set to 2;
+ the blank part is filled with the color \[(R) 240, (G) 240, (B) 240\] if an image cannot cover the entire grid area;

The stitched image will be saved to file of `.../out/after_merge.jpg`.

|        list of original images         |             merged image              |
|:--------------------------------------:|:-------------------------------------:|
| ![](/docs/res/all_to_merge_slices.jpg) | ![](/example/res/out/after_merge.jpg) |

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

In this example, four images of `spring.jpg`,`summer.jpg`,`winter.jpg`,`autumn.jpg` will be loaded from the file 
directory of `.../in/to_merge/` and encoded into the file of `.../out/seasons.gif`, the output file switches to the 
next frame every 400ms, and has been set to repeat mode.

|        list of original images         |    the GIF file after encoded     |
|:--------------------------------------:|:---------------------------------:|
| ![](/docs/res/all_to_merge_slices.jpg) | ![](/example/res/out/seasons.gif) |

## Image Processing

Although *Thumbnailator* is mainly used to process thumbnails, it still provides us with basic image processing 
capabilities, including size scaling, scaling, area cropping, rotation, and adding watermarks, etc. *Imglib* expands a 
series of new image processing capabilities on the basis of *Thumbnailator*, and all new functions implement the 
`ImageFilter` interface, which can be accessed through `Thumbnails.Builder#addFilter(ImageFilter)`, 
`Thumbnails.Builder#addFilters(List<ImageFilter>)` way to use.

### Add a border to the image
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new BorderHandler.Builder()
            .vMargins(30)
            .hMargins(20)
            .fillColor(ColorUtils.of(240, 230, 175))
            .build())
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/bordered.jpg"));
```

In this example, a border will be added to the image of `.../in/panda.jpg` with a margin of 20 px horizontally and 30 px
vertically, and the processed image will be saved to file of `.../out/bordered.jpg`.

|         original image         |          after add border          |
|:------------------------------:|:----------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/bordered.jpg) |

### Add a mosaic to the image
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new MosaicHandler.Builder()
            .height(160)
            .width(180)
            .startX(480)
            .startY(260)
            .sideLength(10)
            .build())
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/after_mosaic.jpg"));
```

In this example, a mosaic will be added to a part of the `.../in/panda.jpg` image.The coordinates of the upper left 
corner of the area are \[480, 260\], and the width and height are 180px and 160px respectively. The side length of each 
mosaic block is fixed at 10 px, and the processed image will be saved to the file of`.../out/after_mosaic.jpg`.

|         original image         |              after mosaic              |
|:------------------------------:|:--------------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/after_mosaic.jpg) |

### Rounded the corners of image
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new RoundRectHandler.Builder()
            .arcWidth(100)
            .arcHeight(100)
            .build())
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/rounded.png"));
```

In this example, the four corners of the original image `.../in/panda.jpg` will be changed to rounded corners, the 
horizontal and vertical diameters of the corner arcs are both 100px, and the processed image will be saved to file of
` .../out/rounded.png`. It is worth noting that the rounded image must be specified as a PNG format when saving, 
otherwise it will have no effect.

|         original image         |     after rounded the corners     |
|:------------------------------:|:---------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/rounded.png) |

### Enlarge image size lossless
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/chaotic_points.png"))
        .addFilter(new HighQualityExpandHandler.Builder()
            .finalWidth(300)
            .keepAspectRatio(true)
            .build())
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/expanded.png"));
```

Although *Thumbnailator* already offers the ability to resize images, images usually become blurry which have been 
enlarged by it because of the loss of image quality. When we need to expand the image lossless, we can use 
`HighQualityExpandHandler` to achieve it.

In this example, the size of the original image `.../in/chaotic_points.png` is 100px * 60px, after zooming in, we will 
get a 300px * 180px image which has been saved to file of `... /out/expanded.png`. Usually, we only need to specify a 
final width (or final height), and set it to keep the aspect ratio, so that we can get an image that is proportional to 
the original image.

|             original image              |           enlarged image           |
|:---------------------------------------:|:----------------------------------:|
| ![](/example/res/in/chaotic_points.png) | ![](/example/res/out/expanded.png) |
|            size:100px * 60px            |         size:300px * 180px         |

### Grayscaled image
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new ModeAdaptor(
            new WeightGrayingStrategy.Builder()
                .redWeight(0.3f)
                .greenWeight(0.59f)
                .build()))
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/grayed.jpg"));
```

In this example, the original image `.../in/panda.jpg` will be grayscaled, and the strategy adopted is to grayscale by 
weight. The red component accounts for 30%, and the green component accounts for 59%. The blue component accounts for 
11%, and the grayscaled image will be saved to the file of `.../out/grayed.jpg`. Assuming that the original RGB value 
of any pixel is \[r', g', b'\], the formula for calculating the gray value is `val = (r' * 0.3 + g' * 0.59 + b' * 0.11) `, 
and the final RGB value of the pixel is \[val, val, val\].

In addition to the graying strategy by weight, *imglib* also provides the average graying strategy `AvgGrayingStrategy`, 
the maximum graying strategy `MaxGrayingStrategy`, the minimum graying strategy `MinGrayingStrategy` and the fixed 
component graying strategy `FixedGrayingStrategy`, and developers can extend `AbstractGrayingStrategy` to achieve custom
grayscale implementation.

|         original image         |         grayscaled image         |
|:------------------------------:|:--------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/grayed.jpg) |

### Binarized image
```java
AbstractGrayingStrategy strategy = new FixedGrayingStrategy(FixedGrayingStrategy.FixedOption.R);
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new ModeAdaptor(
            new SimpleBinaryStrategy.Builder()
                .grayingStrategy(strategy)
                .threshold(120)
                .build()))
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/binary.jpg"));
```

In this example, the original image `.../in/panda.jpg` will be binarized, the grayscale strategy adopted is the fixed 
component (R) grayscale strategy, and the binarization strategy is the simple strategy which requires setting a threshold
`threshold`, when the gray value is greater than the threshold, each component of the pixel will be set to 255, otherwise
it will be set to 0.

In addition to the simple binarization strategy, developers can extend `AbstractBinaryStrategy` to achieve the custom 
binarization implementation.

|         original image         |         binarized image          |
|:------------------------------:|:--------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/binary.jpg) |

### Opening shape
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new ShapeAdaptor(
            new Line.Builder()
                .start(new Point(200, 260))
                .end(new Point(1000, 260))
                .color(ColorUtils.random())
                .stroke(new BasicStroke(6))
                .build()))
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/painted_line.jpg"));
```

In this example, a straight line will be drawn on the original image `/in/panda.jpg`, the starting point and ending 
point of the line are \[200, 260\], \[1000, 260\] respectively, and the color of the line is random. The thickness of
the brush is set to 6px, and the processed image will be saved to file of `.../out/painted_line.jpg`.

For the extension of the opening shape, developers can implement it by extending `AbstractOpenedShape`.

|         original image         |    image after adding opening shape    |
|:------------------------------:|:--------------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/painted_line.jpg) |

### Closed shape
```java
Thumbnails.of(ExampleUtils.tmpFileNameOf("in/panda.jpg"))
        .addFilter(new ShapeAdaptor(
            new Oval.Builder()
                .fill(true)
                .color(ColorUtils.random())
                .rect(new Rectangle(220, 110, 680, 350))
                .build()))
        .scale(1.0)
        .toFile(ExampleUtils.tmpFileNameOf("out/fill_oval.jpg"));
```

In this example, an oval will be drawn on the original image `/in/panda.jpg`, the coordinates of the upper left corner 
of the circumscribed rectangle of the ellipse are \[220, 110\], and the length and width of the circumscribed rectangle
are 680px and 350px, and set to fill the oval with a random color, the processed image will be saved to the file of 
`.../out/fill_oval.jpg`.

For a closed shape, there are two modes of drawing, one is to fill the interior, and the other is to only draw the 
border. If the developer wishes to draw the shape's border, a brush `Stroke` object should also be specified. In 
addition to the ellipse, *imglib* also provides the implementation of the rectangle `Rect`, and developers can achieve
other closed shapes by extending `AbstractClosedShape`.

|         original image         |   image after adding closed shape   |
|:------------------------------:|:-----------------------------------:|
| ![](/example/res/in/panda.jpg) | ![](/example/res/out/fill_oval.jpg) |
