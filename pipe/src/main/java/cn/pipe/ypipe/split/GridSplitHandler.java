package cn.pipe.ypipe.split;

import cn.core.ex.HandlingException;
import cn.core.ex.InvalidSettingException;
import cn.pipe.ypipe.AbstractSplitFilter;
import cn.core.GenericBuilder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A split handler which based on grid implementation.
 *
 * @author tracy
 * @since 0.2.1
 */
public class GridSplitHandler extends AbstractSplitFilter {

    /**
     * The width of the grid.
     */
    private int gridWidth;

    /**
     * The height of the grid.
     */
    private int gridHeight;

    public GridSplitHandler(Builder bu) {
        this.gridWidth = bu.gridWidth;
        this.gridHeight = bu.gridHeight;
    }

    @Override
    public List<BufferedImage> split(BufferedImage img) {
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
     * Adjust the width and height of the grid if necessary.
     *
     * @param imgWidth The width of the image to be split.
     * @param imgHeight The height of the image to be split.
     */
    protected void adjustGridIfNecessary(int imgWidth, int imgHeight) {
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


    public static class Builder implements GenericBuilder<GridSplitHandler> {
        private int gridWidth;
        private int gridHeight;

        public Builder gridWidth(int gridWidth) {
            this.gridWidth = gridWidth;
            return this;
        }

        public Builder gridHeight(int gridHeight) {
            this.gridHeight = gridHeight;
            return this;
        }

        @Override
        public GridSplitHandler build() {
            if (gridWidth < 0) {
                throw new InvalidSettingException("The width of the grid must be greater than 0.");
            }
            if (gridHeight < 0) {
                throw new InvalidSettingException("The height of the grid must be greater than 0.");
            }

            if (gridWidth <= 0 && gridHeight <= 0) {
                throw new InvalidSettingException("Both of the width and height of the grid not set.");
            }

            return new GridSplitHandler(this);
        }
    }
}
