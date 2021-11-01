import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CoolingMap extends TImage {


    //Constructors


    public CoolingMap(int width, int height) {
        super(width, height);
        generateNoisyCoolingMap(10,7);
    }

    public CoolingMap(int width, int height, String path) {
        super(width, height);
        importCoolingMap(path);
    }


    //Overridden methods


    @Override
    public void update() {
        moveUp(speed);
    }


    //Methods


    public void generateNoisyCoolingMap(float density, int blur) {
        randomPoints(density);
        for (int i = 0; i < blur; i++) {
            blur();
        }
    }

    public void importCoolingMap(String file) {
        try {
            String path = "src\\main\\resources\\CoolingMaps\\" + file;
            BufferedImage img = ImageIO.read(new File(path));
            img = FireUtils.resize(img,width,height);
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int color = img.getRGB(x, y);

                    int[] argb = palette.getARGB(color);

                    int a = argb[0];
                    int r = argb[1];
                    int g = argb[2];
                    int b = argb[3];

                    int temperature = ((r + g + b) / 3) * (a / 255);

                    pixels[x][y] = temperature;
                }
            }
            rotateClockWise(1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void randomPoints(float density) {
        int points = (int) ((width * height) * (density / 100));
        Random r = new Random();
        while (points > 0) {
            int rx = r.nextInt(height);
            int ry = r.nextInt(width);
            if (pixels[rx][ry] == 0) {
                pixels[rx][ry] = 255;
                points--;
            }
        }
    }
}
