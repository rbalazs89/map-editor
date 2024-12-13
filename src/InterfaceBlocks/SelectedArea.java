package InterfaceBlocks;

import Elements.Decor;
import Main.EditorPanel;
import Elements.Tile;

import java.awt.*;

public class SelectedArea extends InterfaceBlock{
    public int selectedTileRow = -1;
    public int selectedTileCol = -1;
    public int hoveredX = -1;
    public int hoveredY = -1;
    public int hoveredCoordX = -1;
    public int hoveredCoordY = -1;
    public int infoTableStartX, infoTableStartY, infoTableWidth, infoTableRowHeight, startWhiteX, startWhiteY;
    public SelectedArea(EditorPanel ep) {
        super(ep);
        startX = ep.mapArea.endX;
        startY = ep.buttonsArea.endY;
        width = ep.screenWidth - startX;
        height = ep.screenHeight - startY;
        endY = ep.screenHeight;
        endX = ep.screenWidth;

        infoTableStartX = startX + 15;
        infoTableStartY = startY + 275;
        infoTableWidth = 200;
        infoTableRowHeight = 20;

        startWhiteX = startX + 5;
        startWhiteY = startY + 30;
    }

    public void draw(Graphics2D g2) {
        drawBackGround(g2);
        drawWhiteCaption(g2);
        drawWhiteArea(g2);
        if(ep.panelMode == EditorPanel.tileMode){
            drawTileInfo(g2);
        } else if(ep.panelMode == EditorPanel.decorationMode){
            drawDecorationInfo(g2);
        }
        drawHoveredCoordinates(g2);
    }
    public void drawDecorationInfo(Graphics2D g2){
        if(ep.selectedDecor == null){
            return;
        }
        int tableWidth = infoTableWidth; // Width of the table
        int rowHeight = infoTableRowHeight; // Height of each row
        int colWidth = tableWidth / 2; // Half width for two columns
        Color greyish = new Color(230,230,230);
        Color whiteish = new Color(240,240,240);

        int x = infoTableStartX;
        int y = infoTableStartY;

        Decor currentDecor = ep.decorManager.decors[ep.selectedDecor.id];
        if(ep.isSelectedFromMap){
            if(ep.selectedDecor != null){
                currentDecor = ep.selectedDecor;
                for (int i = 0; i < ep.decorManager.decors.length; i++) {
                    if(ep.decorManager.decors[i].name.equals(currentDecor.name)){
                        currentDecor.id = ep.decorManager.decors[i].id;
                        break;
                    }
                }
            }

        }

        String[][] tableData = {
                {"Property", "Value"},
                {"Element ID", String.valueOf(currentDecor.id)},
                {"Element name", currentDecor.name},
                {"Image width", currentDecor.imageWidth + ""},
                {"Image height", currentDecor.imageHeight + ""},
                {"X coordinate", ep.isSelectedFromMap ? String.valueOf(currentDecor.worldX) : "N/A"},
                {"Y coordinate", ep.isSelectedFromMap ? String.valueOf(currentDecor.worldY) : "N/A"},
                {"Row", ep.isSelectedFromMap ? String.valueOf(currentDecor.worldX / 64) : "N/A"},
                {"Column", ep.isSelectedFromMap ? String.valueOf(currentDecor.worldY / 64) : "N/A"}
        };
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, tableWidth, rowHeight * tableData.length); // Background
        g2.setColor(Color.GRAY);
        g2.drawRect(x, y, tableWidth, rowHeight * tableData.length); // Outer border

        for (int i = 0; i < tableData.length; i++) {
            int rowY = y + i * rowHeight;

            // Set background color for the row
            g2.setColor(i % 2 == 0 ? greyish : whiteish);
            if(i == 0){
                g2.setColor(new Color(137,137,137));
            }
            g2.fillRect(x, rowY, tableWidth, rowHeight);

            // Draw property name (left column)
            g2.setColor(Color.BLACK);
            g2.drawRect(x, rowY, colWidth, rowHeight); // Border for the left cell
            g2.drawString(tableData[i][0], x + 5, rowY + rowHeight / 2 + 5);

            // Draw value (right column)
            g2.drawRect(x + colWidth, rowY, colWidth, rowHeight); // Border for the right cell
            g2.drawString(tableData[i][1], x + colWidth + 5, rowY + rowHeight / 2 + 5);
        }

        int originalWidth = currentDecor.imageWidth;
        int originalHeight = currentDecor.imageHeight;

        // Calculate the aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;

        // Initialize new dimensions
        int newWidth = 200;
        int newHeight = 200;

