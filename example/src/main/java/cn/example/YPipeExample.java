package cn.example;

import cn.core.utils.ColorUtils;
import cn.example.utils.ExampleUtils;
import cn.usage.YPipes;
import cn.ypipe.merge.GridMergeHandler;
import cn.ypipe.split.GridSplitHandler;
import java.io.IOException;

/**
 * YPipeExample
 *
 * @author tracy
 * @since 1.0.0
 */
public class YPipeExample {
    private YPipeExample(){}

    public static void splitImg() throws IOException {
        YPipes.of(ExampleUtils.tmpFileNameOf("in/before_split.jpg"))
                .addLast(new GridSplitHandler.Builder()
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
    }

    public static void mergeImg() throws IOException {
        YPipes.of(ExampleUtils.tmpFileNameOf("in/to_merge/spring.jpg"),
                ExampleUtils.tmpFileNameOf("in/to_merge/summer.jpg"),
                ExampleUtils.tmpFileNameOf("in/to_merge/winter.jpg"),
                ExampleUtils.tmpFileNameOf("in/to_merge/autumn.jpg"))
                .addLast(new GridMergeHandler.Builder()
                        .horizontalNum(2)
                        .fillColor(ColorUtils.of(240, 240, 240))
                        .gridWidth(530)
                        .gridHeight(530)
                        .autoAdapts(true)
                        .alignCenter(true)
                        .build())
                .toFile(ExampleUtils.tmpFileNameOf("out/after_merge.jpg"));
    }
}
