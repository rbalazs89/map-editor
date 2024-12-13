package InterfaceBlocks;

import Elements.Decor;
import Main.EditorPanel;
import Tool.Methods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MapArea extends InterfaceBlock {
    public float scale = 1f;
    public int tileOffsetX = 0; // Offset for X-axis
    public int tileOffsetY = 0; // Offset for Y-axis
    public int[][][] tileCoordinates;
    public boolean showWallTiles = false;
    public int selectedTile = -1;
    public int hoveredRow = -1;
    public int hoveredCol = -1;
    public int hoveredTile = -1;
    public boolean drawGrid = true;
    public int mouseWorldX = 0;
    public int mouseWorldY = 0;

    public int updateDecorArrayCounter = 0;
    public MapArea(EditorPanel ep) {
        super(ep);
        ep.uiElements[0] = this;
        startX = 300;
        startY = 100;
        width = 750;
        height = 768 - 100;
        endX = startX + width;
        endY = startY + height;
        tileCoordinates = new int[ep.currentMapMaxCol][ep.currentMapMaxRow][2];
        calculateTileCoordinates();
    }

    public void update(){
        getMouseAndTileCoordinates();
        if(ep.panelMode == EditorPanel.decorationMode){
            placeDecorationOnTheMap();
            updateDecorArray();
            cancelSelectedDecor();
            deleteSelectedDecor();
        }
    }


    public void draw(Graphics2D g2){
        drawTiles(g2);
        drawDecorations(g2);
        if(drawGrid){
            drawGrid(g2);
        }
        if(ep.panelMode == EditorPanel.tileMode){
            drawHoveredTile(g2);
        } else if (ep.panelMode == EditorPanel.decorationMode){
            showHoveredDecor(g2);
            drawSelectedDecorationOnMap(g2);
        }
        drawBackGround(g2);
    }



    public void updateDecorArray(){
        updateDecorArrayCounter ++;
        if(updateDecorArrayCounter > 60){
            updateDecorArrayCounter = 0;
            Methods.sortDecors(ep.decorManager.decorsOnTheMap);
        }
    }
    private void selectDecorFromShowed(Decor decor){
        if(ep.mouseH.rightMousePressed){
            for (int i = 0; i < ep.decorManager.decorsOnTheMap.length; i++) {
                if(ep.decorManager.decorsOnTheMap[i] == null){
                    break;
                }
                ep.decorManager.decorsOnTheMap[i].selected = false;
            }
            ep.mouseH.rightMousePressed = false;
            ep.isSelectedFromMap = true;
            ep.selectedDecor = decor;
            decor.selected = true;
        }
    }

    public void deleteSelectedDecor(){
        if(ep.keyH.deletePressed && ep.selectedDecor != null && ep.isSelectedFromMap){
            int startingIndex = ep.selectedDecor.arrayPosition;
            ep.decorManager.decorsOnTheMap[startingIndex] = null;
            ep.isSelectedFromMap = false;
            ep.selectedDecor = null;
            ep.keyH.deletePressed = false;

            for (int i = startingIndex; i < ep.decorManager.decorsOnTheMap.length; i++) {
                if(ep.decorManager.decorsOnTheMap[i + 1] == null) {
                    break;
                }
                ep.decorManager.decorsOnTheMap[i] = ep.decorManager.decorsOnTheMap[i + 1];
                ep.decorManager.decorsOnTheMap[i].arrayPosition = i;
            }
        }
    }

    private void showHoveredDecor(Graphics2D g2) {
        int x = ep.mouseH.mouseXMoved;
        int y = ep.mouseH.mouseYMoved;

        int scaledTileSize = (int)(scale * EditorPanel.tileSize);
        int gridStartX = startX + tileOffsetX;
        int gridStartY = startY + tileOffsetY;
        int gridWidth = ep.currentMapMaxCol * scaledTileSize;
        int gridHeight = ep.currentMapMaxRow * scaledTileSize;

        ArrayList<Decor> hoveredDecors = new ArrayList<>();
        if(x > gridStartX && x < gridStartX + gridWidth && y > gridStartY && y < gridStartY + gridHeight){
            for (int i = 0; i < ep.decorManager.decorsOnTheMap.length; i++) {
                if(ep.decorManager.decorsOnTheMap[i] == null){
                    break;
                }

                Decor currentDecor = ep.decorManager.decorsOnTheMap[i];
                int decorX = startX + tileOffsetX + currentDecor.worldX / EditorPanel.tileSize * scaledTileSize + (int)((currentDecor.worldX % EditorPanel.tileSize) * scale);
                int decorY = startY + tileOffsetY + currentDecor.worldY / EditorPanel.tileSize * scaledTileSize + (int)((currentDecor.worldY % EditorPanel.tileSize) * scale);
                int decorXEnd = decorX + (int)(currentDecor.imageWidth * scale);
                int decorYEnd = decorY + (int)(currentDecor.imageHeight *scale);

                if( x > decorX && x < decorXEnd && y > decorY && y < decorYEnd){
                    hoveredDecors.add(currentDecor);
                }
            }
        }

        if (!hoveredDecors.isEmpty()) {
            // Find the closest decor
            Decor closestDecor = null;
            double minDistance = Double.MAX_VALUE;

            for (Decor decor : hoveredDecors) {
                // Calculate center of the decor
                int decorCenterX = (int) (startX + tileOffsetX + (decor.worldX + decor.imageWidth / 2.0) * scale);
                int decorCenterY = (int) (startY + tileOffsetY + (decor.worldY + decor.imageHeight / 2.0) * scale);

                // Calculate distance to the mouse
                double distance = Math.hypot(decorCenterX - x, decorCenterY - y);

                // Update closest decor if this one is nearer
                if (distance < minDistance) {
                    minDistance = distance;
                    closestDecor = decor;
                }
            }

            // Highlight the closest decor
            if (closestDecor != null) {
                int decorX = startX + tileOffsetX + closestDecor.worldX / EditorPanel.tileSize * scaledTileSize +
                        (int) ((closestDecor.worldX % EditorPanel.tileSize) * scale);
                int decorY = startY + tileOffsetY + closestDecor.worldY / EditorPanel.tileSize * scaledTileSize +
                        (int) ((closestDecor.worldY % EditorPanel.tileSize) * scale);
                int decorWidth = (int) (closestDecor.imageWidth * scale);
                int decorHeight = (int) (closestDecor.imageHeight * scale);

                // Draw a semi-transparent yellow fill
                Color fillColor = new Color(255, 223, 88, 70); // Semi-transparent yellow
                g2.setColor(fillColor);
                g2.fillRect(decorX, decorY, decorWidth, decorHeight);

                // Draw the yellow outline
                Color outlineColor = new Color(255, 223, 88); // Softer yellow
                g2.setColor(outlineColor);
                g2.setStroke(new BasicStroke(2)); // Set outline thickness
                g2.drawRect(decorX, decorY, decorWidth, decorHeight);

                selectDecorFromShowed(closestDecor);
            }
        }
        hoveredDecors.clear();
    }

    public void cancelSelectedDecor(){
        if(ep.mouseH.rightMousePressed){
            int gridStartX = startX + tileOffsetX;
            int gridStartY = startY + tileOffsetY;
            int gridWidth = (int)(ep.currentMapMaxCol * scale * EditorPanel.tileSize);
            int gridHeight = (int)(ep.currentMapMaxRow * scale * EditorPanel.tileSize);
            int x = ep.mouseH.mouseXMoved;
            int y = ep.mouseH.mouseYMoved;

            if(!(x > gridStartX && x < gridStartX + gridWidth && y > gridStartY && y < gridStartY + gridHeight)){
                if(ep.selectedDecor != null){
                    ep.selectedDecor.selected = false;
                    ep.isSelectedFromMap = false;
                }
                ep.selectedDecor = null;
            }
        }
    }

    public void resetPosition(){
        scale = 1f;
        tileOffsetX = 0;
        tileOffsetY = 0;
        calculateTileCoordinates();
    }

    public void dragMap(int deltaX, int deltaY) {
        tileOffsetX += deltaX;
        tileOffsetY += deltaY;
        calculateTileCoordinates();
    }

    public void drawDecorations(Graphics2D g2) {
        for (Decor decor : ep.decorManager.decorsOnTheMap) {
            if (decor == null) {
                break;
            }
            if (decor.image == null) {
                continue; // Skip invalid decorations
            }
            Decor currentDecor = decor;
            int scaledTileSize = (int)(ep.tileSize * scale);
            int x = startX + tileOffsetX + currentDecor.worldX / EditorPanel.tileSize * scaledTileSize + (int)((currentDecor.worldX % EditorPanel.tileSize) * scale);
            int y = startY + tileOffsetY + currentDecor.worldY / EditorPanel.tileSize * scaledTileSize + (int)((currentDecor.worldY % EditorPanel.tileSize) * scale);

            if(currentDecor.selected){
                BufferedImage imageToDraw =  addYellowTint(currentDecor.image);
                g2.drawImage(imageToDraw,x, y, (int)(currentDecor.imageWidth * scale),
                        (int)(currentDecor.imageHeight * scale), null);
            } else {
                g2.drawImage(currentDecor.image,x, y, (int)(currentDecor.imageWidth * scale),
                        (int)(currentDecor.imageHeight * scale), null);
            }
        }
    }
    public void drawTiles(Graphics2D g2) {
        int scaledTileSize = (int) (ep.tileSize * scale);

        // Determine visible bounds based on the coordinates
        int startCol = Math.max(0, (-tileOffsetX / scaledTileSize));
        int startRow = Math.max(0, (-tileOffsetY / scaledTileSize));
        int endCol = Math.min(ep.currentMapMaxCol, startCol + (width / scaledTileSize) + 2);
        int endRow = Math.min(ep.currentMapMaxRow, startRow + (height / scaledTileSize) + 2);

        // Loop through the visible tiles
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                BufferedImage image = null;

                boolean collision = false;
                if (col < ep.tileM.mapTileNum.length && row < ep.tileM.mapTileNum[0].length) {
                    int tileNum = ep.tileM.mapTileNum[col][row];
                    image = ep.tileM.tile[tileNum].image;
                    collision = ep.tileM.tile[tileNum].collision;
                }

                // Retrieve precomputed coordinates for the tile
                int drawX = tileCoordinates[col][row][0];
                int drawY = tileCoordinates[col][row][1];

                if (image != null) {
                    g2.drawImage(image, drawX, drawY, scaledTileSize, scaledTileSize, null);
                    if (collision && showWallTiles) {
                        // Set the color for the collision rectangle (semi-transparent)
                        Color transparentBlueBlack = new Color(139, 0, 0, 128); // Semi-transparent blue/blackish
                        g2.setColor(transparentBlueBlack);
                        g2.fillRect(drawX, drawY, scaledTileSize, scaledTileSize); // Fill the rectangle over the tile
                    }
                }
            }
        }
    }

    public void drawGrid(Graphics2D g2) {
        int scaledTileSize = (int) (ep.tileSize * scale);

        // Determine visible bounds based on current map size, ensuring that values don't exceed maxCol/maxRow
        int startCol = Math.max(0, (-tileOffsetX / scaledTileSize));
        int startRow = Math.max(0, (-tileOffsetY / scaledTileSize));

        // Make sure startCol and startRow don't exceed maxCol/maxRow
        startCol = Math.min(startCol, ep.currentMapMaxCol - 1);
        startRow = Math.min(startRow, ep.currentMapMaxRow - 1);

        // Calculate endCol and endRow, ensuring they stay within bounds
        int endCol = Math.min(ep.currentMapMaxCol, startCol + (width / scaledTileSize) + 2);
        int endRow = Math.min(ep.currentMapMaxRow, startRow + (height / scaledTileSize) + 2);

        // Adjust the dash lengths to scale proportionally
        float dashLength = Math.max(1f, 5f * scale);
        Stroke greyDashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{dashLength}, 0);
        Stroke blackDashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1f * scale}, 0);

        // Draw vertical grid lines
        for (int col = startCol; col <= endCol; col++) {
            int x = startX + col * scaledTileSize + tileOffsetX;

            // Skip drawing if the line is outside the map area
            if (x < startX || x > endX) continue;

            if (col % 10 == 0) {
                // Black dashed line for every 10th line
                g2.setColor(Color.BLACK);
                g2.setStroke(blackDashed);
            } else {
                // Regular grey dashed line
                g2.setColor(Color.GRAY);
                g2.setStroke(greyDashed);
            }

            g2.drawLine(x, startY, x, endY);
        }

        // Draw horizontal grid lines
        for (int row = startRow; row <= endRow; row++) {
            int y;

            // Ensure row index is within bounds
            if (row >= 0 && row < tileCoordinates[0].length) {
                y = tileCoordinates[startCol][row][1];
            } else {
                continue; // Skip if out of bounds
            }

            // Skip drawing if the line is outside the map area
            if (y < startY || y > endY) continue;

            if (row % 10 == 0) {
                // Black dashed line for every 10th line
                g2.setColor(Color.BLACK);
                g2.setStroke(blackDashed);
            } else {
                // Regular grey dashed line
                g2.setColor(Color.GRAY);
                g2.setStroke(greyDashed);
            }

            g2.drawLine(startX, y, endX, y);
        }

        // Draw a continuous border around the tiles area
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2)); // 1-pixel border
        int borderX = startX + tileOffsetX;
        int borderY = startY + tileOffsetY;
        int borderWidth = ep.currentMapMaxCol * scaledTileSize;
        int borderHeight = ep.currentMapMaxRow * scaledTileSize;

        g2.drawRect(borderX, borderY, borderWidth, borderHeight);
    }

    public void drawBackGround(Graphics2D g2) {
         // Coordinates and dimensions of the solid grid area
        int scaledTileSize = (int) (ep.tileSize * scale);
        int gridStartX = startX + tileOffsetX;
        int gridStartY = startY + tileOffsetY;
        int gridWidth = ep.currentMapMaxCol * scaledTileSize;
        int gridHeight = ep.currentMapMaxRow * scaledTileSize;

        // Draw rectangles to mask the areas outside the grid
        g2.setColor(new Color(137, 137, 137)); // Match the background gradient base color

        // Top area
        if (gridStartY > startY) {
            g2.fillRect(startX, startY, width, gridStartY - startY);
        }
        // Bottom area
        if (gridStartY + gridHeight < endY) {
            g2.fillRect(startX, gridStartY + gridHeight, width, endY - (gridStartY + gridHeight));
        }
        // Left area
        if (gridStartX > startX) {
            g2.fillRect(startX, gridStartY, gridStartX - startX, gridHeight);
        }
        // Right area
        if (gridStartX + gridWidth < endX) {
            g2.fillRect(gridStartX + gridWidth, gridStartY, endX - (gridStartX + gridWidth), gridHeight);
        }

        // draw bracket
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(148,148,148));
        g2.drawRect(startX, startY, width - 1, height - 1);
    }

    public void updateScale(float newScale) {
        if (newScale < 0.1f) {
            newScale = 0.1f;
        } else if (newScale > 2.0f) {
            newScale = 2.0f;
        }

        float scaleDelta = newScale - scale;
        scale = newScale;

        int mouseXRelative = ep.mouseH.mouseXMoved - startX - tileOffsetX;
        int mouseYRelative = ep.mouseH.mouseYMoved - startY - tileOffsetY;

        tileOffsetX -= (scaleDelta * mouseXRelative);
        tileOffsetY -= (scaleDelta * mouseYRelative);
        calculateTileCoordinates();
    }

    public void calculateTileCoordinates() {
        int scaledTileSize = (int) (ep.tileSize * scale); // Adjusted tile size based on zoom level
        for (int row = 0; row < ep.currentMapMaxRow; row++) {
            for (int col = 0; col < ep.currentMapMaxCol; col++) {
                int tileX = startX + col * scaledTileSize + tileOffsetX;
                int tileY = startY + row * scaledTileSize + tileOffsetY;
                tileCoordinates[col][row][0] = tileX; // Store X coordinate
                tileCoordinates[col][row][1] = tileY; // Store Y coordinate
            }
        }
    }

    public void updateTileCoordinatesArray(){
        int scaledTileSize = (int) (ep.tileSize * scale); // Adjusted tile size based on zoom level

        // Resize tileCoordinates to match the new map dimensions
        tileCoordinates = new int[ep.currentMapMaxCol][ep.currentMapMaxRow][2];

        for (int row = 0; row < ep.currentMapMaxRow; row++) {
            for (int col = 0; col < ep.currentMapMaxCol; col++) {
                int tileX = startX + col * scaledTileSize + tileOffsetX;
                int tileY = startY + row * scaledTileSize + tileOffsetY;
                tileCoordinates[col][row][0] = tileX; // Store X coordinate
                tileCoordinates[col][row][1] = tileY; // Store Y coordinate
            }
        }
    }

    private void placeDecorationOnTheMap() {
        if(ep.selectedDecor != null){
            if (ep.mouseH.mouseXMoved > startX && ep.mouseH.mouseXMoved < endX && ep.mouseH.mouseYMoved > startY && ep.mouseH.mouseYMoved < endY && ep.mouseH.leftMousePressed) {
                ep.mouseH.leftMousePressed = false;
                Decor decor = new Decor();
                decor.image = ep.selectedDecor.image;
                decor.imageWidth = ep.selectedDecor.imageWidth;
                decor.imageHeight = ep.selectedDecor.imageHeight;
                decor.worldX = mouseWorldX - (int)(ep.selectedDecor.imageWidth * scale / 2);
                decor.worldY = mouseWorldY - (int)(ep.selectedDecor.imageHeight * scale / 2);
                //TODO
                decor.worldX = mouseWorldX;
                decor.worldY = mouseWorldY;
                decor.name = ep.selectedDecor.name;
                if(decor.worldX < 0){
                    decor.worldX = 0;
                }
                if(decor.worldY < 0){
                    decor.worldY = 0;
                }
                if(decor.worldX + decor.imageWidth > EditorPanel.tileSize * ep.currentMapMaxCol){
                    decor.worldX = EditorPanel.tileSize * ep.currentMapMaxCol - decor.imageWidth;
                }
                if(decor.worldY + decor.imageHeight > EditorPanel.tileSize * ep.currentMapMaxRow){
                    decor.worldY = EditorPanel.tileSize * ep.currentMapMaxRow - decor.imageHeight;
                }

                for (int i = 0; i < ep.decorManager.decorsOnTheMap.length; i++) {
                    if(ep.decorManager.decorsOnTheMap[i] == null){
                        ep.decorManager.decorsOnTheMap[i] = decor;
                        break;
                    }
                }
            }
        }
    }

    public int getSelectedTile(){
        return selectedTile;
    }

    //this method in addition to get the current tile, transfers the coordinates to SelectedArea class
    public int[] getCurrentTile() {
        if (ep.mouseH.mouseXMoved > startX && ep.mouseH.mouseXMoved < endX && ep.mouseH.mouseYMoved > startY && ep.mouseH.mouseYMoved < endY) {
            int scaledTileSize = (int) (EditorPanel.tileSize * scale);
            int x = ep.mouseH.mouseXMoved - startX - tileOffsetX;
            int y = ep.mouseH.mouseYMoved - startY - tileOffsetY;

            // Calculate tile indices
            int tileX = x / scaledTileSize;
            int tileY = y / scaledTileSize;

            hoveredCol = tileX;
            hoveredRow = tileY;

            // Ensure the tile coordinates are within the bounds of the map
            if (tileX >= 0 && tileX < ep.currentMapMaxCol && tileY >= 0 && tileY < ep.currentMapMaxRow) {
                hoveredTile = ep.tileM.mapTileNum[tileX][tileY]; // Assuming this provides the tile index
                return new int[] { tileX, tileY }; // Return the corresponding tile
            }
        } else {
            hoveredCol = -1;
            hoveredRow = -1;
            hoveredTile = -1;
        }
        return null; // Return null if the mouse is outside the bounds of the map
    }
    public void drawHoveredTile(Graphics2D g2) {
        // Get the current tile coordinates
        int[] currentTile = getCurrentTile();

        if (currentTile != null) {
            int tileX = currentTile[0];
            int tileY = currentTile[1];

            // Calculate the position of the hovered tile
            int scaledTileSize = (int) (ep.tileSize * scale);
            int drawX = startX + tileX * scaledTileSize + tileOffsetX;
            int drawY = startY + tileY * scaledTileSize + tileOffsetY;

            // Set the color to a deeper blue with some transparency
            g2.setColor(new Color(135, 206, 250, 150)); // Slightly more blue and transparency
            g2.fillRect(drawX, drawY, scaledTileSize, scaledTileSize); // Fill the rectangle

            // Draw a blue border around the filled rectangle
            g2.setColor(new Color(70, 130, 180)); // Darker blue for the border
            g2.setStroke(new BasicStroke(2)); // Set a thicker stroke for the border
            g2.drawRect(drawX, drawY, scaledTileSize, scaledTileSize); // Draw the border around the filled area
        }
    }

    private void drawSelectedDecorationOnMap(Graphics2D g2) {
        if(ep.selectedDecor == null) {
            return;
        }
        Decor currentDecor = ep.selectedDecor;
        if(ep.mouseH.mouseXMoved > startX
                && ep.mouseH.mouseXMoved < endX
                && ep.mouseH.mouseYMoved > startY
                && ep.mouseH.mouseYMoved < endY){

            BufferedImage imageToDraw = ep.methods.makeTransparent(currentDecor.image, 0.38f);
            int halfWidth = (int)(currentDecor.imageWidth * scale / 2);
            int halfHeight = (int)(currentDecor.imageHeight * scale / 2);
            g2.drawImage(imageToDraw, ep.mouseH.mouseXMoved, ep.mouseH.mouseYMoved, 2 * halfWidth, 2 * halfHeight, null);
            //g2.drawImage(imageToDraw, ep.mouseH.mouseXMoved - halfWidth, ep.mouseH.mouseYMoved - halfHeight, 2 * halfWidth, 2 * halfHeight, null);
        }
    }

    private void getMouseAndTileCoordinates() {
        if (ep.mouseH.mouseXMoved > startX && ep.mouseH.mouseXMoved < endX && ep.mouseH.mouseYMoved > startY && ep.mouseH.mouseYMoved < endY) {
            int scaledTileSize = (int) (ep.tileSize * scale);
            int x = ep.mouseH.mouseXMoved - startX - tileOffsetX;
            int y = ep.mouseH.mouseYMoved - startY - tileOffsetY;

            // Calculate tile indices
            int tileX = x / scaledTileSize;
            int tileY = y / scaledTileSize;

            if (ep.selectedArea != null) {
                // Assign exact coordinates within the current tile
                ep.selectedArea.hoveredX = tileX;
                ep.selectedArea.hoveredY = tileY;
                ep.selectedArea.hoveredCoordX = (int) ((x % scaledTileSize) / (double) scaledTileSize * 64) + 64 * tileX;
                ep.selectedArea.hoveredCoordY = (int) ((y % scaledTileSize) / (double) scaledTileSize * 64) + 64 * tileY;
                mouseWorldX = ep.selectedArea.hoveredCoordX;
                mouseWorldY = ep.selectedArea.hoveredCoordY;
                hoveredCol = tileX;
                hoveredRow = tileY;
            }

        } else {
            ep.selectedArea.hoveredX = -1;
            ep.selectedArea.hoveredY = -1;
            ep.selectedArea.hoveredCoordX = -1;
            ep.selectedArea.hoveredCoordY = -1;
        }
    }

    public BufferedImage addYellowTint(BufferedImage sourceImage) {
        // Create a new image with the same dimensions as the source image
        BufferedImage tintedImage = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TRANSLUCENT
        );

        // Get the graphics context of the new image
        Graphics2D g2 = tintedImage.createGraphics();

        // Draw the original image
        g2.drawImage(sourceImage, 0, 0, null);

        // Apply a yellow tint as a semi-transparent overlay
        g2.setComposite(AlphaComposite.SrcAtop); // Blend mode
        g2.setColor(new Color(255, 255, 0, 128)); // Semi-transparent yellow
        g2.fillRect(0, 0, sourceImage.getWidth(), sourceImage.getHeight());

        g2.dispose(); // Clean up resources

        return tintedImage;
    }
}