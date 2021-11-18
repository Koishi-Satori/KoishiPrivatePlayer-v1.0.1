package top.kkoishi;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import top.kkoishi.files.Files;
import top.kkoishi.files.SettingFrame;
import top.kkoishi.files.SongListFrame;
import top.kkoishi.tools.AudioLength;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author KKoihi
 */
public class Main {
    static Player[] player;
    static String mode;
    static AudioLength length = new AudioLength();

    //原子类 提供循环播放次数

    static AtomicReference<Integer> circulation = new AtomicReference<>(1);
    static AtomicReference<Integer> atomicReferenceTimer = new AtomicReference<>(0);

    public static void main (String[] args) {
        JFrame frame = new JFrame("Koishi Music Player Test");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(600,300);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton songList = new JButton("Song List");
        JButton start = new JButton("Start");
        JButton end = new JButton("End");
        JButton info = new JButton("Info");
        JButton setting = new JButton("Settings");
        JButton exit = new JButton("Exit");
        songList.setOpaque(false);
        songList.setContentAreaFilled(false);
        start.setOpaque(false);
        start.setContentAreaFilled(false);
        end.setOpaque(false);
        end.setContentAreaFilled(false);
        info.setOpaque(false);
        info.setContentAreaFilled(false);
        setting.setOpaque(false);
        setting.setContentAreaFilled(false);
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBorder(BorderFactory.createLineBorder(Color.BLUE,1,true));
        area.setBackground(Color.WHITE);
        area.setFont(new Font("",Font.PLAIN,15));
        JProgressBar bar = new JProgressBar();
        bar.setMaximum(0);
        bar.setMaximum(100);
        JLabel label = new JLabel("Current thread time:");
        songList.setBounds(0,0,100,30);
        start.setBounds(100,0,100,30);
        end.setBounds(200,0,100,30);
        info.setBounds(300,0,100,30);
        setting.setBounds(400,0,100,30);
        exit.setBounds(500,0,100,30);
        area.setBounds(100,50,400,150);
        bar.setBounds(50,235,500,15);
        label.setBounds(50,215,400,20);
        label.setFont(new Font("",Font.BOLD,15));
        panel.add(songList);
        panel.add(start);
        panel.add(end);
        panel.add(info);
        panel.add(setting);
        panel.add(exit);
        panel.add(area);
        panel.add(bar);
        panel.add(label);
        frame.add(panel);
        frame.setVisible(true);

        songList.addActionListener(e -> new SongListFrame());

        class barLoader extends Thread {
            /**
             * If this thread was constructed using a separate
             * {@code Runnable} run object, then that
             * {@code Runnable} object's {@code run} method is called;
             * otherwise, this method does nothing and returns.
             * <p>
             * Subclasses of {@code Thread} should override this method.
             *
             * @see #start()
             */
            @Override
            public void run () {
                int tick = 0;
                int temp = atomicReferenceTimer.get();
                bar.setMaximum(temp);
                while (temp > tick) {
                    bar.setValue(tick++);
                    label.setText("Current thread time:" +
                            (tick / 60) + ":" + (tick % 60) +
                            "/" +
                            (temp / 60) + ":" + (temp % 60));
                    try {
                        System.out.println(System.currentTimeMillis());
                        Thread.sleep(1000);
                        System.out.println(System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                bar.setValue(0);
                tick = 0;
            }
        }

        Thread thread = new Thread(() -> {
            try {
                String sets = new Files().read("./settings.pack");
                mode = sets.split(";")[0];
                circulation.set(Integer.parseInt(sets.split(";")[1]));
                System.out.println(circulation.get());
                System.out.println(mode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int counter = 0;
            while (counter <= circulation.get()) {
                try {
                    String unformatted = new Files().read(SongListFrame.SONGS_LIST);
                    String[] temp = unformatted.split("\\|");
                    int len = temp.length;
                    String[] paths = new String[len / 2];
                    player = new Player[len / 2];
                    for (int i = 0; i < len; i++) {
                        if ((i + 1) % 2 == 0) {
                            paths[i / 2] = temp[i];
                        }
                    }

                    if ("random".equals(mode)) {
                        //洗牌算法
                        shuffle(paths);
                        System.out.println(Arrays.toString(paths));
                    }

                    for (int i = 0; i < len / 2; i++) {
                        try {
                            //create player thread
                            player[i] = new Player(new BufferedInputStream(new FileInputStream(paths[i])));
                            // System.out.println(length.getLength(paths[i]));
                        } catch (JavaLayerException ex) {
                            ex.printStackTrace();
                        }
                    }

                    //播放音乐

                    barLoader loader;
                    for (int i = 0; i < len / 2; i++) {
                        try {bar.setValue(0);
                            int time = length.getLength(paths[i]);
                            atomicReferenceTimer.set(time);
                            String[] information = length.getInfo(paths[i]);
                            area.setText("Player mode:" + mode +
                                            "\nSong info:" +
                                            "\n\tAlbum:" +information[0] +
                                            "\n\tArtist:" + information[1] +
                                            "\n\tName:" + information[2] +
                                            "\n\tLength:" + (time / 60) + ":" + (time % 60)
                                    );

                            loader = new barLoader();
                            loader.start();
                            player[i].play();
                            player[i].close();
                        } catch (JavaLayerException ex) {
                            ex.printStackTrace();
                        }
                    }
                    counter++;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        start.addActionListener(e -> {
            thread.start();
        });

        end.addActionListener(e -> {
            if (player != null) {
                for (Player player : player) {
                    player.close();
                }
                thread.interrupt();
                System.out.println(thread.isInterrupted());
            }
        });

        setting.addActionListener(e -> {
            try {
                new SettingFrame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exit.addActionListener(e -> {
            System.gc();
            System.exit(514);
        });

        frame.addWindowStateListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             */
            @Override
            public void windowClosing (WindowEvent e) {
                System.exit(514);
            }
        });
    }

    private static String[] shuffle (String[] array) {
        int len = array.length;
        Random random = new Random(System.nanoTime());
        for (int i = 0;i < len / 2;i++) {
            int index = random.nextInt(len);
            String temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
        return array;
    }
}