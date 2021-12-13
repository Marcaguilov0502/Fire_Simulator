import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.RELATIVE;

public class Window extends JFrame {

    private Visualizer fireVisualizer;
    private Visualizer coolingMapVisualizer;
    private ControlPanel controlPanel;
    private int width = 0, height = 0;


    public Window(String name) {
        super(name);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0,0,0));
    }

    public void generateVisualizer(CoolingMap img) {
        coolingMapVisualizer = new Visualizer(img);
        this.width += img.width;
        if (this.height < img.height) {
            this.height = img.height;
        }
    }

    public void generateVisualizer(Fire img) {
        fireVisualizer = new Visualizer(img);
        this.width += img.width;
        if (this.height < img.height) {
            this.height = img.height;
        }
        controlPanel = new ControlPanel((Fire) img);
    }

    public void launch() {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = RELATIVE;
        gbc.gridy = 0;
        gbc.fill = BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        getContentPane().add(coolingMapVisualizer,gbc);
        getContentPane().add(fireVisualizer,gbc);
        gbc.weightx = 0;
        gbc.weighty = 0;
        getContentPane().add(controlPanel, gbc);
        setVisible(true);
        setSize(width+200+14, height+39);

    }

    public void update() {
        fireVisualizer.update();
        coolingMapVisualizer.update();
    }

}
