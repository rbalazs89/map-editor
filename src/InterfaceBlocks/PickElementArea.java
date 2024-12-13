package InterfaceBlocks;

import Main.EditorPanel;
import Tool.MyButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PickElementArea extends InterfaceBlock {
    //listed elements same as
    public int pickStartX, pickStartY, pickWidth, pickHeight;
    public int currentPage = 1;
    public int selectedElementNumber = -1;
    public int buttonStartX;
    public int buttonStartY;
    public int buttonWidth = 50;
    public ArrayList<MyButton> pageButtons = new ArrayList<>();

    public PickElementArea(EditorPanel ep){
        super(ep);
        startX = 0;
        startY = ep.buttonsArea.endY;
        height = ep.screenHeight - startY;
        width = ep.mapArea.startX;
        endX = startX + width;
        endY = startY + height;

        pickStartX = startX + 5;
        pickWidth = width - 10;
        pickStartY = startY + 5;
        pickHeight = 528;

        buttonStartY = pickStartY + pickHeight + 25;
        buttonStartX = 10;

        createPageButtons();
    }

    public void draw(Graphics2D g2){
        drawBackground(g2);
        drawPickArea(g2);
        drawButtons(g2);

        if(ep.panelMode == EditorPanel.tileMode){
            drawTiles(g2);
        } else if(ep.panelMode == EditorPanel.decorationMode){
            drawDecoration(g2);
        }

        drawHoveredElement(g2);
    }

    public void update(){
        if(ep.panelMode == EditorPanel.tileMode) {
            selectTile();
        } else if(ep.panelMode == EditorPanel.decorationMode){
            selectDecoration();
        }
        pageButtonHoverAndClick();
    }

    private void drawDecoration(Graphics2D g2) {
        int tileWidth = 48;
        int tileHeight = 48;
        int tilesPerRow = 6;
        int tilesPerColumn = 11;
        int maxTilesPerPage = tilesPerRow * tilesPerColumn;

        // Calculate starting position
        int x = pickStartX;
        int y = pickStartY;

        // Start index of the tiles for the current page
        int startTileIndex = (currentPage - 1) * maxTilesPerPage;

        // Draw tiles in the grid
        for (int row = 0; row < tilesPerColumn; row++) {
            for (int col = 0; col < tilesPerRow; col++) {
                int tileIndex = startTileIndex + (row * tilesPerRow) + col;

                // Check if the tile index exceeds the available tiles
                if (tileIndex >= ep.decorManager.decors.length) {
                    return;
                }

                // Get the tile image if it's not null
                if (ep.decorManager.decors[tileIndex] != null && ep.decorManager.decors[tileIndex].image != null) {
                    BufferedImage tileImage = ep.decorManager.decors[tileIndex].image;

                    // Draw the tile image
                    g2.drawImage(tileImage, x, y, tileWidth, tileHeight, null);
                }

                // Draw the gridline for the tile
                g2.setColor(new Color(148, 148, 148));
                g2.drawRect(x, y, tileWidth, tileHeight);

                // Move to the next column
                x += tileWidth; // No +1 for spacing
            }

            // Move to the next row and reset column position
            x = pickStartX;
            y += tileHeight; // No +1 for spacing
        }
    }

    public void drawBackground(Graphics2D g2){
        g2.setColor(new Color(186,186,186));
        g2.fillRect(startX, startY, endX - 1, endY);
    }

    public void drawPickArea(Graphics2D g2){
        g2.setColor(Color.white);
        g2.fillRect(pickStartX, pickStartY, pickWidth - 1, pickHeight);

        g2.setColor(new Color(148, 148, 148));
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(pickStartX, pickStartY, pickWidth - 1, pickHeight);
    }

    public void drawTiles(Graphics2D g2) {
        // Define the tile grid parameters
        int tileWidth = 48;
        int tileHeight = 48;
        int tilesPerRow = 6;
        int tilesPerColumn = 11;
        int maxTilesPerPage = tilesPerRow * tilesPerColumn;

        // Calculate starting position
        int x = pickStartX;
        int y = pickStartY;

        // Start index of the tiles for the current page
        int startTileIndex = (currentPage - 1) * maxTilesPerPage;

        // Draw tiles in the grid
        for (int row = 0; row < tilesPerColumn; row++) {
            for (int col = 0; col < tilesPerRow; col++) {
                int tileIndex = startTileIndex + (row * tilesPerRow) + col;

                // Check if the tile index exceeds the available tiles
                if (tileIndex >= ep.tileM.tile.length) {
                    return;
                }

                // Get the tile image if it's not null
                if (ep.tileM.tile[tileIndex] != null && ep.tileM.tile[tileIndex].image != null) {
                    BufferedImage tileImage = ep.tileM.tile[tileIndex].image;

                    // Draw the tile image
                    g2.drawImage(tileImage, x, y, tileWidth, tileHeight, null);
                }

                // Draw the gridline for the tile
                g2.setColor(new Color(148, 148, 148));
                g2.drawRect(x, y, tileWidth, tileHeight);

                // Move to the next column
                x += tileWidth; // No +1 for spacing
            }

            // Move to the next row and reset column position
            x = pickStartX;
            y += tileHeight; // No +1 for spacing
        }
    }

    public void drawHoveredElement(Graphics2D g2) {
        // Ensure mouse coordinates are within the pick area
        if (ep.mouseH.mouseXMoved >= pickStartX && ep.mouseH.mouseXMoved < pickStartX + pickWidth &&
                ep.mouseH.mouseYMoved >= pickStartY && ep.mouseH.mouseYMoved < pickStartY + pickHeight) {

            // Tile dimensions
            int tileWidth = 48;
            int tileHeight = 48;

            // Calculate hovered column and row
            int hoveredCol = (ep.mouseH.mouseXMoved - pickStartX) / tileWidth;
            int hoveredRow = (ep.mouseH.mouseYMoved - pickStartY) / tileHeight;

            // Calculate the top-left corner of the hovered tile
            int x = pickStartX + (hoveredCol * tileWidth);
            int y = pickStartY + (hoveredRow * tileHeight);

            // Calculate selectedElementNumber
            int elementsPerRow = 6; // Number of tiles per row
            int elementsPerPage = elementsPerRow * 11; // Total tiles per page
            selectedElementNumber = ((currentPage - 1) * elementsPerPage) + (hoveredRow * elementsPerRow) + hoveredCol;

            // Draw the light blue rectangle
            g2.setColor(new Color(173, 216, 230, 128)); // Light blue with some transparency
            g2.fillRect(x, y, tileWidth, tileHeight);

            g2.setColor(new Color(30, 144, 255)); // Stronger blue for the border
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, tileWidth, tileHeight);
        } else {
            selectedElementNumber = -1;
        }
    }


    private void selectDecoration() {
        if(selectedElementNumber != -1) {
            if(ep.mouseH.leftMousePressed || ep.mouseH.rightMousePressed){
                if(ep.decorManager.decors[selectedElementNumber] != null){
                    if(ep.selectedDecor != null){
                        ep.selectedDecor.selected = false;
                    }
                    ep.mouseH.leftMousePressed = false;
                    ep.mouseH.rightMousePressed = false;
                    ep.selectedDecor = ep.decorManager.decors[selectedElementNumber];
                    ep.isSelectedFromMap = false;
                } else {
                    ep.selectedDecor = null;
                }
            }
        }
    }

    public void selectTile(){
        if(selectedElementNumber != -1) {
            if(ep.mouseH.leftMousePressed || ep.mouseH.rightMousePressed){
                if(ep.tileM.tile[selectedElementNumber] != null){
                    ep.mouseH.leftMousePressed = false;
                    ep.mouseH.rightMousePressed = false;
                    ep.mapArea.selectedTile = selectedElementNumber;
                    ep.mapArea.hoveredTile = -1;
                }
            }
        }
    }

    public void createPageButtons() {
        int x = buttonStartX;
        int y = buttonStartY;
        MyButton page1 = new MyButton();
        page1.posX = x;
        page1.posY = y;
        page1.isPressed = true;
        pageButtons.add(page1);

        x += buttonWidth;
        MyButton page2 = new MyButton();
        page2.posX = x;
        page2.posY = y;
        pageButtons.add(page2);

        x += buttonWidth;
        MyButton page3 = new MyButton();
        page3.posX = x;
        page3.posY = y;
        pageButtons.add(page3);

        x += buttonWidth;
        MyButton page4 = new MyButton();
        page4.posX = x;
        page4.posY = y;
        pageButtons.add(page4);

        x += buttonWidth;
        MyButton page5 = new MyButton();
        page5.posX = x;
        page5.posY = y;
        pageButtons.add(page5);

        x += buttonWidth;
        MyButton page6 = new MyButton();
        page6.posX = x;
        page6.posY = y;
        pageButtons.add(page6);
    }

    public void drawButtons(Graphics2D g2) {
        Font originalFont = g2.getFont();
        Font largerFont = originalFont.deriveFont(originalFont.getSize() * 3f);

        for (int i = 0; i < pageButtons.size(); i++) {
            pageButtons.get(i).draw(g2);
        }

        g2.setFont(largerFont);
        g2.setColor(Color.BLACK);
        for (int i = 0; i < pageButtons.size(); i++) {
            g2.drawString(i + 1 + "", pageButtons.get(i).posX + 5, pageButtons.get(i).posY + 27);
        }
        g2.setFont(originalFont);
    }

    public void pageButtonHoverAndClick() {
        int mouseX = ep.mouseH.mouseXMoved;
        int mouseY = ep.mouseH.mouseYMoved;

        for (int i = 0; i < pageButtons.size(); i++) {

            MyButton button = pageButtons.get(i);

            // Check if the mouse is hovering over this button
            if (mouseX >= button.posX - 5 && mouseX <= button.posX + buttonWidth  - 15 &&
                    mouseY >= button.posY - 5 && mouseY <= button.posY + buttonWidth - 15) {

                button.isHovered = true;

                // Check if the left mouse button is pressed
                if (ep.mouseH.leftMousePressed) {
                    currentPage = i + 1; // Pages start from 1

                    // Set this button as clicked
                    for (int j = 0; j < pageButtons.size(); j++) {
                        pageButtons.get(j).isPressed = (j == i);
                    }
                }
            } else {
                // Reset hover status if the mouse is not over this button
                button.isHovered = false;
            }
        }
    }
}
