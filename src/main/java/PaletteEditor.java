import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.REMAINDER;
import static java.lang.Thread.sleep;

public class PaletteEditor extends JFrame {


    //Attributes


    private Fire fire;
    ArrayList<Color> colors = new ArrayList<>();
    private int selectedColorSlot;

    //Constructor


    public PaletteEditor(Fire fire) {
        super("Palette editor");
        this.fire = fire;
        previewer = new PalettePreviewer(fire.getPalette());
        this.setLayout(new GridBagLayout());
        addComponents();
        addListeners();
        new Thread(previewer).start();
    }


    //Components


    private ArrayList<JButton> paletteButtons = new ArrayList<>();
    private JPanel buttonsPanel = new JPanel();

    private PalettePreviewer previewer;
    private JColorChooser colorChooser = new JColorChooser();

    private JButton addColor = new JButton("Add Color");
    private JButton removeColor = new JButton("Remove Color");
    private JButton apply = new JButton("apply");
    private JButton cancel = new JButton("Cancel");

    //Methods


    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;

        setupPaletteButtons();
        add(buttonsPanel,gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(43,20,43,10);
        previewer.setBackground(new Color(150,150,150));
        add(previewer,gbc);


        gbc.insets = new Insets(0,0,0,0);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        setupColorChooser();
        add(colorChooser,gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(addColor,gbc);
        gbc.gridx++;
        add(removeColor,gbc);
        gbc.gridx++;
        add(apply,gbc);
        gbc.gridx++;
        add(cancel,gbc);

    }

    private void addListeners() {

        addColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (colors.size() >= 20) {
                    return;
                }

                JButton b = createColorButton(Color.white);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = paletteButtons.size();
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.weighty = 1;
                gbc.fill = BOTH;

                buttonsPanel.add(b,gbc);
                paletteButtons.add(b);
                colors.add(Color.white);

                update(getGraphics());
                pack();

                selectedColorSlot = colors.size()-1;
                colorChooser.setColor(Color.WHITE);
                updatePreviewer();
            }
        });

        removeColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (colors.size() < 3) {
                    return;
                }
                colors.remove(selectedColorSlot);
                buttonsPanel.remove(selectedColorSlot);
                paletteButtons.remove(paletteButtons.get(paletteButtons.size()-1));
                update(getGraphics());
                pack();

                if (selectedColorSlot >= colors.size()) {
                    selectedColorSlot = colors.size()-1;
                    colorChooser.setColor(colors.get(colors.size() - 1));
                }
                updatePreviewer();
            }
        });

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color c = colorChooser.getColor();
                setColorIcon(paletteButtons.get(selectedColorSlot), c);
                colors.set(selectedColorSlot,c);
                updatePreviewer();
            }
        });

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fire.setPalette(new Palette(colors));
                close();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

    }

    private void close() {
        setVisible(false);
        dispose();
    }

    private JButton createColorButton(Color c) {
        JButton b = new JButton();
        setColorIcon(b,c);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedColorSlot = paletteButtons.indexOf(b);
                colorChooser.setColor(colors.get(selectedColorSlot));
            }
        });

        return b;
    }

    private void setColorIcon(JButton b, Color c) {
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(c);
        graphics.fillRect(0, 0, 20, 20);
        graphics.setXORMode(Color.DARK_GRAY);
        graphics.drawRect(0, 0, 20 - 1, 20 - 1);
        image.flush();
        ImageIcon icon = new ImageIcon(image);
        b.setIcon(icon);
    }

    private void setupColorChooser() {
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[0]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.setColor(colors.get(selectedColorSlot));
    }

    private void setupPaletteButtons() {
        buttonsPanel.setLayout(new GridBagLayout());
        for (Color c : fire.getPalette().getBaseColors()) {
            colors.add(c);
            JButton b = createColorButton(c);
            paletteButtons.add(b);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.fill = BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        for (JButton b : paletteButtons) {
            buttonsPanel.add(b, gbc);
            gbc.gridx++;
        }
        selectedColorSlot = paletteButtons.size()-1;
    }

    public void open() {
        setSize(500,500);
        setVisible(true);
        pack();
    }

    public void updatePreviewer() {
        previewer.setPalette(new Palette(colors));
    }


}
