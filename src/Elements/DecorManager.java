package Elements;

import Main.EditorPanel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static Main.EditorPanel.tileSize;

public class DecorManager {
    public Decor[] decors = new Decor[9999];
    public Decor[] decorsOnTheMap = new Decor[9999];
    EditorPanel ep;

    public DecorManager(EditorPanel ep){
        this.ep = ep;
        fillDecorArray();
        //setupMap1();
        setupDecoration(ep.currentMap);
        checkDecor();
    }

    public void checkDecor(){
        for (int i = 0; i < decorsOnTheMap.length; i++) {
            if(decorsOnTheMap[i] == null){
                break;
            }
        }
    }

    private void fillDecorArray() {
        setupDecor("/decor/act1/3desk.png");
        setupDecor("/decor/act1/angelstatueleft.png");
        setupDecor("/decor/act1/angelstatueright.png");
        setupDecor("/decor/act1/axes.png");
        setupDecor("/decor/act1/barrel2.png");
        setupDecor("/decor/act1/benchside.png");
        setupDecor("/decor/act1/bigcabinetmagic.png");
        setupDecor("/decor/act1/bigsword.png");
        setupDecor("/decor/act1/bluebed.png");
        setupDecor("/decor/act1/bluecarpet.png");
        setupDecor("/decor/act1/bluecarpet2.png");
        setupDecor("/decor/act1/bookcase.png");
        setupDecor("/decor/act1/bookcase2.png");
        setupDecor("/decor/act1/bossdoor.png");
        setupDecor("/decor/act1/bossdoor2.png");
        setupDecor("/decor/act1/cabinet.png");
        setupDecor("/decor/act1/carpetstairs.png");
        setupDecor("/decor/act1/carriage.png");
        setupDecor("/decor/act1/cauldron.png");
        setupDecor("/decor/act1/cave2.png");
        setupDecor("/decor/act1/churcha1.png");
        setupDecor("/decor/act1/church_candle.png");
        setupDecor("/decor/act1/church_candle2.png");
        setupDecor("/decor/act1/church_window.png");
        setupDecor("/decor/act1/crest.png");
        setupDecor("/decor/act1/desk.png");
        setupDecor("/decor/act1/desk2.png");
        setupDecor("/decor/act1/desk_home.png");
        setupDecor("/decor/act1/doublebed.png");
        setupDecor("/decor/act1/fireplacewood.png");
        setupDecor("/decor/act1/fruitdesk.png");
        setupDecor("/decor/act1/fruittree.png");
        setupDecor("/decor/act1/homecabinet.png");
        setupDecor("/decor/act1/house1.png");
        setupDecor("/decor/act1/house2.png");
        setupDecor("/decor/act1/hpdesk.png");
        setupDecor("/decor/act1/knightstatue.png");
        setupDecor("/decor/act1/logs.png");
        setupDecor("/decor/act1/magiccabinet.png");
        setupDecor("/decor/act1/potioncabinet.png");
        setupDecor("/decor/act1/rack.png");
        setupDecor("/decor/act1/redbed.png");
        setupDecor("/decor/act1/redcarpet.png");
        setupDecor("/decor/act1/shopsign.png");
        setupDecor("/decor/act1/shopwindow.png");
        setupDecor("/decor/act1/showcaseside.png");
        setupDecor("/decor/act1/smallchair.png");
        setupDecor("/decor/act1/smallchair2.png");
        setupDecor("/decor/act1/starttent.png");
        setupDecor("/decor/act1/statue.png");
        setupDecor("/decor/act1/tent_2.png");
        setupDecor("/decor/act1/woodentrapdoor.png");
        setupDecor("/decor/act1/woodstairs.png");
    }

    public void setupDecoration(int mapNumber) {
        // Assuming mapNumber is passed correctly
        String filePath = "res/maps/decor" + mapNumber + ".json";
        System.out.println("printme");

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();

            // Parse the JSON file into a list of Map<String, Object>
            List<Map<String, Object>> decorList = gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>() {}.getType());
            System.out.println(decorList.size());

            // Populate decorsOnTheMap array
            for (int i = 0; i < decorList.size() && i < decorsOnTheMap.length; i++) {
                Map<String, Object> decorData = decorList.get(i);

                // Create a new Decor object
                decorsOnTheMap[i] = new Decor();
                decorsOnTheMap[i].worldX = ((Double) decorData.get("worldX")).intValue();
                decorsOnTheMap[i].worldY = ((Double) decorData.get("worldY")).intValue();
                decorsOnTheMap[i].name = (String) decorData.get("name");

                // Setup the image using the name
                decorsOnTheMap[i].image = setupImage("/decor/act1/" + decorsOnTheMap[i].name);
            }

