package Tool;

import java.awt.*;

public class StateSwitchButton extends MyButton {
    public int buttonWidth;
    public int buttonHeight;
    public String caption;

    public StateSwitchButton(){

    }

    public void draw(Graphics2D g2) {
        if(isPressed) {
            g2.setColor(new Color(166, 166, 166));
            g2.fillRect(posX, posY, buttonWidth, buttonHeight);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(137, 137, 137));
            g2.drawRect(posX, posY, buttonWidth, buttonHeight);

            g2.setColor(new Color(176, 176, 176));
            g2.drawRect(posX + 1, posY + 1, buttonWidth - 2, buttonHeight);

            g2.setColor(new Color(187,187,187));
            g2.fillRect(posX + 2, posY + 2, buttonWidth - 3, buttonHeight + 1);

        } else {
            g2.setColor(new Color(166, 166, 166));
            g2.fillRect(posX, posY, buttonWidth, buttonHeight);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(137, 137, 137));
            g2.drawRect(posX, posY, buttonWidth, buttonHeight);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(205, 205, 205));
            g2.drawRect(posX + 1, posY + 1, buttonWidth - 2, buttonHeight);
        }

        g2.setColor(Color.BLACK);
        g2.drawString(caption, posX + 10, posY + 20);
    }
}
