package Tool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MyButton {
    public boolean isHovered;
    public boolean isClicked;
    public boolean isPressed;
    public BufferedImage image;
    public int posX, posY;
    public int size = 30;

    public MyButton(){

    }

    public void draw(Graphics2D g2) {
        // Set the border color (always the same)
        Color borderColor = new Color(137, 137, 137); // RGB(137, 137, 137)

        // Set the fill color based on hover or click state
        Color fillColor;
        if (isPressed) {
            fillColor = new Color(166, 166, 166); // RGB(166, 166, 166) when clicked
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1)); // 1 pixel stroke for the outer border
            g2.drawRoundRect(posX - 5, posY - 5, size + 10, size + 10, 10, 10); // Outer border

        } else if (isHovered) {
            fillColor = new Color(236, 236, 236); // RGB(236, 236, 236) when hovered
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1)); // 1 pixel stroke for the outer border
            g2.drawRoundRect(posX - 5, posY - 5, size + 10, size + 10, 10, 10); // Outer border

        } else {
            fillColor = new Color(187, 187, 187); // Default fill color (white)
        }

        // Draw the filled background first
        g2.setColor(fillColor);
        g2.fillRoundRect(posX - 5, posY - 5, size + 10, size + 10, 10, 10); // Filled background

        // Draw the button image on top of the background
        if(image != null){
            g2.drawImage(image, posX, posY, size, size, null);
        }
    }
}
