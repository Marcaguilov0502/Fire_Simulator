import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Window extends JFrame {

    private ArrayList<Visualizer> visualizers = new ArrayList<>();
    private int width = 0, height = 0;
    GridLayout layout = new GridLayout();

    public Window(String name) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void generateVisualizer(TImage img) {
        this.visualizers.add(new Visualizer(img));
        this.width += img.width;
        if (this.height < img.height) {
            this.height = img.height;
        }
    }

    public void launch() {
        setSize(width+15,height+39);
        setLayout(layout);
        for (Visualizer v : visualizers) {
            getContentPane().add(v);
        }
        setVisible(true);

    }

    public void update() {
        for (Visualizer v : visualizers) {
            v.update();
        }
    }

}
