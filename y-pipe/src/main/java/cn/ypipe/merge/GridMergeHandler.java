package cn.ypipe.merge;

import cn.core.GenericBuilder;
import cn.ypipe.AbstractMergeFilter;
import cn.core.exec.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 网格化合并策略
 *
 * @author tracy
 * @since 1.0.0
 */
public class GridMergeHandler extends AbstractMergeFilter {

    /**
     * 是否自适应
     */
    private final boolean autoAdapts;

    /**
     * 网格的宽
     */
    private int gridWidth;

    /**
     * 网格的高
     */
    private int gridHeight;

    /**
     * 横向摆放的图片数量
     */
    private int horizontalNum;

    /**
     * 图像是否居中对齐
     */
    private final boolean alignCenter;

    /**
     * 背景透明度
     */
    private final float alpha;

    /**
     *  背景填充颜色
     */
    private Color fillColor;

    public GridMergeHandler(Builder bu) {
        this.autoAdapts = bu.autoAdapts;
        this.gridWidth = bu.gridWidth;
        this.gridHeight = bu.gridHeight;
        this.horizontalNum = bu.horizontalNum;
        this.alpha = bu.alpha;
        this.alignCenter = bu.alignCenter;
        if (bu.alpha == 1.0) {
            this.fillColor = null;
        } else {
            this.fillColor = bu.fillColor;
            if (bu.fillColor == null) {
                this.fillColor = ColorUtils.random();
            }
        }
    }

    @Override
    protected BufferedImage merge(List<BufferedImage> images) {
        // adjust the parameters
        adjustHorizontalNumIfNecessary(images.size());
        if (autoAdapts) {
            adjustGridSize(images);
        }
        // define the final image
        int rows = getRows(images.size());
        int w = horizontalNum * gridWidth;
        int h = rows * gridHeight;
        BufferedImage tar = BufferedImageUtils.newBackgroundImage(alpha, w, h, fillColor);
        Graphics2D g2d = tar.createGraphics();

        int alreadyCopyIndex = 0;
        while (alreadyCopyIndex < images.size()) {
            // point of start
            int startX = (alreadyCopyIndex % horizontalNum) * gridWidth;
            int startY = (alreadyCopyIndex / horizontalNum) * gridHeight;
            // align center
            int x = startX;
            int y = startY;
            if (alignCenter) {
                // horizontally
                int cw = images.get(alreadyCopyIndex).getWidth();
                if (cw < gridWidth) {
                    x += ((gridWidth - cw) >> 1);
                }
                // vertically
                int ch = images.get(alreadyCopyIndex).getHeight();
                if (ch < gridHeight) {
                    y += ((gridHeight - ch) >> 1);
                }
            }
            // paint current grid
            g2d.drawImage(images.get(alreadyCopyIndex++), x, y, null);
        }

        g2d.dispose();
        return tar;
    }

    /**
     * 调整网格大小
     * @param images 多个图像
     */
    protected void adjustGridSize(List<BufferedImage> images) {
        for (BufferedImage o : images) {
            if (o.getWidth() > gridWidth) {
                gridWidth = o.getWidth();
            }
            if (o.getHeight() > gridHeight) {
                gridHeight = o.getHeight();
            }
        }
    }

    /**
     * 调整水平方向格子的数量，如果有必要时
     * @param imageNum 图片数量
     */
    protected void adjustHorizontalNumIfNecessary(int imageNum) {
        if (horizontalNum <= 0 || imageNum < horizontalNum) {
            horizontalNum = imageNum;
        }
    }

    /**
     * 计算所有图片占用的行数
     * @param imageNum 图片数量
     * @return 所需行数
     */
    protected int getRows(int imageNum) {
        return (imageNum + horizontalNum - 1) / horizontalNum;
    }

    public static class Builder implements GenericBuilder<GridMergeHandler> {
        private boolean autoAdapts = true;
        private int gridWidth;
        private int gridHeight;
        private int horizontalNum = -1;
        private boolean alignCenter = false;
        private float alpha;
        private Color fillColor;

        public Builder autoAdapts(boolean autoAdapts) {
            this.autoAdapts = autoAdapts;
            return this;
        }

        public Builder gridWidth(int gridWidth) {
            this.gridWidth = gridWidth;
            if (gridWidth <= 0) {
                throw new InvalidSettingException("the width of the grid must be greater than 0");
            }
            return this;
        }

        public Builder gridHeight(int gridHeight) {
            this.gridHeight = gridHeight;
            if (gridHeight <= 0) {
                throw new InvalidSettingException("the height of the grid must be greater than 0");
            }
            return this;
        }

        public Builder horizontalNum(int horizontalNum) {
            this.horizontalNum = horizontalNum;
            if (horizontalNum <= 0) {
                throw new InvalidSettingException("the number placed in horizontal must be greater than 0");
            }
            return this;
        }

        public Builder alignCenter(boolean alignCenter) {
            this.alignCenter = alignCenter;
            return this;
        }

        public Builder alpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder fillColor(Color fillColor) {
            this.fillColor = fillColor;
            if (fillColor == null) {
                throw new InvalidSettingException("empty fill color");
            }
            return this;
        }

        @Override
        public GridMergeHandler build() {
            boolean notSetGrid = gridWidth <= 0 || gridHeight <= 0;
            if (!autoAdapts && notSetGrid) {
                throw new InvalidSettingException("the width or height of the grid not set");
            }
            return new GridMergeHandler(this);
        }
    }


}
