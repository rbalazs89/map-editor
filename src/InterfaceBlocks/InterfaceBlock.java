package InterfaceBlocks;

import Main.EditorPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class InterfaceBlock {
    public int startX, startY, endX, endY, height, width;
    //public BufferedImage image;
    EditorPanel ep;
    Graphics2D g2;

    public InterfaceBlock(EditorPanel ep) {
        this.ep = ep;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
    }

    public void update(){

    }
}
