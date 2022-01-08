import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.RELATIVE;

public class PaletteEditor extends JFrame {


    //Attributes

    private final Color buttonColor = new Color(250,250,250);
    private final Fire fire;
    private final Palette originalPalette;
    ArrayList<Color> colors = new ArrayList<>();
    private int selectedColorSlot;

    //Constructor


    public PaletteEditor(Fire fire) {
        super("Palette editor");
        this.fire = fire;
        originalPalette = fire.getPalette();
        paletteChooser = new PaletteChooser(fire);
        previewer = new PalettePreviewer(fire.getPalette(),false);
        this.setLayout(new GridBagLayout());
        addComponents();
        addListeners();
        new Thread(previewer).start();
    }


    //Components


    private final ArrayList<JButton> paletteButtons = new ArrayList<>();
    private final JPanel buttonsPanel = new JPanel();

    private final PalettePreviewer previewer;
    private final JColorChooser colorChooser = new JColorChooser();
    private final PaletteChooser paletteChooser;

    private final JButton addColor = new JButton("Add Color");
    private final JButton removeColor = new JButton("Remove Color");
    private final JButton apply = new JButton("Apply");
    private final JButton cancel = new JButton("Cancel");
    private final JPanel optionPanel = new JPanel();

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
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        setupColorChooser();
        add(colorChooser,gbc);

        gbc.insets = new Insets(20,10,0,30);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(paletteChooser,gbc);

        gbc.insets = new Insets(30,10,20,30);
        gbc.gridy = 2;
        previewer.setBackground(new Color(150,150,150));
        add(previewer,gbc);

