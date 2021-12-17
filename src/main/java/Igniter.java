import java.util.Random;

public class Igniter {


    // Attributes


    private int[] pixels;
    private float[][] ignitions;


    // Constructor


    public Igniter(int igniterCount, int fireWidth) {
        this.pixels = new int[fireWidth];
        this.ignitions = new float[igniterCount][2];
        centerIgnitions();
    }


    // Methods


    public void attractToCenter(float speed) {
        float center = (pixels.length/2f);
        for (int i = 0; i < ignitions.length; i++) {
            float relativeDistanceFromCenter = (((ignitions[i][0] - center)/center));
            ignitions[i][0] -= relativeDistanceFromCenter*((new Random().nextFloat())*(speed/90f));
            //ignitions[i][1] += (1f-relativeDistanceFromCenter)*1.5f;
            if (ignitions[i][1] < 0) {ignitions[i][1]=0;}
        }
    }

    public void centerIgnitions() {
        float positionIncrement = ((float)pixels.length)/((float) ignitions.length);
        float position = positionIncrement;
        for (int x = 0; x < ignitions.length; x++) {
            ignitions[x][0] = position;
            ignitions[x][1] = (new Random().nextFloat())*255;
            position += positionIncrement;
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public void move(float speed) {
        int relativeSpeed = (int) ((pixels.length/2)*(speed/1500));
        Random r = new Random();
        for (int i = 0; i < ignitions.length; i++) {
            int min = -relativeSpeed;
            int max = relativeSpeed;
            ignitions[i][0] += r.nextInt((max-min)+1)+min;
            if (ignitions[i][0] < 0) {
                ignitions[i][0] = 0;}
            if (ignitions[i][0] >= pixels.length) {
                ignitions[i][0] = pixels.length-1;}
        }
    }

    public void spread(float maxSize) {
        pixels = new int[pixels.length];
        Random r = new Random();

        for (int i = 0; i < ignitions.length; i++) {

            float size = maxSize*(ignitions[i][1]/255f);
            int ignitionLength = (int) ((pixels.length/2f)*(size/100f));
            float multiplierDecrement = 1f/ignitionLength;

            ignitions[i][1] += r.nextInt(41)-22;
            if (ignitions[i][1] < 0) {
                ignitions[i][1] = 0;}
            if (ignitions[i][1] > 255) {
                ignitions[i][1] = 255;}

            float multiplier = 1 - multiplierDecrement;
            pixels[(int) ignitions[i][0]] = (int) ignitions[i][1];

            for (int j = 0; j < ignitionLength; j++) {
                if (ignitions[i][0] - j > 0) {
                    pixels[(int) (ignitions[i][0] - j)] += (int) (ignitions[i][1]*multiplier*(r.nextFloat()+0.5f));
                    if (pixels[(int) (ignitions[i][0] - j)] > 255) {pixels[(int) (ignitions[i][0] - j)] = 255-r.nextInt(200);}
                    if (pixels[(int) (ignitions[i][0] - j)] < 0) {pixels[(int) (ignitions[i][0] - j)] = 0;}
                }
                if (ignitions[i][0] + j < pixels.length) {
                    pixels[(int) (ignitions[i][0] + j)] += (int) (ignitions[i][1]*multiplier*(r.nextFloat()+0.5f));
                    if (pixels[(int) (ignitions[i][0] + j)] > 255) {pixels[(int) (ignitions[i][0] + j)] = 255-r.nextInt(200);}
                    if (pixels[(int) (ignitions[i][0] + j)] < 0) {pixels[(int) (ignitions[i][0] + j)] = 0;}
                }
                multiplier -= multiplierDecrement;
            }

        }
    }

    public void update(float speed, float maxSize) {
        move(speed);
        attractToCenter(speed);
        spread(maxSize);
    }

}
