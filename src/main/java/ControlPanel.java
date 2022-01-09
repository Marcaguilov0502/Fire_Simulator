import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static java.awt.GridBagConstraints.*;

public class ControlPanel extends JPanel {

    private final Color backgroundColor = new Color(20,20,20);
    private final Fire fire;
    private final Configuration config;
    private final ExtendedControlPanel extendedControlPanel;

    public ControlPanel(Fire fire) {
        super();
        this.fire = fire;
        this.config = fire.getConfiguration();
        extendedControlPanel = new ExtendedControlPanel(config);
        this.setBackground(backgroundColor);
        this.setLayout(new GridBagLayout());
        addContents();
        adaptColors();
        addListeners();
    }

    private final ExtendedControlPanel.LabeledJSlider oxygen = new ExtendedControlPanel.LabeledJSlider("Oxygen", new JSlider(JSlider.VERTICAL, 1,100,50));

    private final JButton igniterType = new JButton("Igniter: Memory");
    private final JButton coolingType = new JButton("Cooling: Flat");
    private final JButton importedCoolingMap = new JButton("Cooling Map: 0");
    private final JButton palette = new JButton("Edit Palette");
    private final JButton export = new JButton("Export");

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
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridheight = 5;
        gbc.fill = BOTH;
        oxygen.getSlider().setBackground(backgroundColor);
        oxygen.setBackground(backgroundColor);
        add(oxygen,gbc);

        gbc.gridx = 1;
        gbc.gridheight = 1;
        add(igniterType,gbc);

        gbc.gridy = 1;
        add(coolingType,gbc);

        gbc.gridy = 2;
        add(importedCoolingMap,gbc);

        gbc.gridy = 3;
        add(palette,gbc);

        gbc.gridy = 4;
        add(export,gbc);

    }

    private void addListeners() {

        oxygen.getSlider().addChangeListener(e -> {
            int value = oxygen.getSlider().getValue();
            config.setOxygen(value);

            extendedControlPanel.getCoolingPower().getSlider().setValue((int) (config.getCoolingPower()*5));
            extendedControlPanel.getIgniterSpeed().getSlider().setValue((int) config.getIgniterSpeed());
            extendedControlPanel.getIgnitionDensity().getSlider().setValue((int) (config.getIgnitionDensity()));
            extendedControlPanel.getIgniterMaxSize().getSlider().setValue((int) (config.getIgniterMaxSize()));

        });

        igniterType.addActionListener(e -> {
            config.changeIgniterType();
            int actualType = config.getIgniterType();
            if (actualType == Configuration.LINE) {
                igniterType.setText("Igniter: Line");
                extendedControlPanel.getIgniterCount().getSpinner().setEnabled(false);
                extendedControlPanel.getIgniterSpeed().getSlider().setEnabled(false);
                extendedControlPanel.getIgniterMaxSize().getSlider().setEnabled(false);
                extendedControlPanel.getIgniterCount().getLabel().setForeground(new Color(150,150,150));
                extendedControlPanel.getIgniterSpeed().getLabel().setForeground(new Color(150,150,150));
                extendedControlPanel.getIgniterMaxSize().getLabel().setForeground(new Color(150,150,150));
            } else if (actualType == Configuration.RANDOM) {
                igniterType.setText("Igniter: Random");
                extendedControlPanel.getIgniterCount().getSpinner().setEnabled(false);
                extendedControlPanel.getIgniterSpeed().getSlider().setEnabled(false);
                extendedControlPanel.getIgniterMaxSize().getSlider().setEnabled(false);
                extendedControlPanel.getIgniterCount().getLabel().setForeground(new Color(150,150,150));
                extendedControlPanel.getIgniterSpeed().getLabel().setForeground(new Color(150,150,150));
                extendedControlPanel.getIgniterMaxSize().getLabel().setForeground(new Color(150,150,150));
            } else if (actualType == Configuration.MEMORY) {
                igniterType.setText("Igniter: Memory");
                extendedControlPanel.getIgniterCount().getSpinner().setEnabled(true);
                extendedControlPanel.getIgniterSpeed().getSlider().setEnabled(true);
                extendedControlPanel.getIgniterMaxSize().getSlider().setEnabled(true);
                extendedControlPanel.getIgniterCount().getLabel().setForeground(Color.white);
                extendedControlPanel.getIgniterSpeed().getLabel().setForeground(Color.white);
                extendedControlPanel.getIgniterMaxSize().getLabel().setForeground(Color.white);
            }
        });

        coolingType.addActionListener(e -> {
            try {
                fire.changeCoolingType();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            switch (config.getCoolingType()) {
                case Configuration.FLAT:
                    coolingType.setText("Cooling: Flat");
                    importedCoolingMap.setEnabled(false);
                    break;
                case Configuration.RANDOM_GENERATED:
                    coolingType.setText("Cooling: Random");
                    break;
                case Configuration.IMPORTED:
                    coolingType.setText("Cooling: Imported");
                    importedCoolingMap.setEnabled(true);
                    break;
            }
        });

        importedCoolingMap.addActionListener(e -> {
            config.nextCoolingPath();
            fire.getCoolingMap().updateImportedCoolingMap();
            importedCoolingMap.setText("CoolingMap: " + config.getCoolingPath());
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

        export.addActionListener(e -> {
            GifExporter ge = new GifExporter(fire);
            ge.setVisible(true);
            ge.setLocation(900,400);
            ge.pack();
        });
    }

    public ExtendedControlPanel getExtendedControlPanel() {
        return extendedControlPanel;
    }

}
