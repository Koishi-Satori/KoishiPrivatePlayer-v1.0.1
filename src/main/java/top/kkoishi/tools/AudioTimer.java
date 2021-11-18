package top.kkoishi.tools;

import top.kkoishi.AbstructTextArea;

public class AudioTimer extends AbstructTextArea implements Runnable {
    int total;
    int cur;

    public AudioTimer () {
    }

    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public AudioTimer (int length) {
        this.total = 0;
        this.cur = 0;
        flush(length);
        setBounds(100,250,400,100);
        AudioTimer.this.run();
    }

    public void flush (int length) {
        total = length;
        cur = 0;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run () {
        while (cur < total) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setText("Current time:" + cur + "/" + total);
            cur++;
        }
    }
}
