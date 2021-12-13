import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class ControlPanel extends JPanel {

    private Color backgroundColor = new Color(20,20,20);
    private Fire fire;

    public ControlPanel(Fire fire) {
        super();
        this.fire = fire;
        this.setBackground(backgroundColor);
        this.setLayout(new GridBagLayout());
        addContents();
        adaptColors();
        addListeners();
    }

    JLabel sliderLabel = new JLabel("Oxygen");
    JSlider slider = new JSlider(JSlider.VERTICAL, 1,100,50);

    JButton igniterType = new JButton("Igniter: Memory");
    JButton coolingType = new JButton("Cooling: Random");
    JButton importedCoolingMap = new JButton("Cooling Map: 0");
    JButton palette = new JButton("Edit Palette");
    JButton export = new JButton("Export");

    private void adaptColors() {

        Color[] buttonColors = new Color[]{
                fire.getPalette().getColorObject(73),
                fire.getPalette().getColorObject(109),
                fire.getPalette().getColorObject(145),
                fire.getPalette().getColorObject(182),
                fire.getPalette().getColorObject(218)};

        Color[] buttonTextColors = new Color[buttonColors.length];

        for (int i = 0; i < buttonTextColors.length; i++) {
            Color c = buttonColors[i];
            if (c.getRed()+c.getBlue()+c.getGreen()>500) {
                buttonTextColors[i] = Color.black;
            } else {
                buttonTextColors[i] = Color.white;
            }
        }

        igniterType.setBackground(buttonColors[0]);
        igniterType.setForeground(buttonTextColors[0]);

        coolingType.setBackground(buttonColors[1]);
        coolingType.setForeground(buttonTextColors[1]);

        importedCoolingMap.setBackground(buttonColors[2]);
        importedCoolingMap.setForeground(buttonTextColors[2]);

        palette.setBackground(buttonColors[3]);
        palette.setForeground(buttonTextColors[3]);

        export.setBackground(buttonColors[4]);
        export.setForeground(buttonTextColors[4]);

    }

    private void addContents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;

        sliderLabel.setForeground(Color.white);
        add(sliderLabel,gbc);

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridy = 1;
        gbc.gridheight = 5;
        slider.setBackground(backgroundColor);
        add(slider,gbc);
        gbc.gridheight = 1;

        gbc.gridx = 1;
        add(igniterType,gbc);

        gbc.gridy = 2;
        add(coolingType,gbc);

        gbc.gridy = 3;
        importedCoolingMap.setEnabled(false);
        add(importedCoolingMap,gbc);

        gbc.gridy = 4;
        add(palette,gbc);

        gbc.gridy = 5;
        add(export,gbc);
    }

    private void addListeners() {

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int oxygen = slider.getValue();
                fire.setOxygen(oxygen);
            }
        });

        igniterType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fire.changeIgniterType();
                int actualType = fire.getIgniterType();
                if (actualType == fire.LINE ) {
                    igniterType.setText("Igniter: Line");;
                } else if (actualType == fire.RANDOM) {
                    igniterType.setText("Igniter: Random");
                } else if (actualType == fire.MEMORY) {
                    igniterType.setText("Igniter: Memory");
                }
            }
        });

        coolingType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fire.changeCoolingType();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                switch (fire.getCoolingType()) {
                    case CoolingMap.FLAT:
                        coolingType.setText("Cooling: Flat");
                        importedCoolingMap.setEnabled(false);
                        break;
                    case CoolingMap.RANDOM_GENERATED:
                        coolingType.setText("Cooling: Random");
                        break;
                    case CoolingMap.IMPORTED:
                        coolingType.setText("Cooling: Imported");
                        importedCoolingMap.setEnabled(true);
                        break;
                }
            }
        });

        importedCoolingMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fire.changeCoolingMap();
                importedCoolingMap.setText("CoolingMap: " + fire.getCoolingPath());
            }
        });

        palette.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaletteEditor paletteEditor = new PaletteEditor(fire);
                paletteEditor.open();
                paletteEditor.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosing(e);
                        adaptColors();
                    }
                });
            }
        });



    }

}
