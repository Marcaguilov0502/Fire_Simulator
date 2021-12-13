import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class PalettePreviewer extends Canvas implements Runnable {

    private Palette palette;
    private int step = 254;

    public PalettePreviewer(Palette palette) {
        this.palette = palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    private BufferedImage preview() {
        BufferedImage previewer = new BufferedImage(1,256,BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 256; i++) {
            previewer.setRGB(0,i,palette.getColor(i));
        }
        return previewer;
    }

    @Override
    public void run() {
        while (true) {

            Graphics g = getGraphics();
            if (g == null) {continue;}
            g.drawImage(preview(),2,2, (int) (getWidth()*(2f/3f)),getHeight()-4,this);
            g.setColor(palette.getColorObject(step));
            g.fillRect((int)(getWidth()*(2f/3f)),2,(int)(getWidth()*(1f/3f))-1,getHeight()-4);
            g.dispose();
            step = (step <= 0)?255:step-1;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
