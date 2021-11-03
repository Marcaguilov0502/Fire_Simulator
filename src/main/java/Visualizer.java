import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
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

    public void draw(Graphics g) {

        image.update();
        g.drawImage(image.buffer(),0,0,this);

    }

    public void update() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        draw(g);
        g.dispose();
        bs.show();
    }

}
