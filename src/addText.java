import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import java.util.ArrayList;



public class addText {
    static String[] capTextWidth(String text){
        final int maxDigitInLine = 30;

        String[] wordArray = text.split("\\s+");
        ArrayList<StringBuffer> lineArray = new ArrayList<StringBuffer>();

        lineArray.add(new StringBuffer(""));
        for (String s : wordArray) {
            lineArray.get(lineArray.size() - 1).append(s).append(" ");
            if (lineArray.get(lineArray.size() - 1).length() >= maxDigitInLine) lineArray.add(new StringBuffer(""));
        }

        String[] arr = new String[lineArray.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = lineArray.get(i).toString();
        }

        //System.out.println(Arrays.deepToString(arr));
        return arr;
    }

    static void writeOnImg(String text){
        capTextWidth(text);
        BufferedImage image = null;
        try {
            File img = new File("img/img.png");
            image = ImageIO.read(img);
        } catch (IOException e){
            e.printStackTrace();
        }

        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.BLACK);
        Font font = new Font("Impact", Font.BOLD, findTextSize(image));

        graphics.setFont(font);
        Rectangle rect = new Rectangle(image.getWidth(),image.getHeight());

        FontMetrics metrics = graphics.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = (int) Math.round(rect.y + ((rect.height - metrics.getHeight()) * 0.9) + metrics.getAscent());

        graphics.drawString(text, x, y);

        try {
            ImageIO.write(image, "png", new File(
                    "img/img.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Image Created");


    }

    private static int findTextSize(BufferedImage img) {
        return (int)Math.round(Math.sqrt(img.getWidth() * img.getHeight()) * 0.1);
    }
}
