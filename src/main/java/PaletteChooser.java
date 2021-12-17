import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;

import static java.awt.GridBagConstraints.BOTH;

public class PaletteChooser extends JPanel {


    private PaletteSaver paletteSaver;
    private final Fire fire;
    private boolean adding = false;

    private final JComboBox paletteList = new JComboBox();
    private final JButton add = new JButton("Add");
    private final JButton remove = new JButton("Remove");
    private final JTextField textField = new JTextField();
    private PaletteRemover paletteRemover;

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
        });

        add.addActionListener(e -> {
            if (adding) {
                setAdding(false);
                if (!textField.getText().isBlank()) {
                    paletteList.addItem(textField.getText());
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
            System.out.println(2);
            paletteSaver.createJSON();
        }

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

}
