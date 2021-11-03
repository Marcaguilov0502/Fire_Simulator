import java.util.Random;

public class FluidFire extends TImage {


    //Attributes


    private float viscosity = 0.01f;

    private FluidParticle[][] particleField;
    private FluidParticle[][] nextParticleField;


    //Constructor


    public FluidFire(int width, int height) {
        super(width, height);
        generateParticleField(width,height);
    }


    //Overridden methods


    @Override
    public void update() {
        Random r = new Random();
        particleField[400][250].x = (r.nextFloat()*5f)-2f;
        particleField[400][250].y = (r.nextFloat()*5f)-2f;
        particleField[200][100].x = (r.nextFloat()*5f)-2f;
        particleField[200][100].y = (r.nextFloat()*5f)-2f;
        particleField[50][400].x = (r.nextFloat()*5f)-2f;
        particleField[50][400].y = (r.nextFloat()*5f)-2f;
        particleField[310][170].x = (r.nextFloat()*5f)-2f;
        particleField[310][170].y = (r.nextFloat()*5f)-2f;
        updateField();
        //blur();
        updatePixels();
    }


    //Methods


    public void generateParticleField(int width, int height) {
        particleField = new FluidParticle[height][width];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                particleField[x][y] = new FluidParticle();
            }
        }
    }

    public void generateNextParticleField(int width, int height) {
        nextParticleField = new FluidParticle[height][width];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                nextParticleField[x][y] = new FluidParticle();
            }
        }
    }

    public void updateField() {

        generateNextParticleField(width,height);

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                float dx = particleField[x][y].x;
                float dy = particleField[x][y].y;

                if (x+dx >= 0 && x+dx < height && y+dy >= 0 && y+dy < width ) {
                    nextParticleField[(int) (x+dx)][(int) (y+dy)].x += dx;
                    nextParticleField[(int) (x+dx)][(int) (y+dy)].y += dy;
                    float speed = nextParticleField[(int) (x+dx)][(int) (y+dy)].getSpeed();
                    nextParticleField[(int) (x+dx)][(int) (y+dy)].setSpeed(speed*(1f-viscosity));
                }

            }
        }
        particleField = nextParticleField;
    }

    public void updatePixels() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                pixels[x][y] = (int) (particleField[x][y].getSpeed()*150);
                if (pixels[x][y] > 255) {pixels[x][y] = 255;}
            }
        }
    }

}