            // Handle array position and other properties
            for (int i = 0; i < decorsOnTheMap.length; i++) {
                if (decorsOnTheMap[i] == null) {
                    break;
                }
                decorsOnTheMap[i].imageHeight = decorsOnTheMap[i].image.getHeight();
                decorsOnTheMap[i].imageWidth = decorsOnTheMap[i].image.getWidth();
                decorsOnTheMap[i].arrayPosition = i;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading the file: " + filePath);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.out.println("Error parsing JSON in file: " + filePath);
        }
    }


    public void setupMap1(){
        //use this for test, not json file:
        decorsOnTheMap[0] = new Decor();
        decorsOnTheMap[0].worldX = 35 * tileSize;
        decorsOnTheMap[0].worldY = 25 * tileSize;
        decorsOnTheMap[0].image = setupImage("/decor/act1/house1");
        decorsOnTheMap[0].name = "house1";

        decorsOnTheMap[1] = new Decor();
        decorsOnTheMap[1].worldX = 44 * tileSize;
        decorsOnTheMap[1].worldY = 2 * tileSize;
        decorsOnTheMap[1].image = setupImage("/decor/act1/starttent");
        decorsOnTheMap[1].name = "starttent";

        decorsOnTheMap[2] = new Decor();
        decorsOnTheMap[2].worldX = 13 * tileSize;
        decorsOnTheMap[2].worldY = 1 * tileSize;
        decorsOnTheMap[2].image = setupImage("/decor/act1/tent_2");
        decorsOnTheMap[2].name = "tent_2";

        decorsOnTheMap[3] = new Decor();
        decorsOnTheMap[3].worldX = 13 * tileSize;
        decorsOnTheMap[3].worldY = 5 * tileSize;
        decorsOnTheMap[3].image = setupImage("/decor/act1/3desk");
        decorsOnTheMap[3].name = "3desk";

        decorsOnTheMap[4] = new Decor();
        decorsOnTheMap[4].worldX = 18 * tileSize;
        decorsOnTheMap[4].worldY = 1 * tileSize;
        decorsOnTheMap[4].image = setupImage("/decor/act1/barrel2");
        decorsOnTheMap[4].name = "barrel2";

        decorsOnTheMap[5] = new Decor();
        decorsOnTheMap[5].worldX = 14 * tileSize;
        decorsOnTheMap[5].worldY = 6 * tileSize;
        decorsOnTheMap[5].image = setupImage("/decor/act1/smallchair2");
        decorsOnTheMap[5].name = "smallchair2";

        decorsOnTheMap[6] = new Decor();
        decorsOnTheMap[6].worldX = 12 * tileSize;
        decorsOnTheMap[6].worldY = 2 * tileSize;
        decorsOnTheMap[6].image = setupImage("/decor/act1/logs");
        decorsOnTheMap[6].name = "logs";

        decorsOnTheMap[7] = new Decor();
        decorsOnTheMap[7].worldX = 8 * tileSize;
        decorsOnTheMap[7].worldY = 1 * tileSize;
        decorsOnTheMap[7].image = setupImage("/decor/act1/carriage");
        decorsOnTheMap[7].name = "carriage";

        decorsOnTheMap[8] = new Decor();
        decorsOnTheMap[8].worldX = 0 * tileSize;
        decorsOnTheMap[8].worldY = 0 * tileSize;
        decorsOnTheMap[8].image = setupImage("/decor/act1/cave2");
        decorsOnTheMap[8].name = "cave2";

        decorsOnTheMap[9] = new Decor();
        decorsOnTheMap[9].worldX = (int)(22.8 * tileSize);
        decorsOnTheMap[9].worldY = 23 * tileSize;
        decorsOnTheMap[9].image = setupImage("/decor/act1/statue");
        decorsOnTheMap[9].name = "statue";

        decorsOnTheMap[10] = new Decor();
        decorsOnTheMap[10].worldX = (int)(5 * tileSize);
        decorsOnTheMap[10].worldY = 16 * tileSize;
        decorsOnTheMap[10].image = setupImage("/decor/act1/churcha1");
        decorsOnTheMap[10].name = "churcha1";

        decorsOnTheMap[11] = new Decor();
        decorsOnTheMap[11].worldX = 34 * tileSize;
        decorsOnTheMap[11].worldY = 31 * tileSize;
        decorsOnTheMap[11].image = setupImage("/decor/act1/shopsign");
        decorsOnTheMap[11].name = "shopsign";

        decorsOnTheMap[12] = new Decor();
        decorsOnTheMap[12].worldX = 35 * tileSize;
        decorsOnTheMap[12].worldY = 35 * tileSize;
        decorsOnTheMap[12].image = setupImage("/decor/act1/house2");
        decorsOnTheMap[12].name = "house2";

        decorsOnTheMap[13] = new Decor();
        decorsOnTheMap[13].worldX = 24 * tileSize;
        decorsOnTheMap[13].worldY = 27 * tileSize;
        decorsOnTheMap[13].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[13].name = "fruittree";

        decorsOnTheMap[14] = new Decor();
        decorsOnTheMap[14].worldX = 24 * tileSize;
        decorsOnTheMap[14].worldY = 29 * tileSize;
        decorsOnTheMap[14].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[14].name = "fruittree";

        decorsOnTheMap[15] = new Decor();
        decorsOnTheMap[15].worldX = 24 * tileSize;
        decorsOnTheMap[15].worldY = 31 * tileSize;
        decorsOnTheMap[15].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[15].name = "fruittree";

        decorsOnTheMap[16] = new Decor();
        decorsOnTheMap[16].worldX = 24 * tileSize;
        decorsOnTheMap[16].worldY = 33 * tileSize;
        decorsOnTheMap[16].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[16].name = "fruittree";

        decorsOnTheMap[17] = new Decor();
        decorsOnTheMap[17].worldX = 24 * tileSize;
        decorsOnTheMap[17].worldY = 35 * tileSize;
        decorsOnTheMap[17].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[17].name = "fruittree";

        decorsOnTheMap[18] = new Decor();
        decorsOnTheMap[18].worldX = 24 * tileSize;
        decorsOnTheMap[18].worldY = 37 * tileSize;
        decorsOnTheMap[18].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[18].name = "fruittree";

        decorsOnTheMap[19] = new Decor();
        decorsOnTheMap[19].worldX = 24 * tileSize;
        decorsOnTheMap[19].worldY = 39 * tileSize;
        decorsOnTheMap[19].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[19].name = "fruittree";

        decorsOnTheMap[20] = new Decor();
        decorsOnTheMap[20].worldX = 24 * tileSize;
        decorsOnTheMap[20].worldY = 41 * tileSize;
        decorsOnTheMap[20].image = setupImage("/decor/act1/fruittree");
        decorsOnTheMap[20].name = "fruittree";

        for (int i = 0; i < decorsOnTheMap.length; i++) {
            if(decorsOnTheMap[i] == null){
                break;
            }
            decorsOnTheMap[i].arrayPosition= i;
            decorsOnTheMap[i].imageWidth = decorsOnTheMap[i].image.getWidth();
            decorsOnTheMap[i].imageHeight = decorsOnTheMap[i].image.getHeight();
        }
    }

    public void setupDecor(String imageName) {
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(imageName));
        }catch (IOException e){
            e.printStackTrace();
        }
        Decor decor = new Decor();
        decor.image = image;
        decor.name = getPngNameFromPath(imageName);
        if(image.getHeight() > 64 || image.getHeight() > 64){
            decor.big = true;
        } else {
            decor.big = false;
        }
        decor.imageHeight = image.getHeight();
        decor.imageWidth = image.getWidth();
        for (int i = 0; i < decors.length; i++) {
            if(decors[i] == null){
                decors[i] = decor;
                decor.id = i;
                break;
            }
        }
    }

    public String getPngNameFromPath(String path) {
        if (path == null || !path.contains("/") || !path.contains(".")) {
            return null; // Handle invalid input
        }
        int lastSlashIndex = path.lastIndexOf("/");
        int lastDotIndex = path.lastIndexOf(".");

        if (lastDotIndex > lastSlashIndex) {
            return path.substring(lastSlashIndex + 1, lastDotIndex);
        }
        return "decor001"; // Return null if the format is incorrect
    }

    public BufferedImage setupImage(String imageName) {
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(imageName +".png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

}
