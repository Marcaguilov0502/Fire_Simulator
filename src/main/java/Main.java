import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class Main {
    /* ToDo
        BufferStrategy
        FluidFire
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        int w = 500, h = 500;
        Window window = new Window("FIRE SIMULATOR");
        Fire fire = new Fire(w,h);

        runFire(window,fire);

    }

    public static void runFire(Window window, Fire fire) throws InterruptedException, IOException {
        fire.setPalette(new Palette(PreparedPalettes.FIRE_OPAQUE));
        if (fire.getCoolingMap() != null) {
            window.generateVisualizer((CoolingMap) fire.getCoolingMap());
        }
        window.generateVisualizer(fire);

        window.launch();

        while (true) {
            window.update();
            sleep(10);
        }
    }

}