        // Adjust dimensions to fit within 200x200 while maintaining the aspect ratio
        if (aspectRatio > 1) {
            // Width is the limiting factor
            newHeight = (int) (200 / aspectRatio);
        } else {
            // Height is the limiting factor
            newWidth = (int) (200 * aspectRatio);
        }

        // Calculate the x and y positions to center the image within the 200x200 box
        x = startWhiteX + 10 + (200 - newWidth) / 2;
        y = startWhiteY + 10 + (200 - newHeight) / 2;

        // Draw the image
        g2.drawImage(currentDecor.image, x, y, newWidth, newHeight, null);
    }

    public void drawTileInfo(Graphics2D g2) {
        if (ep.mapArea.getSelectedTile() != -1) {
            int tableWidth = infoTableWidth; // Width of the table
            int rowHeight = infoTableRowHeight; // Height of each row
            int colWidth = tableWidth / 2; // Half width for two columns
            Color greyish = new Color(230,230,230);
            Color whiteish = new Color(240,240,240);

            int x = infoTableStartX;
            int y = infoTableStartY;

            Tile currentTile = ep.tileM.tile[ep.mapArea.selectedTile];

            // Prepare data for the table
            String[][] tableData = {
                    {"Property", "Value"},
                    {"Tile ID", String.valueOf(ep.mapArea.selectedTile)},
                    {"Collision", String.valueOf(currentTile.collision)},
                    {"Row", selectedTileRow != -1 ? String.valueOf(selectedTileRow) : "N/A"},
                    {"Column", selectedTileCol != -1 ? String.valueOf(selectedTileCol) : "N/A"}
            };

            // Draw background and border for the table
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, tableWidth, rowHeight * tableData.length); // Background
            g2.setColor(Color.GRAY);
            g2.drawRect(x, y, tableWidth, rowHeight * tableData.length); // Outer border

            // Draw rows and columns
            for (int i = 0; i < tableData.length; i++) {
                int rowY = y + i * rowHeight;

                // Set background color for the row
                g2.setColor(i % 2 == 0 ? greyish : whiteish);
                if(i == 0){
                    g2.setColor(new Color(137,137,137));
                }
                g2.fillRect(x, rowY, tableWidth, rowHeight);

                // Draw property name (left column)
                g2.setColor(Color.BLACK);
                g2.drawRect(x, rowY, colWidth, rowHeight); // Border for the left cell
                g2.drawString(tableData[i][0], x + 5, rowY + rowHeight / 2 + 5);

                // Draw value (right column)
                g2.drawRect(x + colWidth, rowY, colWidth, rowHeight); // Border for the right cell
                g2.drawString(tableData[i][1], x + colWidth + 5, rowY + rowHeight / 2 + 5);
            }
        }
    }

    public void drawBackGround(Graphics2D g2) {
        g2.setColor(new Color(187,187,187));
        g2.fillRect(startX,startY,width,height);
    }

    public void drawWhiteArea(Graphics2D g2) {
        int whiteWidth = width - 10;
        int whiteHeight = 220;
        g2.setColor(Color.WHITE);
        g2.fillRect(startWhiteX, startWhiteY, whiteWidth, whiteHeight);
        g2.setColor(new Color(148,148,148));
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(startWhiteX, startWhiteY, whiteWidth, whiteHeight);

        if(ep.mapArea.getSelectedTile() != -1){
            Tile currentTile = ep.tileM.tile[ep.mapArea.selectedTile];
            g2.drawImage(currentTile.image, startWhiteX + 10, startWhiteY + 10, 200, 200, null);
        }
    }

    public void drawWhiteCaption(Graphics2D g2) {
        String text = "";
        int x = startX + 10;
        int y = startY + 15;
        g2.setColor(Color.BLACK);
        if(ep.panelMode == ep.tileMode){
            text = "Tile information:";
        } else if(ep.panelMode == ep.objectMode){
            text = "Object information:";
        } else if(ep.panelMode == ep.decorationMode){
            text = "Decoration information:";
        } else if(ep.panelMode == ep.fightersMode){
            text = "Fighter information:";
        }
        g2.drawString(text,x,y);
    }

    public void drawHoveredCoordinates(Graphics2D g2){
        g2.setColor(Color.BLACK);
        if(hoveredX != -1) {
            g2.drawString("X:" + hoveredCoordX, startX + 10, endY - 8);
            g2.drawString("Y:" + hoveredCoordY, startX + 70, endY - 8);
            g2.drawString("Col: " + hoveredX, startX + 140, endY - 8);
            g2.drawString("Row: " + hoveredY, startX + 180,endY - 8);
        }
    }
}
