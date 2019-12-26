package conwaywar;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window {

    public JFrame frame;

    public Window(int width, int height, String title, Canvas game) {
        frame = new JFrame(title);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.getContentPane().setMaximumSize(new Dimension(width, height));
        frame.getContentPane().setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
    }

    public int getWidth() {
        return frame.getContentPane().getWidth();
    }

    public int getHeight() {
        return frame.getContentPane().getHeight();
    }
}
