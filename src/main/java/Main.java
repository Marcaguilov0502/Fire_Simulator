import javax.imageio.ImageIO;
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

        Fire fire = new Fire(w,h,true, false);
        fire.setPalette(new Palette(PreparedPalettes.FIRE_OPAQUE));
        if (fire.getCoolingMap() != null) {
            window.generateVisualizer(fire.getCoolingMap());
        }
        window.generateVisualizer(fire);

        //FluidFire ff = new FluidFire(w,h);
        //window.generateVisualizer(ff);


        window.launch();

        while (true) {
            window.update();
            //sleep(10);
        }
    }

}
