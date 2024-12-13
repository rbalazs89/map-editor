package Main;

import Tool.MyButton;

import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

    EditorPanel ep;
    public int mouseXMoved, mouseYMoved, mouseXClicked, mouseYClicked, lastMouseX, lastMouseY;
    public boolean middleMousePressed, leftMousePressed, rightMousePressed;

    public MouseHandler(EditorPanel ep){
        this.ep = ep;
        ep.addMouseMotionListener(this);
        ep.addMouseListener(this);
        ep.addMouseWheelListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseXClicked = e.getX();
        mouseYClicked = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            tileButtonsClicked();
            exportButtonClicked();
            newFileButtonClicked();
            loadButtonClicked();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getX() + ", " + e.getY());
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = true;
            testMethod();

            overwriteTile();
            exportButtonPressed();
            newFileButtonPressed();
            loadButtonPressed();

            if(ep.panelMode == ep.tileMode){
                tileButtonsPressed();
            }
        }

        if (e.getButton() == MouseEvent.BUTTON2) { // Middle mouse button
            middleMousePressed = true;
            dragMapWithMouse(e);
        }

        if(e.getButton() == MouseEvent.BUTTON3) {
            rightMousePressed = true;
            selectTile(e);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleMousePressed = false;
        } if (e.getButton() == MouseEvent.BUTTON1){
            leftMousePressed = false;
        } if (e.getButton() == MouseEvent.BUTTON3){
            rightMousePressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseXMoved = e.getX();
        mouseYMoved = e.getY();
        dragMapArea(e);
        if (leftMousePressed) {
            overwriteTile();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseXMoved = e.getX();
        mouseYMoved = e.getY();
        tileButtonsHovered();
        newFileButtonHovered();
        exportButtonHovered();
        loadButtonHovered();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseXMoved = e.getX();
        mouseYMoved = e.getY();
        changeZoomMapArea(e);
    }

    public void changeZoomMapArea(MouseWheelEvent e) {
        if (mouseAreaAbs(ep.mapArea.startX, ep.mapArea.endX, ep.mapArea.startY, ep.mapArea.endY) && ep.keyH.ctrlPressed) {
            int rotation = e.getWheelRotation(); // Negative for zooming in, positive for zooming out
            float zoomDelta = rotation > 0 ? -0.06f : 0.06f;
            float newScale = ep.mapArea.scale + zoomDelta;
            ep.mapArea.updateScale(newScale);
        }
    }

    public void dragMapArea(MouseEvent e){
        if (middleMousePressed) {
            // Calculate how far the mouse has moved
            int deltaX = e.getX() - lastMouseX;
            int deltaY = e.getY() - lastMouseY;

            // Delegate the offset adjustment to dragMap
            ep.mapArea.dragMap(deltaX, deltaY);

            // Store the new mouse position for the next drag event
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }
    private void overwriteTile() {
        if(ep.panelMode == 1 && ep.mapArea.selectedTile != -1){
            if(ep.mapArea.hoveredCol >= 0 && ep.mapArea.hoveredCol < ep.currentMapMaxCol &&
                    ep.mapArea.hoveredRow >= 0 && ep.mapArea.hoveredRow < ep.currentMapMaxRow) {
                    ep.tileM.mapTileNum[ep.mapArea.hoveredCol][ep.mapArea.hoveredRow] = ep.mapArea.selectedTile;
            }
        }
    }

    private void tileButtonsHovered() {
        for (MyButton button : ep.buttonsArea.tileRelatedButtons) {
            if (mouseXMoved >= button.posX - 5 && mouseXMoved <= button.posX + ep.buttonsArea.buttonWidth  - 15 &&
                    mouseYMoved >= button.posY - 5 && mouseYMoved <= button.posY + ep.buttonsArea.buttonWidth - 15) {
                button.isHovered = true;  // Set the button's hover state to true
            } else {
                button.isHovered = false; // Set the button's hover state to false
            }
        }
    }

    private void tileButtonsPressed() {
        for (MyButton button : ep.buttonsArea.tileRelatedButtons) {
            // Check if the mouse is within the bounds of the button
            if (mouseXMoved >= button.posX - 5 && mouseXMoved <= button.posX + ep.buttonsArea.buttonWidth  - 15 &&
                    mouseYMoved >= button.posY - 5 && mouseYMoved <= button.posY + ep.buttonsArea.buttonWidth - 15) {
                button.isPressed = true;
            }
        }
    }

    private void tileButtonsClicked() {
        for (MyButton button : ep.buttonsArea.tileRelatedButtons) {
            // Check if the mouse is within the bounds of the button
            if (mouseXMoved >= button.posX - 5 && mouseXMoved <= button.posX + ep.buttonsArea.buttonWidth  - 15 &&
                    mouseYMoved >= button.posY - 5 && mouseYMoved <= button.posY + ep.buttonsArea.buttonWidth - 15) {
                button.isClicked = true;
            }
        }
    }

    private void newFileButtonClicked(){
        if(ep.mouseH.mouseAreaAbs(ep.buttonsArea.newFileButton.posX - 5, ep.buttonsArea.newFileButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.newFileButton.posY - 5,
                ep.buttonsArea.newFileButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.newFileButton.isClicked = true;

        }
    }

    private void newFileButtonPressed(){
        if(ep.mouseH.mouseAreaAbs(ep.buttonsArea.newFileButton.posX - 5, ep.buttonsArea.newFileButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.newFileButton.posY - 5, ep.buttonsArea.newFileButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.newFileButton.isPressed = true;
        }
    }

    private void selectTile(MouseEvent e){
        if(ep.panelMode == 1){
            if (e.getX() > ep.mapArea.startX && e.getX() < ep.mapArea.endX && e.getY() > ep.mapArea.startY && e.getY() < ep.mapArea.endY){
                int scaledTileSize = (int) (ep.tileSize * ep.mapArea.scale);

                // Convert mouse coordinates to relative tile coordinates (taking into account zoom and offset)
                int tileX = (ep.mouseH.mouseXMoved - ep.mapArea.startX - ep.mapArea.tileOffsetX) / scaledTileSize;
                int tileY = (ep.mouseH.mouseYMoved - ep.mapArea.startY - ep.mapArea.tileOffsetY) / scaledTileSize;

                ep.selectedArea.selectedTileCol = tileX;
                ep.selectedArea.selectedTileRow = tileY;

                // Ensure the tile coordinates are within the bounds of the map
                if (tileX >= 0 && tileX < ep.currentMapMaxCol && tileY >= 0 && tileY < ep.currentMapMaxRow) {
                    // Return the tile at the calculated position
                    int tileNum = ep.tileM.mapTileNum[tileX][tileY]; // Assuming this provides the tile index
                    ep.mapArea.selectedTile = tileNum;
                }
            }
        } else {
            ep.mapArea.selectedTile = -1;
        }
    }

    private void dragMapWithMouse(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }
    private void exportButtonClicked() {
        if(mouseAreaAbs(ep.buttonsArea.exportButton.posX - 5, ep.buttonsArea.exportButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.exportButton.posY - 5, ep.buttonsArea.exportButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.exportButton.isClicked = true;
        }
    }

    private void exportButtonPressed(){
        if(mouseAreaAbs(ep.buttonsArea.exportButton.posX - 5, ep.buttonsArea.exportButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.exportButton.posY - 5, ep.buttonsArea.exportButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.exportButton.isPressed = true;
        }
    }

    private void exportButtonHovered(){
        if(mouseAreaAbs(ep.buttonsArea.exportButton.posX - 5, ep.buttonsArea.exportButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.exportButton.posY - 5, ep.buttonsArea.exportButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.exportButton.isHovered = true;
        } else {
            ep.buttonsArea.exportButton.isHovered = false;
        }
    }
    private void loadButtonClicked() {
        if(mouseAreaAbs(ep.buttonsArea.loadFileButton.posX - 5, ep.buttonsArea.loadFileButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.loadFileButton.posY - 5, ep.buttonsArea.loadFileButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.loadFileButton.isClicked = true;
        }
    }

    private void loadButtonPressed(){
        if(mouseAreaAbs(ep.buttonsArea.loadFileButton.posX - 5, ep.buttonsArea.loadFileButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.loadFileButton.posY - 5, ep.buttonsArea.loadFileButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.loadFileButton.isPressed = true;
        }
    }

    private void loadButtonHovered(){
        if(mouseAreaAbs(ep.buttonsArea.loadFileButton.posX - 5, ep.buttonsArea.loadFileButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.loadFileButton.posY - 5, ep.buttonsArea.loadFileButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.loadFileButton.isHovered = true;
        } else {
            ep.buttonsArea.loadFileButton.isHovered = false;
        }
    }

    private void newFileButtonHovered(){
        if(mouseAreaAbs(ep.buttonsArea.newFileButton.posX - 5, ep.buttonsArea.newFileButton.posX + ep.buttonsArea.buttonWidth - 15,
                ep.buttonsArea.newFileButton.posY - 5, ep.buttonsArea.newFileButton.posY + ep.buttonsArea.buttonWidth - 15)){
            ep.buttonsArea.newFileButton.isHovered = true;
        } else {
            ep.buttonsArea.newFileButton.isHovered = false;
        }
    }

    public boolean mouseAreaAbs(int x, int x2, int y, int y2){
        return mouseXMoved > x && mouseXMoved < x2 && mouseYMoved > y && mouseYMoved < y2;
    }

    public void testMethod(){
        int gridStartX = ep.mapArea.startX + ep.mapArea.tileOffsetX;
        int gridStartY = ep.mapArea.startY + ep.mapArea.tileOffsetY;
        int scaledTileSize = (int)(ep.tileSize * ep.mapArea.scale);

        int gridWidth = ep.currentMapMaxCol * scaledTileSize;
        int gridHeight = ep.currentMapMaxRow * scaledTileSize;

    }
}
