import java.awt.image.BufferedImage;

public abstract class TImage extends BufferedImage {

    //Attributes


    protected Configuration config;
    protected int[][] pixels;
    protected int width, height;
    protected Palette palette;


    //Constructor



    public TImage(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        this.pixels = new int[height][width];
        palette = new Palette(PreparedPalettes.GRAY_SCALE);
    }



    //Getters & Setters


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }


    //Methods


    public void blur() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int count = 1;
                int total = pixels[x][y];
                if (x > 0) {total += pixels[x-1][y]; count++;}
                if (x < height-1) {total += pixels[x+1][y]; count++;}
                if (y > 0) {total += pixels[x][y-1]; count++;}
                if (y < width-1) {total += pixels[x][y+1]; count++;}

                pixels[x][y] = total/count;
            }
        }
    }

    public void updateImage() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                setRGB(y,x,palette.getColor(pixels[x][y]));
            }
        }
    }

    public void moveUp(int speed) {
        int[][] aux = new int[speed][width];
        System.arraycopy(pixels, 0, aux, 0, speed);
        if (height - speed >= 0) System.arraycopy(pixels, speed, pixels, 0, height - speed);
        if (height - (height - speed) >= 0)
            System.arraycopy(aux, 0, pixels, height - speed, height - (height - speed));
    }

    public void rotateClockWise(int times) {
        for (int i = 0; i++< times;) {
            int[][] pixelsRotated = new int[width][height];
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    pixelsRotated[y][x] = pixels[x][y];
                }
            }
            pixels = pixelsRotated;
        }
    }

    public abstract void update();
}
