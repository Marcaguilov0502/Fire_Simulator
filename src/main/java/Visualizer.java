import java.awt.*;
import java.awt.image.BufferStrategy;

import static java.lang.Thread.sleep;

public class Visualizer extends Canvas {

    private TImage image;

    public Visualizer(TImage image) {
        this.image = image;
    }

    public void draw(Graphics g) {

        image.update();
        g.drawImage(image,0,0,this);

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
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
