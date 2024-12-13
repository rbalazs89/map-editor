package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {
    EditorPanel ep;
    public boolean ctrlPressed, deletePressed;

    KeyHandler(EditorPanel editorPanel){
        this.ep = editorPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_CONTROL) {
            ctrlPressed = true;
        } else if (code == KeyEvent.VK_DELETE){
            deletePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_CONTROL) {
            ctrlPressed = false; // Reset flag when Ctrl is released
        } else if (code == KeyEvent.VK_DELETE){
            deletePressed = false;
        }
    }
}
