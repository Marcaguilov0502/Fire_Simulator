import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.GridBagConstraints.BOTH;
import static java.lang.Thread.sleep;

public class GifExporter extends JDialog {


    //Attributes


    private final TImage img;


    //Components


    private final JLabel nameLabel = new JLabel("Name: ");
    private final JTextField name = new JTextField();

    private final JLabel durationLabel = new JLabel("Duration (seconds): ");
    private final JSpinner duration = new JSpinner();

    private final JButton cancel = new JButton("Cancel");
    private final JButton export = new JButton("Export");


    //Constructor


    public GifExporter(TImage img) {
        super();
        this.img = img;
        addComponents();
        addListeners();
    }

    private void addComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        add(nameLabel,gbc);

        gbc.gridx = 1;
        add(name,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(durationLabel,gbc);

        gbc.gridx = 1;
        add(duration,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(cancel,gbc);

        gbc.gridx = 1;
        add(export,gbc);

    }

    private void addListeners() {

        cancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        export.addActionListener(e -> {

            if (name.getText().isBlank() || (Integer)(duration.getValue()) < 0) {
                return;
            }

            new Thread(() -> {
                try {
                    writeGif(name.getText(), (Integer) duration.getValue());
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            }).start();

            setVisible(false);
            dispose();
        });
    }

    private void writeGif(String name, int duration) throws IOException, InterruptedException {

        int delay = 10;
        JDialog progress = new JDialog();
        JProgressBar progressBar = new JProgressBar();
        progress.add(progressBar);
        progressBar.setMinimum(0);
        progressBar.setMaximum(duration*delay);
        progress.setVisible(true);
        progress.pack();
        progress.setLocation(getLocation());

        if (!new File("src\\main\\resources\\gifs").exists()) {
            new File("src\\main\\resources\\gifs").mkdir();
        }
        ImageOutputStream output = new FileImageOutputStream(new File("src\\main\\resources\\gifs\\"+name+".gif"));
        GifSequenceWriter writer = new GifSequenceWriter(output, img.getType(), delay, true);
        for (int i = 0; i++<duration*delay;) {
            writer.writeToSequence(img);
            sleep(delay);
            progressBar.setValue(i);
        }
        writer.close();
        output.close();

        progress.setVisible(false);
    }

}
