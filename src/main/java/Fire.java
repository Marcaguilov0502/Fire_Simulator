import java.io.IOException;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Fire extends TImage {


    //Attributes


    private final CoolingMap coolingMap;
    private final Igniter igniter;


    //Constructor


    public Fire(int width, int height) {
        super(width, height);
        config = new Configuration();
        igniter = new Igniter(config.getIgniterCount(), width);
        coolingMap = new CoolingMap(width, height, config);
    }


    //Overridden methods


    @Override
    public void update() {
        ignite(config.getSpeed(), config.getIgnitionDensity());
        convection(config.getSpeed());
        blur();
        cool(config.getCoolingPower());
        updateImage();
    }


    //Methods


    public void changeCoolingType() throws IOException {

        int coolingType = config.getCoolingType();

        coolingType = (coolingType == 2) ? 0 : coolingType + 1;

        coolingMap.importCoolingMap("flat.png");
        try {
            sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coolingMap.importCoolingMap("flat.png");

        config.setCoolingType(coolingType);

        coolingMap.updateCoolingType();

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

        if (config.isUsingCoolingMap()) {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {

                    pixels[x][y] -= coolingMap.pixels[x][y] * (potency / 70f);
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    pixels[x][y] *= 0.94f;
                    pixels[x][y] -= 1;
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }
                }
            }

        } else {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    pixels[x][y] *= 0.97f;
                    pixels[x][y] -= 3;
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }
        }
    }

    public CoolingMap getCoolingMap() {
        return coolingMap;
    }

    public Palette getPalette() {
        return palette;
    }

    public void ignite(int speed, float density) {
        switch (config.getIgniterType()) {
            case Configuration.LINE:
                igniteLine(density,speed);
                break;
            case Configuration.RANDOM:
                igniteRandom(speed, density);
                break;
            case Configuration.MEMORY:
                igniteMemory(speed);
                break;
        }
    }

    private void igniteLine(float density, int speed) {
        for (int x = height - speed; x < height; x++) {
            for (int y = 0; y < width; y++) {

                pixels[x][y] = (int) (density * 12.75f);

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

    public void igniteMemory(int speed) {
        igniter.update(config.getIgniterSpeed(), config.getIgniterMaxSize());
        for (int x = height - speed; x < height; x++) {
            pixels[x] = igniter.getPixels();
        }
    }


}
