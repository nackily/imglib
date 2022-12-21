package cn.pipe.ypipe.merge;

import cn.core.GenericBuilder;
import cn.core.utils.ObjectUtils;
import cn.pipe.ypipe.AbstractMergeFilter;
import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
import cn.core.utils.ColorUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A merge handler which based on grid implementation.
 *
 * @author tracy
 * @since 0.2.1
 */
public class GridMergeHandler extends AbstractMergeFilter {

    /**
     * Whether auto adapts is enabled. When this parameter is enabled,
     * it is possible to dynamically adjust the width and height of
     * the grid.
     */
    private final boolean autoAdapts;

    /**
     * The width of the grid.
     */
    private int gridWidth;

    /**
     * The height of the grid.
     */
    private int gridHeight;

    /**
     * The horizontal grid number of the final image.
     */
    private int horizontalNum;

    /**
     * Whether the original image is centered relative to the grid.
     */
    private final boolean alignCenter;

    /**
     * The alpha of the background.
     */
    private final float alpha;

    /**
     *  The fill color of the background.
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
     * Adjust the width and height of the grid if necessary.
     *
     * @param images The images to be merged.
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
     * Adjust the horizontal grid number if necessary.
     *
     * @param imageNum The size of images to be merged.
     */
    protected void adjustHorizontalNumIfNecessary(int imageNum) {
        if (horizontalNum <= 0 || imageNum < horizontalNum) {
            horizontalNum = imageNum;
        }
    }

    /**
     * Calculate the final number of rows.
     *
     * @param imageNum The size of images to be merged.
     * @return The final number of rows.
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
                throw new InvalidSettingException("The width of the grid must be greater than 0.");
            }
            return this;
        }

        public Builder gridHeight(int gridHeight) {
            this.gridHeight = gridHeight;
            if (gridHeight <= 0) {
                throw new InvalidSettingException("The height of the grid must be greater than 0.");
            }
            return this;
        }

        public Builder horizontalNum(int horizontalNum) {
            this.horizontalNum = horizontalNum;
            if (horizontalNum <= 0) {
                throw new InvalidSettingException("The number placed in horizontal must be greater than 0.");
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
            ObjectUtils.excNull(fillColor, "Empty fill color.");
            return this;
        }

        @Override
        public GridMergeHandler build() {
            boolean notSetGrid = gridWidth <= 0 || gridHeight <= 0;
            if (!autoAdapts && notSetGrid) {
                throw new InvalidSettingException("The width or height of the grid not set.");
            }
            return new GridMergeHandler(this);
        }
    }


}
