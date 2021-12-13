import java.io.IOException;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Fire extends TImage {


    public final int LINE = 0, RANDOM = 1, MEMORY = 2;


    //Attributes

    private int igniterType;
    private int coolingType;
    private int coolingPath;
    private boolean usesCoolingMap = true;
    private int wind = 0;
    private boolean windRight = true;
    private int windTarget = 0;
    private boolean windIncreasing = true;
    private CoolingMap coolingMap;
    private Igniter igniter;


    //Constructor


    public Fire(int width, int height) {
        super(width, height);
        igniterType = MEMORY;
        coolingType = coolingMap.RANDOM_GENERATED;
        coolingPath = 1;
        igniter = new Igniter(igniterCount, width);
        coolingMap = new CoolingMap(width, height);
    }


    //Overridden methods


    @Override
    public void update() {
        ignite(speed, ignitionDensity);
        convection(speed);
        blur();
        cool(coolingPower);
        wind();
        updateImage();
    }


    //Methods


    public void changeIgniterType() {
        igniterType = (igniterType == 2) ? 0 : igniterType + 1;
    }

    public void changeCoolingType() throws IOException {
        coolingType = (coolingType == 2) ? 0 : coolingType + 1;

        coolingMap.importCoolingMap("flat.png");
        try {
            sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coolingMap.importCoolingMap("flat.png");

        switch (coolingType) {
            case CoolingMap.FLAT:
                usesCoolingMap = false;
                coolingMap.importCoolingMap("flat.png");
                break;
            case CoolingMap.RANDOM_GENERATED:
                usesCoolingMap = true;
                coolingMap.generateNoisyCoolingMap(10, 7);
                break;
            case CoolingMap.IMPORTED:
                usesCoolingMap = true;
                setCoolingMap(coolingPath);
                break;
        }

    }

    public void changeCoolingMap() {
        coolingPath++;
        try {
            setCoolingMap(coolingPath);
        } catch (IOException ioException) {
            coolingPath = 0;
            changeCoolingMap();
        }
    }

    public void convection(int speed) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {

                if (x < height - speed) {
                    pixels[x][y] = pixels[x + speed][y];
                }

            }
        }
    }

    public void cool(float potency) {

        if (usesCoolingMap) {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {

                    pixels[x][y] -= coolingMap.pixels[x][y] * (potency / 70);
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }

        } else {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {

                    pixels[x][y] -= potency/2;
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }
        }
    }

    public TImage getCoolingMap() {
        return coolingMap;
    }

    public int getCoolingPath() {
        return coolingPath;
    }

    public int getCoolingType() {
        return coolingType;
    }

    public int getIgniterType() {
        return igniterType;
    }

    public Palette getPalette() {
        return palette;
    }

    public void ignite(int lines, float density) {
        switch (igniterType) {
            case LINE:
                igniteLine(density);
                break;
            case RANDOM:
                igniteRandom(lines, density);
                break;
            case MEMORY:
                igniteMemory();
                break;
        }
    }

    private void igniteLine(float density) {
        for (int x = height - speed; x < height; x++) {
            for (int y = 0; y < width; y++) {

                pixels[x][y] = (int) (ignitionDensity * 12.75f);

            }
        }
    }

    public void igniteRandom(int lines, float density) {
        for (int x = height - lines; x < height; x++) {

            int ignitions = (int) (width * (density / 100f));

            for (Random r = new Random(); ignitions > 0; ignitions--) {
                int ry = r.nextInt(width);
                pixels[x][ry] = (int) ((r.nextFloat() * (105f - density)) + density + 150f);
            }
        }
    }

    public void igniteMemory() {
        igniter.update(igniterSpeed, igniterMaxSize);
        for (int x = height - speed; x < height; x++) {
            pixels[x] = igniter.getPixels();
        }
    }

    public void setCoolingMap(int path) throws IOException {
        coolingMap.importCoolingMap("coolingMap" + path + ".png");
    }

    public void setOxygen(int oxygen) {
        igniterMaxSize = oxygen / 5f * 3f;
        igniterSpeed = oxygen / 5f + 10;
        ignitionDensity = oxygen / 5f;
    }

    public void wind() {
        Random r = new Random();
        int min = -1000, max = 1000;
        if (wind == windTarget) {
            if (r.nextFloat()>0.97f) {
                wind = 0;
                windTarget = r.nextInt(max-min+1)+min;
            }
        } else if (wind > windTarget) {
            if (r.nextFloat() > wind/(float)windTarget*(-1)
                    || r.nextFloat() < (wind-windTarget)/(float)windTarget*(-1)) { return; }
            wind--;
            windLeft();
            System.out.println(1);
        } else {
            if (r.nextFloat() > wind/(float)windTarget*(-1)
                    || r.nextFloat() < (wind-windTarget)/(float)windTarget*(-1)) { return; }
            wind++;
            windRight();
            System.out.println(2);
        }
    }

    public void windLeft() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int count = 1;
                int total = pixels[x][y];
                if (x < height-1) {total += pixels[x+1][y]; count++;}
                if (y < width-1 && x < height-1) {total += pixels[x+1][y+1]; count++;}
                if (y < width-1) {total += pixels[x][y+1]; count++;}
                if (y < width-2) {total += pixels[x][y+2]; count++;}

                pixels[x][y] = total/count;
            }
        }
    }

    public void windRight() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int count = 1;
                int total = pixels[x][y];
                if (x < height-1) {total += pixels[x+1][y]; count++;}
                if (y > 0 && x < height-1) {total += pixels[x+1][y-1]; count++;}
                if (y > 0) {total += pixels[x][y-1]; count++;}
                if (y > 1) {total += pixels[x][y-2]; count++;}

                pixels[x][y] = total/count;
            }
        }
    }

}
