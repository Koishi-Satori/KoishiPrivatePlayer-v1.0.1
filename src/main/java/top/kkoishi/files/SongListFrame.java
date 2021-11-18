package top.kkoishi.files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class SongListFrame extends JFrame {
    public static final String SONGS_LIST = "./songs.list";
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
    public SongListFrame () throws HeadlessException {
        setTitle("Songs List");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationByPlatform(true);
        setSize(300,600);
        JMenuBar bar = new JMenuBar();
        JMenu add = new JMenu("Add");
        JMenu edit = new JMenu("Edit");
        JMenu save = new JMenu("Save");
        JMenu back = new JMenu("Back");
        bar.add(add);
        bar.add(edit);
        bar.add(save);
        bar.add(back);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        try {
            area.setText(new Files().read(SONGS_LIST));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(area);
        setJMenuBar(bar);
        add(scrollPane);
        setVisible(true);

        add.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mousePressed (MouseEvent e) {
                try {
                    String path = new FileChooser().file();
                    new Files().write(SONGS_LIST,"|" + path + "|\n",true);
                    area.setText(new Files().read(SONGS_LIST));
                    area.setEditable(false);
                } catch (ClassNotFoundException | InstantiationException | IOException |
                        IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
            }
        });

        edit.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mousePressed (MouseEvent e) {
                area.setEditable(true);
            }
        });

        save.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mousePressed (MouseEvent e) {
                if (!"".equals(area.getText())) {
                    try {
                        new Files().write(SONGS_LIST,area.getText(),false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        back.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked (MouseEvent e) {
                dispose();
            }
        });
    }
}
