package cn.pipe.ypipe.merge;

import cn.core.GenericBuilder;
import cn.core.tool.Range;
import cn.core.utils.ObjectUtils;
import cn.pipe.ypipe.AbstractMergeFilter;
import cn.core.ex.InvalidSettingException;
import cn.core.utils.BufferedImageUtils;
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
    private final Color fillColor;

    public GridMergeHandler(Builder bu) {
        this.autoAdapts = bu.autoAdapts;
        this.gridWidth = bu.gridWidth;
        this.gridHeight = bu.gridHeight;
        this.horizontalNum = bu.horizontalNum;
        this.alpha = bu.alpha;
        this.alignCenter = bu.alignCenter;
        this.fillColor = bu.fillColor;
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
        private boolean autoAdapts;
        private int gridWidth;
        private int gridHeight;
        private int horizontalNum;
        private boolean alignCenter = false;
        private float alpha = 1.0f;
        private Color fillColor;

        public Builder autoAdapts() {
            this.autoAdapts = true;
            return this;
        }

        public Builder gridWidth(int gridWidth) {
            this.gridWidth = gridWidth;
            return this;
        }

        public Builder gridHeight(int gridHeight) {
            this.gridHeight = gridHeight;
            return this;
        }

        public Builder horizontalNum(int horizontalNum) {
            this.horizontalNum = horizontalNum;
            return this;
        }

        public Builder alignCenter() {
            this.alignCenter = true;
            return this;
        }

        public Builder alpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder fillColor(Color fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        @Override
        public GridMergeHandler build() {
            // invalid setting
            if (gridWidth < 0) {
                throw new InvalidSettingException("The width of the grid must be greater than 0.");
            }
            if (gridHeight < 0) {
                throw new InvalidSettingException("The height of the grid must be greater than 0.");
            }
            if (horizontalNum < 0) {
                throw new InvalidSettingException("The number placed in horizontal must be greater than 0.");
            }
            if (Range.ofFloat(0f, 1f).notWithin(alpha)) {
                throw new InvalidSettingException("The alpha out of bounds:[0, 1].");
            }

            // The size of the grid must be determined when 'autoAdapts' is false.
            if (!autoAdapts) {
                if (gridWidth == 0) {
                    throw new InvalidSettingException("The width of the grid must be determined when autoAdapts is not enabled.");
                }
                if (gridHeight == 0) {
                    throw new InvalidSettingException("The height of the grid must be determined when autoAdapts is not enabled.");
                }
            }

            // Set a default fill color when not completely transparent.
            if (alpha != 0.0 && ObjectUtils.isNull(fillColor)) {
                fillColor = Color.WHITE;
            }

            return new GridMergeHandler(this);
        }
    }


}
