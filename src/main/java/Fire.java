import java.util.Random;

public class Fire extends TImage {


    //Attributes


    private CoolingMap coolingMap;
    private Igniter igniter;


    //Constructor


    public Fire(int width, int height, boolean withMemoryIgniter, boolean withCoolingMap) {
        super(width, height);
        if (withMemoryIgniter) { igniter = new Igniter(igniterCount,width); }
        if (withCoolingMap) { coolingMap = new CoolingMap(width, height); }
    }

    public Fire(int width, int height, boolean withMemoryIgniter, String coolingMapFileName) {
        super(width, height);
        if (withMemoryIgniter) { igniter = new Igniter(igniterCount,width); }
        coolingMap = new CoolingMap(width, height, coolingMapFileName);
    }


    //Overridden methods


    @Override
    public void update() {
        ignite(speed,ignitionDensity);
        convection(speed);
        blur();
        cool(coolingPower);
    }


    //Methods


    public TImage getCoolingMap() {
        return coolingMap;
    }

    public void convection(int speed) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {

                if (x < height - speed) {
                    pixels[x][y] = pixels[x+speed][y];
                }

            }
        }
    }

    public void cool(float potency) {

        if (coolingMap == null) {
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    pixels[x][y] *= (1f-((potency*3*(new Random().nextFloat()))/200f));
                }
            }
        } else {
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {

                    pixels[x][y] -= coolingMap.pixels[x][y] * (potency / 100);
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }
        }
    }

    public void ignite(int lines, float density) {
        if (igniter == null) {
            igniteRandom(speed, density);
        } else {
            igniteMemory();
        }
    }

    public void igniteRandom(int lines, float density) {
        for (int x = height-lines; x < height; x++) {

            int ignitions = (int) (width*(density/100f));

            for (Random r = new Random();ignitions > 0; ignitions--) {
                int ry = r.nextInt(width);
                pixels[x][ry] = 255;
            }
        }
    }

    public void igniteMemory() {
        igniter.update(igniterSpeed, igniterMaxSize);
        for (int x = height-speed; x < height; x++) {
            pixels[x] = igniter.getPixels();
        }
    }
}
