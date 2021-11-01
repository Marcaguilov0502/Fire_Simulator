import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

public class Visualizer extends Canvas {

    private TImage image;
    private int width, height;

    public Visualizer(TImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void update() {
        image.update();
        Graphics g = this.getGraphics();
        g.drawImage(image.buffer(),0,0,this);
    }

}
