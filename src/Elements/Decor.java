package Elements;

import java.awt.image.BufferedImage;

public class Decor {
    public String name = "decor001";
    public int id;
    public int arrayPosition;
    // if bigger than a tile:
    public boolean big;
    public BufferedImage image;
    public int imageHeight;
    public int imageWidth;
    public int worldX;
    public int worldY;
    public int worldMiddleX;
    public int worldMiddleY;
    public boolean drawBeforeEntity;
    public boolean selected;

    public Decor(){

    }

}
