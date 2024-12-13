package Main;

import Elements.Decor;
import Elements.DecorManager;
import InterfaceBlocks.*;
import Elements.TileManager;
import Tool.Methods;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static java.awt.Color.BLACK;

public class EditorPanel extends JPanel implements Runnable{

    public Robot robot;
    public static int fps = 20;
    public static final int tileSize = 64;
    Thread thread;
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public int screenWidth = 1280;
    public int screenHeight = 768;
    public int currentMapMaxCol;
    public int currentMapMaxRow;
    public int currentMap = 1;
    public InterfaceBlock[] uiElements = new InterfaceBlock[10];
    //keep creation order:
    public TileManager tileM = new TileManager(this);
    public MapArea mapArea = new MapArea(this);
    public ButtonsArea buttonsArea= new ButtonsArea(this);
    public SelectedArea selectedArea = new SelectedArea(this);
    public PickElementArea pickElementArea = new PickElementArea(this);
    public DecorManager decorManager = new DecorManager(this);
    public int panelMode = 1;
    public static final int tileMode = 1;
    public static final int objectMode = 2;
    public static final int decorationMode = 3;
    public static final int fightersMode = 4;

    //selected decoration:
    public Decor selectedDecor = null;
    public boolean isSelectedFromMap = false;

    public Methods methods = new Methods(this);

    public EditorPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        //currentMap = getHighestMapNumber("res/maps");
        currentMap = 1;
        setupEditor();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(){
        mapArea.update();
        buttonsArea.update();
        selectedArea.update();
        pickElementArea.update();
    }

    public void setupEditor(){
        getHighestMapNumber("res/maps");
    }

    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        mapArea.draw(g2);
        buttonsArea.draw(g2);
        selectedArea.draw(g2);
        pickElementArea.draw(g2);

        g2.dispose();
    }

    public int getHighestMapNumber(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a directory: " + folderPath);
        }

        int highestNumber = -1;

        // List all files in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            return highestNumber; // Return -1 if folder is empty or inaccessible
        }

        for (File file : files) {
            String fileName = file.getName();
            // Check if the file name matches the pattern "mapN.txt"
            if (fileName.matches("map\\d+\\.txt")) {
                // Extract the number N
                String numberPart = fileName.replaceAll("\\D+", ""); // Remove non-digits
                int number = Integer.parseInt(numberPart);

                // Update the highest number
                if (number > highestNumber) {
                    highestNumber = number;
                }
            }
        }
        return highestNumber;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(thread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();

                delta--;
                drawCount++;
            }
            if (timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
            //restrictMouseToWindow();
        }
    }

    private void restrictMouseToWindow() {
        if (Main.window.isFocused()) {
            Insets insets = Main.window.getInsets();

            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            Point windowLocation = Main.window.getLocationOnScreen();

            int mouseX = (int) mouseLocation.getX();
            int mouseY = (int) mouseLocation.getY();
            int windowX = (int) windowLocation.getX() + insets.left + 1;
            int windowY = (int) windowLocation.getY();
            int windowWidth = Main.window.getWidth() - insets.left - insets.right;
            int windowHeight = Main.window.getHeight() - insets.bottom;

            int restrictedX = Math.max(windowX, Math.min(mouseX, windowX + windowWidth - 3));
            int restrictedY = Math.max(windowY, Math.min(mouseY, windowY + windowHeight - 2));

            if (mouseX != restrictedX || mouseY != restrictedY) {
                robot.mouseMove(restrictedX, restrictedY);
            }
        }
    }
}