        setupOptionPanel();
        gbc.insets = new Insets(0,0,0,0);
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(optionPanel,gbc);

    }

    private void addListeners() {

        addColor.addActionListener(e -> {
            if (colors.size() >= 20) {
                return;
            }

            JButton b = createColorButton(Color.white);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = paletteButtons.size()+1;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.fill = BOTH;

            buttonsPanel.add(b,gbc);
            paletteButtons.add(b);
            colors.add(Color.white);

            update(getGraphics());
            pack();

            paletteButtons.get(selectedColorSlot).setBackground(buttonColor);
            selectedColorSlot = colors.size()-1;
            paletteButtons.get(selectedColorSlot).setBackground(new Color(200,200,200));
            colorChooser.setColor(Color.WHITE);
            updatePreviewer();
        });

        removeColor.addActionListener(e -> {
            if (colors.size() < 3) {
                return;
            }
            colors.remove(selectedColorSlot);
            buttonsPanel.remove(selectedColorSlot);
            paletteButtons.remove(paletteButtons.get(selectedColorSlot));
            update(getGraphics());
            pack();

            if (selectedColorSlot >= colors.size()) {
                selectedColorSlot = colors.size()-1;
                paletteButtons.get(selectedColorSlot).setBackground(new Color(200,200,200));
                colorChooser.setColor(colors.get(colors.size() - 1));
            } else {
                colorChooser.setColor(colors.get(selectedColorSlot));
                paletteButtons.get(selectedColorSlot).setBackground(new Color(200,200,200));
            }
            updatePreviewer();
        });

        colorChooser.getSelectionModel().addChangeListener(e -> {
            Color c = colorChooser.getColor();
            setColorIcon(paletteButtons.get(selectedColorSlot), c);
            colors.set(selectedColorSlot,c);
            updatePreviewer();
        });

        apply.addActionListener(e -> {
            fire.setPalette(new Palette(colors));
            close();
        });

        cancel.addActionListener(e -> {
            close();
            fire.setPalette(originalPalette);
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                fire.setPalette(originalPalette);
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
        b.setBackground(buttonColor);

        b.addActionListener(e -> {
            paletteButtons.get(selectedColorSlot).setBackground(buttonColor);
            selectedColorSlot = paletteButtons.indexOf(b);
            paletteButtons.get(selectedColorSlot).setBackground(new Color(200,200,200));
            colorChooser.setColor(colors.get(selectedColorSlot));
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
        colorChooser.getChooserPanels()[0].getComponent(0).setVisible(false);
        colorChooser.getChooserPanels()[0].getComponent(0).setEnabled(false);
    }

    private void setupOptionPanel() {
        optionPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = RELATIVE;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        addColor.setBackground(buttonColor);
        optionPanel.add(addColor, gbc);
        removeColor.setBackground(buttonColor);
        optionPanel.add(removeColor, gbc);
        apply.setBackground(buttonColor);
        optionPanel.add(apply, gbc);
        cancel.setBackground(buttonColor);
        optionPanel.add(cancel, gbc);
    }

    private void setupPaletteButtons() {
        buttonsPanel.setLayout(new GridBagLayout());
        for (Color c : fire.getPalette().getBaseColors()) {
            colors.add(c);
            JButton b = createColorButton(c);
            paletteButtons.add(b);
        }
        selectedColorSlot = paletteButtons.size()-1;
        paletteButtons.get(selectedColorSlot).setBackground(new Color(200,200,200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
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
        Palette p = new Palette(colors);
        previewer.setPalette(p);
        fire.setPalette(p);
    }


    //--------------------------------------------------------------------------------
    //Inner Classes
    //--------------------------------------------------------------------------------


    public class PaletteChooser extends JPanel {


        private PaletteSaver paletteSaver;
        private final Fire fire;
        private boolean adding = false;

        private final JComboBox<String> paletteList = new JComboBox<>();
        private final JButton add = new JButton("Add");
        private final JButton remove = new JButton("Remove");
        private final JTextField textField = new JTextField();

        public PaletteChooser(Fire fire) {
            this.fire = fire;
            try {
                importPalettes();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            setLayout(new GridBagLayout());
            addComponents();
            addListeners();
        }

        private void addComponents() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = BOTH;
            gbc.weightx = 1;
            gbc.weighty = 1;

            gbc.gridwidth = 2;
            setupPaletteList();
            add(paletteList, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            add.setBackground(new Color(250, 250, 250));
            add(add, gbc);

            gbc.gridx = 1;
            remove.setBackground(new Color(250, 250, 250));
            add(remove, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            textField.setEnabled(false);
            add(textField, gbc);
        }

        private void addListeners() {

            paletteList.addItemListener(e -> {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                String selected = e.getItem().toString();
                fire.setPalette(paletteSaver.get(selected));
                close();
            });

            add.addActionListener(e -> {
                if (adding) {
                    setAdding(false);
                    if (!textField.getText().isBlank()) {
                        paletteList.addItem(textField.getText());
                        paletteSaver.add(textField.getText(), fire.getPalette());
                        textField.setText("");
                    }
                    paletteList.setEnabled(true);
                } else {
                    setAdding(true);
                }
            });

            remove.addActionListener(e -> {
                if (adding) {
                    setAdding(false);
                } else {
                    new PaletteRemover(paletteSaver.getPaletteNames());
                }
            });

        }

        private void importPalettes() throws IOException {
            String path = "src\\main\\resources\\palettes.json";
            paletteSaver = new PaletteSaver(path);
            while (!paletteSaver.exists()) {
                paletteSaver.createJSON();
            }

        }

        public void removePalette(int index) {
            paletteList.removeItem(paletteList.getItemAt(index+1));
            paletteSaver.remove(index);
        }

        public void setAdding(boolean b) {
            if (adding) {
                add.setText("Add");
                remove.setText("Remove");
            } else {
                add.setText("Save");
                remove.setText("Cancel");
            }
            adding = b;
            paletteList.setEnabled(!b);
            textField.setEnabled(b);
        }

        private void setupPaletteList() {
            paletteList.setBackground(Color.white);
            paletteList.addItem(" ");
            for (String s : paletteSaver.getPaletteNames()) {
                paletteList.addItem(s);
            }
        }


        //--------------------------------------------------------------------------------
        //Inner Classes
        //--------------------------------------------------------------------------------


        public class PaletteRemover extends JDialog {

            ArrayList<String> paletteNames;

            JButton remove = new JButton("Remove");
            JButton cancel = new JButton("Cancel");
            JList<String> list;

            public PaletteRemover(ArrayList<String> paletteNames) {
                this.paletteNames = paletteNames;
                setLayout(new GridBagLayout());
                addComponents();
                addListeners();
                setVisible(true);
                pack();
            }

            private void addComponents() {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.gridheight = 1;
                gbc.weightx = 1;
                gbc.weighty = 1;
                gbc.fill = BOTH;

                setupList();
                add(list,gbc);

                gbc.gridwidth = 1;
                gbc.gridy = 1;
                remove.setBackground(Color.white);
                add(remove,gbc);

                gbc.gridx = 1;
                cancel.setBackground(Color.white);
                add(cancel,gbc);

            }

            private void addListeners() {
                remove.addActionListener(e -> {
                    setVisible(false);
                    dispose();
                    removePalette(list.getSelectedIndex());
                });

                cancel.addActionListener(e -> {
                    setVisible(false);
                    dispose();
                });
            }

            private void setupList() {
                String[] names = paletteNames.toArray(new String[0]);
                list = new JList<>(names);
                list.setBackground(Color.lightGray);
                list.setForeground(Color.darkGray);
                list.setSelectionBackground(new Color(150,30,30));
                list.setSelectionForeground(Color.white);
            }

        }

    }


}
