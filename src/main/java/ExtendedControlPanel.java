import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;

public class ExtendedControlPanel extends JPanel {


    //Attributes


    private final Configuration config;


    //Components


    private final LabeledJSlider ignitionDensity = new LabeledJSlider("Ignition Density", new JSlider(1,100,10));
    private final LabeledJSlider coolingPower = new LabeledJSlider("Cooling Power", new JSlider(1,100,50));
    private final LabeledJSlider speed = new LabeledJSlider("Speed", new JSlider(1,10,5));
    private final LabeledJSlider igniterMaxSize = new LabeledJSlider("Igniter Size", new JSlider(1,100,30));
    private final LabeledJSlider igniterSpeed = new LabeledJSlider("Igniter Speed", new JSlider(1,100,40));
    private final LabeledJSpinner igniterCount = new LabeledJSpinner("Igniter Count", new JSpinner());
    private final JButton reset = new JButton("Reset");

    //Constructor


    public ExtendedControlPanel(Configuration config) {
        setLayout(new GridBagLayout());
        setBackground(Color.red);
        setBackground(new Color(50,50,50));
        this.config = config;
        addComponents();
        addListeners();
    }


    //Methods

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(speed,gbc);

        gbc.gridy = 1;
        add(ignitionDensity, gbc);

        gbc.gridy = 0;
        gbc.gridx = 1;
        add(coolingPower, gbc);

        gbc.gridy = 1;
        add(igniterMaxSize, gbc);

        gbc.gridy = 0;
        gbc.gridx = 2;
        add(igniterSpeed, gbc);

        gbc.gridy = 1;
        igniterCount.getSpinner().setValue(120);
        add(igniterCount,gbc);

        gbc.gridy = 0;
        gbc.gridx = 3;
        gbc.gridheight = 2;
        reset.setBackground(Color.white);
        add(reset,gbc);

    }

    private void addListeners() {

        speed.getSlider().addChangeListener(e -> {
            int value = speed.getSlider().getValue();
            config.setSpeed(value);
        });

        ignitionDensity.getSlider().addChangeListener(e -> {
            int value = ignitionDensity.getSlider().getValue();
            config.setIgnitionDensity(value);
        });

        coolingPower.getSlider().addChangeListener(e -> {
            int value = coolingPower.getSlider().getValue();
            config.setCoolingPower(value/5f);
        });

        igniterMaxSize.getSlider().addChangeListener(e -> {
            int value = igniterMaxSize.getSlider().getValue();
            config.setIgniterMaxSize(value);
        });

        igniterSpeed.getSlider().addChangeListener(e -> {
            int value = igniterSpeed.getSlider().getValue();
            config.setIgniterSpeed(value);
        });

        igniterCount.getSpinner().addChangeListener(e ->  {
            int value = (int) igniterCount.getSpinner().getValue();
            if (value < 0) {
                value = 0;
                igniterCount.getSpinner().setValue(0);
            }
            config.setIgniterCount(value);
        });

        reset.addActionListener(e -> {
            config.setIgniterCount(120);
            igniterCount.getSpinner().setValue(120);
            config.setSpeed(5);
            speed.getSlider().setValue(5);
            config.setIgnitionDensity(10f);
            ignitionDensity.getSlider().setValue(10);
            config.setCoolingPower(10f);
            coolingPower.getSlider().setValue(50);
            config.setIgniterMaxSize(30f);
            igniterMaxSize.getSlider().setValue(30);
            config.setIgniterSpeed(40);
            igniterSpeed.getSlider().setValue(40);
        });

    }

    public LabeledJSlider getIgnitionDensity() {
        return ignitionDensity;
    }

    public LabeledJSlider getCoolingPower() {
        return coolingPower;
    }

    public LabeledJSlider getIgniterMaxSize() {
        return igniterMaxSize;
    }

    public LabeledJSlider getIgniterSpeed() {
        return igniterSpeed;
    }

    public LabeledJSpinner getIgniterCount() {
        return igniterCount;
    }

    //Inner Class


    public static class LabeledJSlider extends JPanel {


        //Components


        JLabel label;
        JSlider slider;


        //Constructor


        public LabeledJSlider(String name, JSlider slider) {
            this.label = new JLabel(name);
            this.slider = slider;
            Color c = new Color(50,50,50);
            setBackground(c);
            slider.setBackground(c);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            label.setForeground(Color.white);
            add(label,gbc);

            gbc.fill = BOTH;
            gbc.weighty = 1;
            gbc.gridy = 1;
            slider.setForeground(Color.white);
            add(slider,gbc);
        }


        //Getters


        public JLabel getLabel() {
            return label;
        }

        public JSlider getSlider() {
            return slider;
        }
    }

    public static class LabeledJSpinner extends JPanel {


        //Components


        JLabel label;
        JSpinner spinner;


        //Constructor


        public LabeledJSpinner(String name, JSpinner spinner) {
            this.label = new JLabel(name);
            this.spinner = spinner;
            Color c = new Color(50,50,50);
            setBackground(c);
            spinner.setBackground(c);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.CENTER;

            label.setForeground(Color.white);

            add(label,gbc);

            gbc.weighty = 0;
            gbc.gridy = 1;
            gbc.fill = BOTH;
            gbc.insets = new Insets(4,100,6,100);
            spinner.setForeground(Color.white);
            add(spinner,gbc);
        }


        //Getters


        public JLabel getLabel() {
            return label;
        }

        public JSpinner getSpinner() {
            return spinner;
        }
    }

}
