package InterfaceBlocks;

import Elements.Decor;
import Main.EditorPanel;
import Tool.MyButton;
import Tool.StateSwitchButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ButtonsArea extends InterfaceBlock{
    public ArrayList<MyButton> tileRelatedButtons = new ArrayList<>();
    public ArrayList<StateSwitchButton> switchModeButtons = new ArrayList<>();
    public MyButton exportButton;
    public MyButton newFileButton;
    public MyButton loadFileButton;
    public int buttonsRowStartY = 60;
    public int buttonWidth = 50;
    private boolean buttonClicked = false;
    private boolean buttonPressed = false;
    private int counter = 0;
    private int pressedCounter = 0;
    public int chooseModeButtonEndY = 35;
    public int chooseModeButtonStartX = 5;
    public int chooseModeButtonWidth = 100;
    public int chooseButtonHeight = 30;
    public ButtonsArea(EditorPanel ep) {
        super(ep);
        ep.uiElements[0] = this;
        startX = 0;
        startY = 0;
        width = ep.screenWidth;
        height = 100;
        endX = startX + width;
        endY = startY + height;
        createButtons();
    }

    public void update(){
        if(ep.panelMode == EditorPanel.tileMode) {
            updateTileButtons();
        }
        updateExportButton();
        handleNewFileButton();
        handleLoadFileButton();

        handleSwitchButtons();
        removePressed();
    }

    public void removePressed(){
        if(buttonClicked){
            counter ++;
        }

        if(buttonPressed){
            pressedCounter++;
        }

        if(pressedCounter > 35){
            buttonPressed = false;
            pressedCounter = 0;
        }
    }

    public void draw(Graphics2D g2){
        drawBackground(g2);
        if(ep.panelMode == EditorPanel.tileMode){
            drawTileButtons(g2);
        } else if(ep.panelMode == EditorPanel.decorationMode){

        }
        newFileButton.draw(g2);
        exportButton.draw(g2);
        loadFileButton.draw(g2);
        drawSwitchModeButtons(g2);

    }


    public void drawSwitchModeButtons(Graphics2D g2){
        for (int i = 0; i < switchModeButtons.size(); i++) {
            switchModeButtons.get(i).draw(g2);
        }
    }

    public void exportObjects(){

    }

    public void exportFighters(){

    }

    public void exportDecoration() {
        matchImageReferenceForExport();

        // Explicitly declare the type of decorList
        ArrayList<Map<String, Object>> decorList = new ArrayList<>();

        for (int i = 0; i < ep.decorManager.decorsOnTheMap.length; i++) {
            Decor decor = ep.decorManager.decorsOnTheMap[i];
            if (decor == null) {
                break; // Stop if no more decor items are found
            }

            // Collect decor properties
            Map<String, Object> decorData = new HashMap<>();
            decorData.put("name", decor.name);
            decorData.put("worldX", decor.worldX);
            decorData.put("worldY", decor.worldY);
            decorList.add(decorData);
        }

        // Convert to JSON and write to file
        String filePath = "res/maps/decor" + ep.currentMap + ".json";
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(decorList, fileWriter);
            System.out.println("Decoration data exported successfully to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to export decoration data.");
        }
    }



    public void createButtons(){
        createTileButtons();
        createExportButton();
        createNewFileButton();
        createSwitchModeButtons();
        createLoadFileButton();
    }

    private void loadFileButtonAction() {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("res/maps")); // Set default directory

        // Filter for .txt files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });

        // Open the file chooser dialog
        int result = fileChooser.showOpenDialog(null); // Pass the parent component (null for center of the screen)
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            try {
                // Extract map number from the file name
                String fileName = selectedFile.getName(); // Example: "map1.txt"
                int mapNumber = Integer.parseInt(fileName.replaceAll("\\D", "")); // Remove non-numeric characters
                System.out.println("Map number extracted: " + mapNumber);

                // Use the map number to update your application state
                ep.currentMap = mapNumber;
                ep.tileM.getTileManagerData(mapNumber);
                ep.mapArea.updateTileCoordinatesArray();
                ep.mapArea.calculateTileCoordinates();

                System.out.println("Map loaded successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to process the selected file.");
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    public void drawBackground(Graphics2D g2){
        g2.setColor(new Color(187,187,187));
        g2.fillRect(startX, startY, width, height); // Filled background

        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(219, 219, 219));
        g2.drawLine(startX, endY - 2, endX, endY - 2);
        g2.setColor(new Color(165, 165, 165));
        g2.drawLine(startX, endY - 4, endX, endY - 4);


        GradientPaint gradient = new GradientPaint(
                startX, startY, new Color(154, 154, 154), // Start color (darker gray)
                startX, chooseModeButtonEndY, new Color(166, 166, 166) // End color (lighter gray)
        );

        // Set the gradient paint
        g2.setPaint(gradient);

        // Draw a filled rectangle for the gradient
        g2.setStroke(new BasicStroke(1));
        g2.fillRect(startX, 0, endX - startX, chooseModeButtonEndY); // Rectangle spans horizontally across `startX` to `endX`
        g2.setColor(new Color(137, 137, 137));
        g2.drawLine(startX, chooseModeButtonEndY, endX, chooseModeButtonEndY);
    }

    public void drawTileButtons(Graphics2D g2) {
        for (int i = 0; i < tileRelatedButtons.size() ; i++) {
            tileRelatedButtons.get(i).draw(g2);
        }
    }

    public void addIconImage(MyButton button, String fileName){
        try {
            button.image = ImageIO.read(getClass().getResource("/icons/" + fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error gracefully
        }
    }

    public void addBottomRow() {
        // Create a new tile map with one more row at the bottom
        int[][] newTileMap = new int[ep.currentMapMaxCol][ep.currentMapMaxRow + 1];

        // Copy the old map and add a new row at the bottom
        for (int i = 0; i < newTileMap.length; i++) {
            for (int j = 0; j < newTileMap[0].length; j++) {
                if (j == newTileMap[0].length - 1) { // Last row will be the new row at the bottom
                    if (ep.mapArea.getSelectedTile() == -1) {
                        newTileMap[i][j] = 0;
                    } else {
                        newTileMap[i][j] = ep.mapArea.getSelectedTile();
                    }
                } else {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i][j];
                }
            }
        }

        // Update map dimensions and the tile map
        ep.currentMapMaxRow++;
        ep.tileM.mapTileNum = newTileMap;
    }

    public void addRightCol() {
        // Create a new tile map with one more column
        int[][] newTileMap = new int[ep.currentMapMaxCol + 1][ep.currentMapMaxRow];

        // Copy the old map and add a new column to the right
        for (int i = 0; i < newTileMap.length; i++) {
            for (int j = 0; j < newTileMap[0].length; j++) {
                if (i == newTileMap.length - 1) {
                    if (ep.mapArea.getSelectedTile() == -1) {
                        newTileMap[i][j] = 0;
                    } else {
                        newTileMap[i][j] = ep.mapArea.getSelectedTile();
                    }
                } else {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i][j];
                }
            }
        }

        // Update map dimensions and the tile map
        ep.currentMapMaxCol++;
        ep.tileM.mapTileNum = newTileMap;
    }

    public void addLeftCol() {
        // Create a new tile map with one more column on the left
        int[][] newTileMap = new int[ep.currentMapMaxCol + 1][ep.currentMapMaxRow];

        // Copy the old map and add a new column to the left
        for (int i = 0; i < ep.currentMapMaxCol; i++) {  // Loop through the original map's columns
            for (int j = 0; j < ep.currentMapMaxRow; j++) {
                // Shift the columns to the right in the new tile map
                newTileMap[i + 1][j] = ep.tileM.mapTileNum[i][j];  // Copy from old map to new map, shifted by 1 in the column
            }
        }

        // Set the new left column
        for (int j = 0; j < ep.currentMapMaxRow; j++) {
            if (ep.mapArea.getSelectedTile() == -1) {
                newTileMap[0][j] = 0;  // Set default tile value if no tile is selected
            } else {
                newTileMap[0][j] = ep.mapArea.getSelectedTile();  // Set selected tile in the new left column
            }
        }

        // Update map dimensions and the tile map
        ep.currentMapMaxCol++;
        ep.tileM.mapTileNum = newTileMap;
    }

    public void addTopRow() {
        // Create a new tile map with one more row at the top
        int[][] newTileMap = new int[ep.currentMapMaxCol][ep.currentMapMaxRow + 1];

        // Copy the old map and add a new row at the top
        for (int i = 0; i < newTileMap.length; i++) {
            for (int j = 0; j < newTileMap[0].length; j++) {
                if (j == 0) { // First row will be the new row at the top
                    if (ep.mapArea.getSelectedTile() == -1) {
                        newTileMap[i][j] = 0;
                    } else {
                        newTileMap[i][j] = ep.mapArea.getSelectedTile();
                    }
                } else {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i][j - 1];
                }
            }
        }

        // Update map dimensions and the tile map
        ep.currentMapMaxRow++;
        ep.tileM.mapTileNum = newTileMap;
    }

    public void removeLeftCol() {
        if (ep.currentMapMaxCol > 1) {  // Only remove if there is more than one column
            // Create a new tile map with one less column
            int[][] newTileMap = new int[ep.currentMapMaxCol - 1][ep.currentMapMaxRow];

            // Copy the old map and remove the first column
            for (int i = 0; i < newTileMap.length; i++) {
                for (int j = 0; j < newTileMap[0].length; j++) {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i + 1][j];
                }
            }

            // Update map dimensions and the tile map
            ep.currentMapMaxCol--;
            ep.tileM.mapTileNum = newTileMap;
        }
    }

    public void removeRightCol() {
        if (ep.currentMapMaxCol > 1) {  // Only remove if there is more than one column
            // Create a new tile map with one less column
            int[][] newTileMap = new int[ep.currentMapMaxCol - 1][ep.currentMapMaxRow];

            // Copy the old map and remove the last column
            for (int i = 0; i < newTileMap.length; i++) {
                for (int j = 0; j < newTileMap[0].length; j++) {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i][j];
                }
            }

            // Update map dimensions and the tile map
            ep.currentMapMaxCol--;
            ep.tileM.mapTileNum = newTileMap;
        }
    }

    public void removeTopRow() {
        if (ep.currentMapMaxRow > 1) {  // Only remove if there is more than one row
            // Create a new tile map with one less row
            int[][] newTileMap = new int[ep.currentMapMaxCol][ep.currentMapMaxRow - 1];

            // Copy the old map and remove the first row
            for (int i = 0; i < newTileMap.length; i++) {
                for (int j = 0; j < newTileMap[0].length; j++) {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i][j + 1];
                }
            }

            // Update map dimensions and the tile map
            ep.currentMapMaxRow--;
            ep.tileM.mapTileNum = newTileMap;
        }
    }

    public void removeBottomRow() {
        if (ep.currentMapMaxRow > 1) {  // Only remove if there is more than one row
            // Create a new tile map with one less row
            int[][] newTileMap = new int[ep.currentMapMaxCol][ep.currentMapMaxRow - 1];

            // Copy the old map and remove the last row
            for (int i = 0; i < newTileMap.length; i++) {
                for (int j = 0; j < newTileMap[0].length; j++) {
                    // Copy the values from the old tile map
                    newTileMap[i][j] = ep.tileM.mapTileNum[i][j];
                }
            }

            // Update map dimensions and the tile map
            ep.currentMapMaxRow--;
            ep.tileM.mapTileNum = newTileMap;
        }
    }



    public void clickActionTileButtons(int i){
        if(i == 0){
            addRightCol();
        } else if (i == 1){
            addBottomRow();
        } else if (i == 2){
            addLeftCol();
        } else if (i == 3){
            addTopRow();
        } else if (i == 4){
            removeRightCol();
        } else if (i == 5){
            removeBottomRow();
        } else if (i == 6){
            removeLeftCol();
        } else if (i == 7){
            removeTopRow();
        } else if (i == 8){
            ep.mapArea.resetPosition();
        } else if (i == 9){
            ep.mapArea.showWallTiles = !ep.mapArea.showWallTiles;
        } else if (i == 10){
            ep.mapArea.drawGrid = !ep.mapArea.drawGrid;
        }
    }

    private void handleSwitchButtons(){
        if(ep.mouseH.leftMousePressed){
            StateSwitchButton current = switchModeButtons.get(0);
            if (ep.mouseH.mouseAreaAbs(current.posX, current.posX + current.buttonWidth, current.posY, current.posY + current.buttonHeight)) {
                ep.panelMode = EditorPanel.tileMode;
                ep.selectedDecor = null;
                ep.mouseH.leftMousePressed = false;
                for (int i = 0; i < switchModeButtons.size(); i++) {
                    if(switchModeButtons.get(i) != current){
                        switchModeButtons.get(i).isPressed = false;
                    } else {
                        switchModeButtons.get(i).isPressed = true;
                    }
                }
                return;
            }
            current = switchModeButtons.get(1);
            if (ep.mouseH.mouseAreaAbs(current.posX, current.posX + current.buttonWidth, current.posY, current.posY + current.buttonHeight)) {
                ep.panelMode = ep.decorationMode;
                ep.mouseH.leftMousePressed = false;
                clearTileSelection();
                for (int i = 0; i < switchModeButtons.size(); i++) {
                    if(switchModeButtons.get(i) != current){
                        switchModeButtons.get(i).isPressed = false;
                    } else {
                        switchModeButtons.get(i).isPressed = true;
                    }
                }
                return;
            }
        }
    }

    public void clearTileSelection(){
        ep.mapArea.selectedTile = -1;
        ep.mapArea.hoveredRow = -1;
        ep.mapArea.hoveredCol = -1;
        ep.mapArea.hoveredTile = -1;
    }

    private void createSwitchModeButtons() {
        int x = chooseModeButtonStartX;
        int y = chooseModeButtonEndY - chooseButtonHeight;
        StateSwitchButton tileStateButton = new StateSwitchButton();
        tileStateButton.posX = x;
        tileStateButton.posY = y;
        tileStateButton.buttonWidth = chooseModeButtonWidth;
        tileStateButton.buttonHeight = chooseButtonHeight;
        tileStateButton.caption = "Tile";
        tileStateButton.isPressed = true;
        switchModeButtons.add(tileStateButton);

        x = x + chooseModeButtonWidth;
        StateSwitchButton decorStateButton = new StateSwitchButton();
        decorStateButton.posX = x;
        decorStateButton.posY = y;
        decorStateButton.buttonWidth = chooseModeButtonWidth;
        decorStateButton.buttonHeight = chooseButtonHeight;
        decorStateButton.caption = "Decoration";
        switchModeButtons.add(decorStateButton);
    }

    public void createTileButtons(){
        int x = 10 + startX;
        MyButton addRightRow = new MyButton();
        addIconImage(addRightRow, "addRightRow");
        addRightRow.posX = x;
        addRightRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(addRightRow);

        x = x + buttonWidth;
        MyButton addBottomRow = new MyButton();
        addIconImage(addBottomRow, "addBottomRow");
        addBottomRow.posX = x;
        addBottomRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(addBottomRow);

        x = x + buttonWidth;
        MyButton addLeftRow = new MyButton();
        addIconImage(addLeftRow, "addLeftRow");
        addLeftRow.posX = x;
        addLeftRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(addLeftRow);

        x = x + buttonWidth;
        MyButton addTopRow = new MyButton();
        addIconImage(addTopRow, "addTopRow");
        addTopRow.posX = x;
        addTopRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(addTopRow);

        x = x + buttonWidth;
        MyButton removeRightRow = new MyButton();
        addIconImage(removeRightRow, "removeRightRow");
        removeRightRow.posX = x;
        removeRightRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(removeRightRow);

        x = x + buttonWidth;
        MyButton removeBottomRow = new MyButton();
        addIconImage(removeBottomRow, "removeBottomRow");
        removeBottomRow.posX = x;
        removeBottomRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(removeBottomRow);

        x = x + buttonWidth;
        MyButton removeLeftRow = new MyButton();
        addIconImage(removeLeftRow, "removeLeftRow");
        removeLeftRow.posX = x;
        removeLeftRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(removeLeftRow);

        x = x + buttonWidth;
        MyButton removeTopRow = new MyButton();
        addIconImage(removeTopRow, "removeTopRow");
        removeTopRow.posX = x;
        removeTopRow.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(removeTopRow);

        x = x + buttonWidth;
        MyButton refresh = new MyButton();
        addIconImage(refresh, "refresh");
        refresh.posX = x;
        refresh.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(refresh);

        x = x + buttonWidth;
        MyButton wall = new MyButton();
        addIconImage(wall, "wall");
        wall.posX = x;
        wall.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(wall);

        x = x + buttonWidth;
        MyButton tile = new MyButton();
        addIconImage(tile, "grid");
        tile.posX = x;
        tile.posY = startY + buttonsRowStartY;
        tileRelatedButtons.add(tile);
    }
    public void createExportButton(){
        exportButton = new MyButton();
        addIconImage(exportButton, "export");
        exportButton.posX = endX - 50;
        exportButton.posY = startY + buttonsRowStartY;
    }
    public void handleLoadFileButton(){
        if(loadFileButton.isClicked) {
            loadFileButton.isClicked = false;
            buttonClicked = true;
            loadFileButtonAction();
        }
        if(loadFileButton.isPressed){
            buttonPressed = true;
        }



        if(counter > 20) {
            counter = 0;
            buttonClicked = false;
            loadFileButton.isPressed = false;
        }

        if(pressedCounter > 20){
            loadFileButton.isPressed = false;
        }
    }
    public void createNewFileButton(){
        newFileButton = new MyButton();
        addIconImage(newFileButton, "newFileIcon");
        newFileButton.posX = endX - 50 - buttonWidth;
        newFileButton.posY = startY + buttonsRowStartY;
    }
    public void createLoadFileButton(){
        loadFileButton = new MyButton();
        addIconImage(loadFileButton, "loadFileIcon");
        loadFileButton.posX = endX - 50 - buttonWidth * 2;
        loadFileButton.posY = startY + buttonsRowStartY;
    }
    public void updateExportButton(){
        if(exportButton.isClicked) {
            exportButton.isClicked = false;
            buttonClicked = true;
            exportMap();
            exportObjects();
            exportFighters();
            exportDecoration();
        }
        if(exportButton.isPressed) {
            buttonPressed = true;
        }

        if(counter > 20) {
            counter = 0;
            buttonClicked = false;
            exportButton.isPressed = false;
        }
        if(pressedCounter > 20){
            exportButton.isPressed = false;
        }
    }

    private void handleNewFileButton() {
        if(newFileButton.isClicked) {
            newFileButton.isClicked = false;
            buttonClicked = true;
            newFileButtonAction();
        }
        if(newFileButton.isPressed){
            buttonPressed = true;
        }

        if(counter > 20) {
            counter = 0;
            buttonClicked = false;
            newFileButton.isPressed = false;
        }

        if(pressedCounter > 20){
            newFileButton.isPressed = false;
        }
    }

    private void newFileButtonAction() {
        int[][] newMap = new int[30][30];
        ep.currentMapMaxRow = 30;
        ep.currentMapMaxCol = 30;
        for (int[] row : newMap) {
            Arrays.fill(row, 0); // Fill the map with default value 1
        }
        ep.currentMap = 1 + ep.getHighestMapNumber("res/maps");
        ep.tileM.mapTileNum = newMap;
        for (int i = 0; i < ep.decorManager.decorsOnTheMap.length; i++) {
            if(ep.decorManager.decorsOnTheMap[i] == null){
                break;
            }
            ep.decorManager.decorsOnTheMap[i] = null;
        }

        String fileName = "map" + ep.currentMap + ".txt";
        //create file:

        File newFile = new File("res/maps/" + fileName);

        try {
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());

                // Write the map to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                    for (int[] row : newMap) {
                        for (int tile : row) {
                            writer.write("00" + tile + " ");
                        }
                        writer.newLine(); // Move to the next line for the next row
                    }
                }

                System.out.println("Map successfully written to: " + fileName);
            } else {
                System.out.println("File already exists or could not be created.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while creating the file.");
        }
        ep.mapArea.updateTileCoordinatesArray();
        ep.mapArea.calculateTileCoordinates();
    }

    public void exportMap() {
        String filePath = "res/maps/map" + ep.currentMap + ".txt"; // Define the file path

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Loop through the tile map array
            for (int i = 0; i < ep.currentMapMaxRow; i++) {
                StringBuilder line = new StringBuilder(); // Use StringBuilder to construct each line
                for (int j = 0; j < ep.currentMapMaxCol; j++) {
                    // Format each number to be 3 digits
                    String formattedNumber = String.format("%03d", ep.tileM.mapTileNum[j][i]);
                    line.append(formattedNumber);

                    if (j < ep.currentMapMaxCol - 1) {
                        line.append(" "); // Add a space between numbers
                    }
                }
                writer.write(line.toString()); // Write the formatted line to the file
                writer.newLine(); // Add a newline
            }
            System.out.println("Map successfully exported to " + filePath);
        } catch (IOException e) {
            System.err.println("Error while exporting the map: " + e.getMessage());
        }
    }
    public void updateTileButtons(){
        for (int i = 0; i < tileRelatedButtons.size(); i++) {
            if(tileRelatedButtons.get(i).isClicked){
                tileRelatedButtons.get(i).isClicked = false;
                clickActionTileButtons(i);
                buttonClicked = true;
                ep.mapArea.updateTileCoordinatesArray();
                ep.mapArea.calculateTileCoordinates();
            }

            if(tileRelatedButtons.get(i).isPressed){
                buttonPressed = true;
            }
        }

        if(counter > 20) {
            counter = 0;
            buttonClicked = false;
            for (int i = 0; i < tileRelatedButtons.size(); i++) {
                if(tileRelatedButtons.get(i).isPressed)
                    tileRelatedButtons.get(i).isPressed = false;
            }
        }

        if(pressedCounter > 20){
            for (int i = 0; i < tileRelatedButtons.size(); i++) {
                tileRelatedButtons.get(i).isPressed = false;
            }
        }
    }

    public void matchImageReferenceForExport() {
        for (int i = 0; i < ep.decorManager.decorsOnTheMap.length; i++) {
            Decor currentDecor = ep.decorManager.decorsOnTheMap[i];
            if (currentDecor == null) {
                break;
            }

            for (int j = 0; j < i; j++) {
                Decor previousDecor = ep.decorManager.decorsOnTheMap[j];
                if (previousDecor != null && currentDecor.name.equals(previousDecor.name)) {
                    currentDecor.image = previousDecor.image;
                    break; // Ensure no chain references; stop at the first match
                }
            }
        }
    }
}