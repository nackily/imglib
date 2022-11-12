package cn.extension.ext.split;

import cn.extension.exec.HandlingException;
import cn.extension.exec.ParameterException;
import cn.extension.ext.SplitStrategy;
import cn.extension.tool.GenericBuilder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 网格化拆分策略
 *
 * @author tracy
 * @since 1.0.0
 */
public class GridSplitStrategy implements SplitStrategy {

    /**
     * 网格的宽
     */
    private int gridWidth;

    /**
     * 网格的高
     */
    private int gridHeight;

    public GridSplitStrategy (Builder bu) {
        this.gridWidth = bu.gridWidth;
        this.gridHeight = bu.gridHeight;
    }

    @Override
    public List<BufferedImage> execute(BufferedImage img) {
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();

        // adjust the parameters
        adjustGridIfNecessary(originalWidth, originalHeight);

        // cut the picture into small pieces
        List<BufferedImage> tars = new ArrayList<>();
        int rows = (originalHeight + gridHeight - 1) / gridHeight;
        int cols = (originalWidth + gridWidth - 1) / gridWidth;

        for (int r = 0; r < rows; r++) {

            int h = Math.min(gridHeight, (originalHeight - gridHeight * r));

            for (int c = 0; c < cols; c++) {
                // copy the corresponding rectangle
                int x = c * gridWidth;
                int y = r * gridHeight;
                int w = Math.min(gridWidth, (originalWidth - gridWidth * c));

                tars.add(img.getSubimage(x, y, w, h));
            }
        }

        return tars;
    }

    /**
     * 调整网格，如果有必要时
     * @param imgWidth 原始图像宽
     * @param imgHeight 原始图像高
     */
    public void adjustGridIfNecessary(int imgWidth, int imgHeight) {
        if (gridWidth > imgWidth && gridHeight > imgHeight) {
            throw new HandlingException("There is no need to split.");
        }
        if (gridWidth > imgWidth || gridWidth <= 0) {
            gridWidth = imgWidth;
        }
        if (gridHeight > imgHeight || gridHeight <= 0) {
            gridHeight = imgHeight;
        }
    }


    public static class Builder implements GenericBuilder<GridSplitStrategy> {
        private int gridWidth;
        private int gridHeight;

        public Builder gridWidth(int gridWidth) {
            this.gridWidth = gridWidth;
            if (gridWidth <= 0) {
                throw new ParameterException("the width of the grid must be greater than 0");
            }
            return this;
        }

        public Builder gridHeight(int gridHeight) {
            this.gridHeight = gridHeight;
            if (gridHeight <= 0) {
                throw new ParameterException("the height of the grid must be greater than 0");
            }
            return this;
        }

        @Override
        public GridSplitStrategy build() {
            if (gridWidth <= 0 && gridHeight <= 0) {
                throw new ParameterException("both of the width and height of the grid not set");
            }
            return new GridSplitStrategy(this);
        }
    }
}
