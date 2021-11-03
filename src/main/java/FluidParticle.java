public class FluidParticle {


    //Attributes


    public float x;
    public float y;


    //Constructor


    public FluidParticle() {
        this.x = 0;
        this.y = 0;
    }


    //Getters & Setters


    public float getDirection() {
        return (float) Math.atan2(y,x);
    }

    public void setDirection(float direction) {
        float d = direction;
        while (d >= 360f) { d -= 360f;}
        while (d < 0f) { d += 360f;}

        float speed = getSpeed();
        x = speed * ((float) Math.cos(direction));
        y = speed * ((float) Math.sin(direction));
    }

    public float getSpeed() {
        return (float) Math.sqrt((y*y)+(x*x));
    }

    public void setSpeed(float speed) {
        float direction = getDirection();
        if (speed > 0f) {
            if (direction < 2f) {
                System.out.println("a");
            }
        }
        if (speed < 0f) {
            setDirection(getDirection() + 180f);
        }
        x = speed * ((float) Math.cos(direction));
        y = speed * ((float) Math.sin(direction));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    //ToString


    @Override
    public String toString() {
        return "Particle: " +
                "speed=" + x +
                ", direction=" + y;
    }


    //Methods


}
