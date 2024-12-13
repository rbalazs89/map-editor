package Elements;

import Main.EditorPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    EditorPanel ep;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(EditorPanel ep) {
        this.ep = ep;
        tile = new Tile[999];
        getTileImage();
        //getTileManagerData(ep.getHighestMapNumber("res/maps")); // Load initial map data
        getTileManagerData(1); // Load initial map data
    }

    public void getTileManagerData(int mapNumber) {
        getMapDimensions("/maps/map" + mapNumber + ".txt");
        mapTileNum = new int[ep.currentMapMaxCol][ep.currentMapMaxRow];
        loadMap("/maps/map" + mapNumber + ".txt");
    }

    private void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < ep.currentMapMaxCol && row < ep.currentMapMaxRow) {
                String line = br.readLine();
                while (col < ep.currentMapMaxCol) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == ep.currentMapMaxCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMapDimensions(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            if (line != null) {
                ep.currentMapMaxCol = line.split(" ").length; // Should be 50
            }
            int rowCount = 0;
            while (line != null) {
                rowCount++;
                line = br.readLine();
            }
            ep.currentMapMaxRow = rowCount; // Should be 50

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTileImage() {
        //setup(0, "act1/grass2", false);
        setup2(0, "act1/grass", false); //1
        setup(1,"act1/grass_wall", true); //2
        setup(2,"act1/Ggrass_2", true); //1
        setup(3,"act1/water_1", true); // 3
        setup(4,"act1/water_2", true); // 4
        setup(5,"act1/water_3", true); // 5
        setup(6,"act1/water_4", true); // 6
        setup(7,"act1/grasswater_1", true); // 7
        setup(8,"act1/grasswater_2", true); // 8
        setup(9,"act1/grasswater_3", true); // 9
        setup(10,"act1/grasswater_4", true); // 10
        setup(11,"act1/grasswater_5", true); // 11
        setup(12,"act1/grasswater_6", true); // 12
        setup(13,"act1/grasswater_7", true); // 13
        setup(14,"act1/grasswater_8", true); // 14
        setup(15,"act1/grasswateredge_1", true); // 15
        setup(16,"act1/grasswateredge_2", true); // 16
        setup(17,"act1/grasswateredge_3", true); // 17
        setup(18,"act1/grasswateredge_4", true); // 18
        setup(19,"act1/grasswater_7", false); // 13
        setup(20,"act1/grasswater_3", false); // 9
        setup(21,"act1/grassroad", false); // 19
        setup(22,"act1/grassdirt_1", false); // 20th 21th
        setup(23,"act1/grassdirt_3", false); // 22
        setup(24,"act1/grassdirt_4", false); // 23
        setup(25,"act1/grassdirt_5", false); // 24
        setup(26,"act1/grassdirt_6", false); // 25
        setup(27,"act1/grassdirt_7", false); // 26
        setup(28,"act1/dirt", false); // 27
        setup(29,"act1/dirt", true); // 27
        setup(30,"act1/rock_1",false); // delete
        setup(31,"act1/rock_2",false); // delete
        setup(32,"act1/rock_3",false); // delete
        setup(33,"act1/rock_4",false); // delete
        setup2(34,"act1/cave", false); // 28
        setup(35,"act1/grassrock1", true); // 29
        setup(36,"act1/grassrock2", true); // 30th
        setup(37,"act1/grassdirt_edge1",false);  //31
        setup(38,"act1/grassdirt_edge2", false); // 32
        setup(39,"act1/grassdirt_edge3", false); // 33
        setup(40,"act1/grassdirt_edge4", false); // 34
        setup(41,"act1/grassroad", true); // duplicate but collision different // 19
        setup(42,"act1/fence1", true); // 35
        setup(43,"act1/fence2", true); // 36
        setup(44,"act1/fenceedge1", true); // 37
        setup(45,"act1/fenceedge2", true); // 38
        setup(46,"act1/fenceedge3", true); // 39
        setup(47,"act1/fenceedge4", true); // 40th
        setup(48, "act1/templetile", false);
        setup(49, "act1/templetile", true);
        setup(50, "act1/churchbench_left", true);
        setup(51, "act1/churchbench_right", true);
        setup(52, "act1/church_wall", true);
        setup2(53, "act1/stoneside", true);
        setup2(54, "act1/stonetop", true);
        setup2(55, "act1/stoneside_left", true);
        setup2(56, "act1/carpetedge1", false); // not used
        setup2(57, "act1/carpetedge2", false); // not used
        setup2(58, "act1/carpetedge3", false); // not used
        setup2(59, "act1/carpetedge4", false); // not used
        setup2(60, "act1/carpetleft", false);
        setup2(61, "act1/carpetright", false);
        setup2(62, "act1/wood", false); // 50th
        setup2(63, "act1/woodside_left", true);
        setup2(64, "act1/woodside_right", true);
        setup2(65, "act1/woodside_top", true);
        setup2(66, "act1/woodside_down", true);
        setup2(67, "act1/wood", true); // duplicate diff collision 5 10
        setup2(68, "act1/cellar", false);
        setup2(69, "act1/stonewall_1", true);
        setup2(70, "act1/stonewall_2", true);
        setup2(71, "act1/stonedesk_1", true);
        setup2(72, "act1/stonedesk_2", true);
        //setup2(73, "act1/stonedesk_1", true); // duplicate mistake
        setup2(74, "act1/stonebarrel", true); // 60th
        setup2(75, "act1/stonesmalldesk", true);
        setup(76, "act1/pebble", true); // not used
        setup2(77, "act1/wateredge1", true);
        setup2(78, "act1/wateredge2", true);
        setup2(79, "act1/wateredge3", true);
        setup2(80, "act1/wateredge4", true);
        setup2(81, "act1/wateredge5", true);
        setup2(82, "act1/wateredge6", true);
        setup2(83, "act1/wateredge7", true);
        setup2(84, "act1/wateredge8", true);
        setup2(85, "act1/cavewater", true); // 70th
        setup2(86, "act1/wateredge3", false); // duplicate for collision 79
        setup2(87, "act1/wateredge7", false); // duplicate for collision 83
        setup2(88, "act1/cavestairs", false);
        setup2(89, "act1/cave_a", true);
        setup2(90, "act1/cave_b", true);
        setup2(91, "act1/cave_c", true);
        setup2(92, "act1/cave_d", true);
        setup2(93, "act1/stonetop_nowall", true); // end row skip
        setup2(94, "act1/stonetop_nowall2", true); // end row skip
        setup2(95, "act1/stonetop_nowall3", true); // end row skip
        setup2(96, "act1/cave", true); // duplicate for collision
        setup2(97, "act1/bosstile2", false); // not used
        setup2(98, "act1/bosstile3", false);
        //skip 99
        setup(99, "nullimage", false); // endblock
        setup2(100, "act1/bosswall", true);
        setup2(101, "act1/bosswall_side_left", true);
        setup2(102, "act1/bosswall_side_right", true);
        setup2(103, "act1/bosswall_side_bottom", true); //80th
        setup2(104, "act1/totemtile", false);
    }

    public void setup2(int index, String imagePath, boolean collision){
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath +".png"));
            tile[index].collision = collision;
            tile[index].ID = index;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setup(int index, String imagePath, boolean collision){
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath +".png"));
            tile[index].image = scaleImage(tile[index].image, 64,64);
            tile[index].collision = collision;
            tile[index].ID = index;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0,0,width, height, null);
        g2.dispose();
        return scaledImage;
    }



    /*public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;
        while(worldCol < ep.currentMapMaxCol && worldRow < ep.currentMapMaxRow){

            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * ep.tileSize;
            int worldY = worldRow * ep.tileSize;
            g2.drawImage(tile[tileNum].image, worldX, worldY, null);
            worldCol++;
            if(worldCol == ep.currentMapMaxCol) {
                worldCol = 0;
                worldRow ++;
            }
        }
    }*/
}
