package top.kkoishi.files;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {

    public String file () throws ClassNotFoundException, InstantiationException
            , IllegalAccessException, UnsupportedLookAndFeelException {
        String path = null;
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MusicFile", "mp3");
        jFileChooser.setFileFilter(filter);
        jFileChooser.setDialogTitle("Please select file path");
        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            path = jFileChooser.getSelectedFile().getAbsolutePath();
        }
        return path;
    }

}
