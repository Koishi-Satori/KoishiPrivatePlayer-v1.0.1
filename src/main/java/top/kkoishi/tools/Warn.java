package top.kkoishi.tools;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Warn {
    public void frameWithOneButton(String title,String text) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.setSize(300,200);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTextArea area = new JTextArea("");
        area.setEditable(true);
        area.setBackground(frame.getBackground());
        area.setText(text);
        area.setBounds(10,10,270,110);
        JButton button = new JButton("Close");
        button.addActionListener(e->frame.dispose());
        button.setBounds(100,140,100,30);

        panel.add(area);
        panel.add(button);
        panel.setVisible(true);
        frame.add(panel);
        frame.setVisible(true);
    }
}
