public class Configuration {

    private final Igniter igniter = new Igniter(120, 500);

    public static final int FLAT = 0, RANDOM_GENERATED = 1, IMPORTED = 2;
    public static final int LINE = 0, RANDOM = 1, MEMORY = 2;

    private int speed = 5;
    private float ignitionDensity = 10f;
    private float coolingPower = 10f;
    private float igniterMaxSize = 30f;
    private float igniterSpeed = 40f;
    private int igniterCount = 120;
    private int igniterType = MEMORY;
    private int coolingType = FLAT;
    private int coolingPath;
    private boolean usingCoolingMap = false;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getIgnitionDensity() {
        return ignitionDensity;
    }

    public void setIgnitionDensity(float ignitionDensity) {
        this.ignitionDensity = ignitionDensity;
    }

    public float getCoolingPower() {
        return coolingPower;
    }

    public void setCoolingPower(float coolingPower) {
        this.coolingPower = coolingPower;
    }

    public float getIgniterMaxSize() {
        return igniterMaxSize;
    }

    public void setIgniterMaxSize(float igniterMaxSize) {
        this.igniterMaxSize = igniterMaxSize;
    }

    public float getIgniterSpeed() {
        return igniterSpeed;
    }

    public void setIgniterSpeed(float igniterSpeed) {
        this.igniterSpeed = igniterSpeed;
    }

    public int getIgniterCount() {
        return igniterCount;
    }

    public void setIgniterCount(int igniterCount) {
        int dif = this.igniterCount - igniterCount;
        if (dif < 0) {
            igniter.addSparks(dif*(-1));
        } else if (dif > 0) {
            igniter.removeSparks(dif);
        }

        this.igniterCount = igniterCount;
    }

    public Igniter getIgniter() {
        return igniter;
    }

    public int getIgniterType() {
        return igniterType;
    }

    public void setIgniterType(int igniterType) {
        this.igniterType = igniterType;
    }

    public int getCoolingType() {
        return coolingType;
    }

    public void setCoolingType(int coolingType) {
        this.coolingType = coolingType;
    }

    public int getCoolingPath() {
        return coolingPath;
    }

    public void setCoolingPath(int coolingPath) {
        this.coolingPath = coolingPath;
    }

    public boolean isUsingCoolingMap() {
        return usingCoolingMap;
    }

    public void setUsingCoolingMap(boolean usingCoolingMap) {
        this.usingCoolingMap = usingCoolingMap;
    }

    //-----------------------------------------------------------------


    public void changeIgniterType() {
        igniterType = (igniterType == 2) ? 0 : igniterType + 1;
    }

    public void nextCoolingPath() {
        coolingPath++;
    }

    public void setOxygen(int oxygen) {
        igniterMaxSize = oxygen / 5f * 3f;
        igniterSpeed = oxygen / 5f + 10;
        ignitionDensity = oxygen / 5f;
    }


}
