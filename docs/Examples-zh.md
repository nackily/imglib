
---

[**English Documentation**](Examples.md)

---

在本文中将列举一些示例，以便开发者可以快速了解 imglib 的功能并且轻松使用他们，所有的这些示例都可以在[example/.../Setting.java](/example/src/main/java/cn/example/Setting.java)中找到，并且您可以运行 **example** 模块来执行相应的功能。

## 图像收集

### 创建一张透明图像
```java
ImagePipes.ofEmptySource()
        .register(new TransparentImageGenerator.Builder()
             .width(300)
             .height(200)
             .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/transparent.png"));
```

在本例中，将创建一张宽高为 300px \* 200px ，背景透明的图像，并将其保存到`.../out/transparent.png`文件中。

### 创建一张纯色图像
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

在本例中，将创建一张宽高为 300px \* 200px ，透明度为 0.2（透明度在 \[0, 1\] 之间取值，等于 0 时图像完全不透明，等于 1 时图像完全透明），背景颜色随机的图像，并将其保存到`.../out/mono_color.png`文件中。

### 截取一张屏幕快照
```java
ImagePipes.ofEmptySource()
        .register(new ScreenshotGenerator.Builder()
            .startPoint(600, 300)
            .width(280)
            .height(190)
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/screenshot.png"));
```

在本例中，将截取一张屏幕快照，截取的区域指定为左上角坐标为\[600, 300\], 宽度为 280px，高度为 190px，截取的快照将被保存到`.../out/screenshot.png`文件中。

### 创建一张 HASH 图像
```java
ImagePipes.ofEmptySource()
        .register(new HashImageGenerator.Builder("imglib-user")
            .gridVerticalNum(5)
            .fgColor(ColorUtils.of(50, 150, 50))
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/hash.png"));
```

在本例中，将创建宽高为 5px \* 5px 的二维矩阵，在矩阵中如果对应位置的像素点取值为 `TRUE`，则该像素点的前景色设置为\[(R) 50,(G) 150,(B) 50\]，该点阵图像将被保存到`.../out/hash.png`文件中。

### 提取图像自 PDF 页
```java
ImagePipes.ofPdf(ExampleUtils.tmpFileNameOf("in/jvms8.pdf"))
        .register(0)
        .dpi(280)
        .toFile(ExampleUtils.tmpFileNameOf("out/page_1_of_jvms8.jpg"));
```

在本例中，将解析指定的 PDF 文件`.../in/jvms8.pdf`，提取索引为 0 的页为图像，提取图像时指定 DPI（每英寸内像素点数）为 280，并将该图像保存到`.../out/page_1_of_jvms8.jpg`文件中。

### 提取图像自 GIF 帧
```java
ImagePipes.ofGif(ExampleUtils.tmpFileNameOf("in/duck.gif"))
        .registerAll()
        .toFiles(ExampleUtils.tmpFileNameOf("out/gif/frame_1.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_2.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_3.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_4.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_5.jpg"));
```

在本例中，将解析指定的 GIF 文件`.../in/duck.gif`，提取该文件的所有帧为图像列表，并将这些图像按照帧的顺序逐个保存到`.../out/gif/`文件目录中。

## 聚合与分裂

### 网格化拆分图像
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

在本例中，将使用宽高为 400px \* 250px 的矩形区域逐行自左往右从原始图像中提取图像，直至该矩形已触碰到（或者已包含）图像边界，并将提取的图像按照顺序保存到`.../out/split/`文件目录中。

### 网格化合并图像
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

在本例中，将从`.../in/to_merge/`文件目录中加载`spring.jpg` `summer.jpg` `winter.jpg` `autumn.jpg`四幅图像，并将这些图像按照顺序拼接在一幅图像内，拼接的规则如下：

+ 使用固定宽高为 530px \* 530px 的网格盛放每幅图像；
+ 开启网格自动调整，当网格不足以容纳某幅图像时，重置网格的大小到足以容纳；
+ 设置图像居中摆放，包括水平居中和垂直居中；
+ 每一行摆放的网格设置为 2 个；
+ 如某个图像不能铺满整个网格区域，则空白部分使用颜色\[(R) 240,(G) 240,(B) 240\]填充；

拼接的图像将被保存到`.../out/after_merge.jpg`文件中。

### 将图像保存为 GIF 文件
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

在本例中，将从`.../in/to_merge/`文件目录中加载`spring.jpg` `summer.jpg` `winter.jpg` `autumn.jpg`四幅图像，并将这些图像编码到`.../out/seasons.gif`文件中，该 GIF 格式的文件每隔 400ms 切换下一帧，并且已设定为重复播放模式。

## 图像处理

// todo