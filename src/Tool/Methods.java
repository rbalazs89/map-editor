package Tool;

import Elements.Decor;
import Main.EditorPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Methods {
    EditorPanel ep;

    public Methods(EditorPanel ep){
        this.ep = ep;
    }

    public void printSetupDecor(String folderPath) {
        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (files != null) {
                for (File file : files) {
                    // Get the relative path starting from the provided folderPath
                    String relativePath = folderPath.replace("\\", "/") + "/" + file.getName();

                    System.out.println("setupDecor(\"" + relativePath + "\");");
                }
            } else {
                System.out.println("The folder is empty or inaccessible.");
            }
        } else {
            System.out.println("Invalid folder path.");
        }
    }

    public BufferedImage makeTransparent(BufferedImage sourceImage, float transparency) {
        // Ensure transparency is clamped between 0.0f and 1.0f
        transparency = Math.max(0.0f, Math.min(1.0f, transparency));

        // Create a new image with the same dimensions as the source image
        BufferedImage transparentImage = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TRANSLUCENT
        );

        // Get the graphics context of the new image
        Graphics2D g2 = transparentImage.createGraphics();

        // Apply transparency
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

        // Draw the original image with the specified transparency
        g2.drawImage(sourceImage, 0, 0, null);

        g2.dispose(); // Clean up resources

        return transparentImage;
    }

    public static void sortDecors(Decor[] decors) {
        // Find the first null in the array
        int nonNullCount = 0;
        while (nonNullCount < decors.length && decors[nonNullCount] != null) {
            nonNullCount++;
        }

        // If no non-null elements or only one element, no need to sort
        if (nonNullCount <= 1) {
            return;
        }

        // Sort only the non-null portion of the array
        Arrays.sort(decors, 0, nonNullCount, Comparator.comparingInt(d -> d.worldY + d.imageHeight));
        for (int i = 0; i < decors.length; i++) {
            if(decors[i] == null){
                break;
            }
            decors[i].arrayPosition = i;
        }

        // Remaining part of the array (after nonNullCount) stays unchanged
    }
}
