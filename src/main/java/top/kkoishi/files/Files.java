package top.kkoishi.files;

import java.io.*;

public class Files {
    public String read (String path) throws IOException {
        if (!new File(path).exists()) {
            new File(path).createNewFile();
        }
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int len;
        char[] chars = new char[1024];
        StringBuilder ans = new StringBuilder();
        while ((len = reader.read(chars)) != -1) {
            ans.append(new String(chars,0,len));
        }
        reader.close();
        return ans.toString();
    }

    public void write (String path, String content, boolean ifAppend) throws IOException {
        if (!new File(path).exists()) {
            new File(path).createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, ifAppend));
        writer.write(content);
        writer.close();
    }
}
