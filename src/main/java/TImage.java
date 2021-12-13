import java.awt.image.BufferedImage;

public abstract class TImage extends BufferedImage {

    //Parameters

    protected int speed = 5;
    protected float ignitionDensity = 10f;
    protected float coolingPower = 4f;
    protected float igniterMaxSize = 30f;
    protected float igniterSpeed = 40f;
    protected int igniterCount = 120;


    //Attributes


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

    public TImage(int width, int height, int imgWidth, int imgHeight) {
        super(imgWidth, imgHeight, TYPE_INT_ARGB);
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
        for (int x = 0; x < speed; x++) {
            aux[x] = pixels[x];
        }
        for (int x = 0; x < height - speed; x++) {
            pixels[x] = pixels[x + speed];
        }
        for (int x = height - speed; x < height; x++) {
            pixels[x] = aux[x-(height-speed)];
        }
    }

    public void rotateClockWise(int times) {
        int[][] pixelsRotated = new int[width][height];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                pixelsRotated[y][x] = pixels[x][y];
            }
        }
        pixels = pixelsRotated;
    }

    public abstract void update();
}
