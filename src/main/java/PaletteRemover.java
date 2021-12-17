import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;

public class PaletteRemover extends JDialog {

    ArrayList<String> paletteNames;
    String chosen;

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
            chosen =  list.getSelectedValue();
            setVisible(false);
            dispose();
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
