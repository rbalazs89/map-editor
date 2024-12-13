package Main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame window;
    public static void main(String[] args) {

        //Image cursorImage = Toolkit.getDefaultToolkit().getImage(Main.Main.class.getResource("/ui/pointer.png"));
        //Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(1, 1), "customCursor");

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("MapEditor");
        //window.setCursor(customCursor);
        //window.setUndecorated(true); // top bar gone, better to keep it, if user wants to close

        EditorPanel editorPanel = new EditorPanel();
        window.add(editorPanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        editorPanel.setupEditor();
        editorPanel.startThread();
    }
}