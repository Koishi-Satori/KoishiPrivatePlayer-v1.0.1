package top.kkoishi.files;

import top.kkoishi.tools.Warn;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SettingFrame extends JFrame {
    JPanel panel = new JPanel();
    JTextArea area = new JTextArea();
    JButton repeat = new JButton("repeat");
    JButton random = new JButton("random");
    JTextArea field = new JTextArea("10");
    JButton confirm = new JButton("confirm");
    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public SettingFrame () throws HeadlessException, IOException {
        super("Player Mode->" +new Files().read("./settings.pack").split(";")[0]);
        setLocationByPlatform(true);
        setSize(300,200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel.setLayout(null);

        area.setText("key in a number to change circulated play times");
        area.setLineWrap(true);
        area.setEditable(false);
        area.setEnabled(false);
        area.setBounds(10,10,280,50);
        repeat.setBounds(40,50,90,30);
        random.setBounds(170,50,90,30);
        field.setBounds(10,90,250,30);
        confirm.setBounds(100,150,100,30);
        panel.add(area);
        panel.add(random);
        panel.add(repeat);
        panel.add(field);
        panel.add(confirm);
        add(panel);
        setVisible(true);

        repeat.addActionListener(e -> {
            try {
                int time = Integer.parseInt(new Files().read("./settings.pack").split(";")[1]);
                new Files().write("./settings.pack","repeat;" + time + ";",false);
                setTitle("Player Mode->" +new Files().read("./settings.pack").split(";")[0]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        random.addActionListener(e -> {
            try {
                int time = Integer.parseInt(new Files().read("./settings.pack").split(";")[1]);
                new Files().write("./settings.pack","random;" + time + ";",false);
                setTitle("Player Mode->" +new Files().read("./settings.pack").split(";")[0]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        confirm.addActionListener(e -> {
            if (field.getText().matches("\\d+")) {
                try {
                    String mode = new Files().read("./settings.pack").split(";")[0];
                    new Files().write("./settings.pack",mode + ";" + field.getText() + ";",false);
                    new Warn().frameWithOneButton("Success","change to" + field.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    this.dispose();
                }
            } else {
                new Warn().frameWithOneButton("Error","Need a number!");
            }
        });
    }
}
