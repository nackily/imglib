
---
[**English Documentation**](Examples.md)
---

以下所有的示例都能在Application中找到。

## 图像收集

图像收集包括...

### 创建一张透明图像
```java
Captors.ofEmptySource()
        .addLast(new TransparentImageGenerator.Builder()
             .width(300)
             .height(200)
             .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/transparent.png"));
```

在本例中，将创建一张宽高为 300px \* 200px ，背景透明的图像，并将其保存到`.../out/transparent.png`文件中。

### 创建一张纯色图像
```java
Captors.ofEmptySource()
        .addLast(new MonoColorImageGenerator.Builder()
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
Captors.ofEmptySource()
        .addLast(new ScreenshotGenerator.Builder()
            .startPoint(600, 300)
            .width(280)
            .height(190)
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/screenshot.png"));
```

在本例中，将截取一张屏幕快照，截取的区域指定为左上角坐标为\[600, 300\], 宽度为 280px，高度为 190px，截取的快照将被保存到`.../out/screenshot.png`文件中。

### 创建一张 HASH 图像
```java
Captors.ofEmptySource()
        .addLast(new HashImageGenerator.Builder("imglib-user")
            .gridVerticalNum(5)
            .fgColor(ColorUtils.of(50, 150, 50))
            .build())
        .toFile(ExampleUtils.tmpFileNameOf("out/hash.png"));
```

在本例中，将创建宽高为 5px \* 5px 的二维矩阵，在矩阵中如果对应位置的像素点取值为 `TRUE`，则该像素点的前景色设置为\[(R) 50,(G) 150,(B) 50\]，该点阵图像将被保存到`.../out/hash.png`文件中。

### 提取图像自 PDF 页
```java
Captors.ofPdf(ExampleUtils.tmpFileNameOf("in/jvms8.pdf"))
        .page(0)
        .dpi(280)
        .toFile(ExampleUtils.tmpFileNameOf("out/page_1_of_jvms8.jpg"));
```

在本例中，将解析指定的 PDF 文件`.../in/jvms8.pdf`，提取索引为 0 的页为图像，提取图像时指定 DPI（每英寸内像素点数）为 280，并将该图像保存到`.../out/page_1_of_jvms8.jpg`文件中。

### 提取图像自 GIF 帧
```java
Captors.ofGif(ExampleUtils.tmpFileNameOf("in/duck.gif"))
        .allFrame()
        .toFiles(ExampleUtils.tmpFileNameOf("out/gif/frame_1.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_2.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_3.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_4.jpg"),
            ExampleUtils.tmpFileNameOf("out/gif/frame_5.jpg"));
```

在本例中，将解析指定的 GIF 文件`.../in/duck.gif`，提取该文件的所有帧为图像列表，并将这些图像按照帧的顺序逐个保存到`.../out/gif/`文件目录中。

## 图像处理

## 聚合与分裂
