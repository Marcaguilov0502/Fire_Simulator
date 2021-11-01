import java.util.Random;

public class Igniter {


    // Attributes


    private int[] pixels;
    private int[][] ignitionPositions;


    // Constructor


    public Igniter(int igniterCount, int fireWidth) {
        this.pixels = new int[fireWidth];
        this.ignitionPositions = new int[igniterCount][2];
        centerIgnitions();
    }


    // Methods


    public void centerIgnitions() {
        float positionIncrement = pixels.length/(ignitionPositions.length);
        float position = positionIncrement;
        for (int x = 0; x < ignitionPositions.length; x++) {
            ignitionPositions[x][0] = (int) position;
            ignitionPositions[x][1] = new Random().nextInt(256);
            position += positionIncrement;
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public void move(float speed) {
        int relativeSpeed = (int) ((pixels.length/2)*(speed/1500));
        Random r = new Random();
        for (int i = 0; i < ignitionPositions.length; i++) {
            int min = -relativeSpeed;
            int max = relativeSpeed;
            ignitionPositions[i][0] += r.nextInt((max-min)+1)+min;
            if (ignitionPositions[i][0] < 0) {ignitionPositions[i][0] = 0;}
            if (ignitionPositions[i][0] >= pixels.length) {ignitionPositions[i][0] = pixels.length-1;}
        }

    }

    public void spread(float maxSize) {
        pixels = new int[pixels.length];
        Random r = new Random();

        for (int i = 0; i < ignitionPositions.length; i++) {

            float size = maxSize*(ignitionPositions[i][1]/255f);
            int ignitionLength = (int) ((pixels.length/2f)*(size/100f));
            float multiplierDecrement = 1f/ignitionLength;

            ignitionPositions[i][1] += r.nextInt(41)-21;
            if (ignitionPositions[i][1] < 0) {ignitionPositions[i][1] = 0;}
            if (ignitionPositions[i][1] > 255) {ignitionPositions[i][1] = 255;}

            float multiplier = 1 - multiplierDecrement;
            pixels[ignitionPositions[i][0]] = ignitionPositions[i][1];

            for (int j = 0; j < ignitionLength; j++) {
                if (ignitionPositions[i][0] - j > 0) {
                    pixels[ignitionPositions[i][0] - j] += (int) (ignitionPositions[i][1]*multiplier*(r.nextFloat()+0.5f));
                    if (pixels[ignitionPositions[i][0] - j] > 255) {pixels[ignitionPositions[i][0] - j] = 255-r.nextInt(100);}
                    if (pixels[ignitionPositions[i][0] - j] < 0) {pixels[ignitionPositions[i][0] - j] = 0;}
                }
                if (ignitionPositions[i][0] + j < pixels.length) {
                    pixels[ignitionPositions[i][0] + j] += (int) (ignitionPositions[i][1]*multiplier*(r.nextFloat()+0.5f));
                    if (pixels[ignitionPositions[i][0] + j] > 255) {pixels[ignitionPositions[i][0] + j] = 255-r.nextInt(100);}
                    if (pixels[ignitionPositions[i][0] + j] < 0) {pixels[ignitionPositions[i][0] + j] = 0;}
                }
                multiplier -= multiplierDecrement;
            }

        }
    }

    public void update(float speed, float maxSize) {
        move(speed);
        spread(maxSize);
    }

}
