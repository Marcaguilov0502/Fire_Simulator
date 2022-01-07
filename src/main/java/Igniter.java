import java.util.Random;
import java.util.Stack;

public class Igniter {


    // Attributes


    private int[] pixels;
    private Stack<float[]> ignitions;


    // Constructor


    public Igniter(int igniterCount, int fireWidth) {
        this.pixels = new int[fireWidth];
        this.ignitions = new Stack<>();
        addSparks(igniterCount);
        centerIgnitions();
    }


    // Methods


    public synchronized void addSparks(int number) {
        for (int i = 0; i < number; i++) {
            ignitions.push(new float[]{pixels.length/2f,150f});
        }
    }

    public synchronized void attractToCenter(float speed) {
        float center = (pixels.length/2f);
        for (int i = 0; i < ignitions.size(); i++) {
            float relativeDistanceFromCenter = (((ignitions.get(i)[0] - center)/center));
            ignitions.get(i)[0] -= relativeDistanceFromCenter*((new Random().nextFloat())*(speed/90f));
            //ignitions[i][1] += (1f-relativeDistanceFromCenter)*1.5f;
            if (ignitions.get(i)[1] < 0) {ignitions.get(i)[1] = 0;}
        }
    }

    public synchronized void centerIgnitions() {
        float positionIncrement = ((float)pixels.length)/((float) ignitions.size());
        float position = positionIncrement;
        for (int x = 0; x < ignitions.size(); x++) {
            ignitions.get(x)[0] = position;
            ignitions.get(x)[1] = (new Random().nextFloat())*255;
            position += positionIncrement;
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public synchronized void move(float speed) {
        int relativeSpeed = (int) ((pixels.length/2)*(speed/1500));
        Random r = new Random();
        for (int i = 0; i < ignitions.size(); i++) {
            int min = -relativeSpeed;
            int max = relativeSpeed;
            ignitions.get(i)[0] += r.nextInt((max-min)+1)+min;
            if (ignitions.get(i)[0] < 0) {
                ignitions.get(i)[0] = 0;}
            if (ignitions.get(i)[0] >= pixels.length) {
                ignitions.get(i)[0] = pixels.length-1;}
        }
    }

    public synchronized void removeSparks(int number) {
        for (int i = 0;  i++<number && ignitions.size() > 0;) {
            ignitions.pop();
        }
    }

    public synchronized void spread(float maxSize) {
        pixels = new int[pixels.length];
        Random r = new Random();

        for (int i = 0; i < ignitions.size(); i++) {

            float size = maxSize*(ignitions.get(i)[1]/255f);
            int ignitionLength = (int) ((pixels.length/2f)*(size/100f));
            float multiplierDecrement = 1f/ignitionLength;

            ignitions.get(i)[1] += r.nextInt(41)-22;
            if (ignitions.get(i)[1] < 0) {
                ignitions.get(i)[1] = 0;}
            if (ignitions.get(i)[1] > 255) {
                ignitions.get(i)[1] = 255;}

            float multiplier = 1 - multiplierDecrement;
            pixels[(int) ignitions.get(i)[0]] = (int) ignitions.get(i)[1];

            for (int j = 0; j < ignitionLength; j++) {
                if (ignitions.get(i)[0] - j > 0) {
                    pixels[(int) (ignitions.get(i)[0] - j)] += (int) (ignitions.get(i)[1]*multiplier*(r.nextFloat()+0.5f));
                    if (pixels[(int) (ignitions.get(i)[0] - j)] > 255) {pixels[(int) (ignitions.get(i)[0] - j)] = 255-r.nextInt(200);}
                    if (pixels[(int) (ignitions.get(i)[0] - j)] < 0) {pixels[(int) (ignitions.get(i)[0] - j)] = 0;}
                }
                if (ignitions.get(i)[0] + j < pixels.length) {
                    pixels[(int) (ignitions.get(i)[0] + j)] += (int) (ignitions.get(i)[1]*multiplier*(r.nextFloat()+0.5f));
                    if (pixels[(int) (ignitions.get(i)[0] + j)] > 255) {pixels[(int) (ignitions.get(i)[0] + j)] = 255-r.nextInt(200);}
                    if (pixels[(int) (ignitions.get(i)[0] + j)] < 0) {pixels[(int) (ignitions.get(i)[0] + j)] = 0;}
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
