import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;



public class addText {
    //divide caption input in several lines/strings
    static String[] capTextWidth(String text, final int maxDigitInLine){

        String[] wordArray = text.split("\\s+");
        ArrayList<StringBuffer> lineArray = new ArrayList<StringBuffer>();

        lineArray.add(new StringBuffer());
        for (String s : wordArray) {
            lineArray.get(lineArray.size() - 1).append(s).append(" ");
            if (lineArray.get(lineArray.size() - 1).length() >= maxDigitInLine) lineArray.add(new StringBuffer());
        }

        String[] arr = new String[lineArray.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = lineArray.get(i).toString();
        }

        //System.out.println(Arrays.deepToString(arr));
        return arr;
    }

    static void writeOnImg(String text, float lineDistance, float textScale){
        BufferedImage image = null;
        try {
            File img = new File("img/img.png");
            image = ImageIO.read(img);
        } catch (IOException e){
            e.printStackTrace();
        }

        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.BLACK);
        Font font = new Font("Impact", Font.BOLD, Math.round(findTextSize(image, text.length())));

        graphics.setFont(font);
        Rectangle rect = new Rectangle(image.getWidth(), image.getHeight());

        FontMetrics metrics = graphics.getFontMetrics(font);

        int maxCharsPerLine = (int)(image.getWidth() / metrics.charWidth('w'));

        String[] lines = capTextWidth(text, maxCharsPerLine);
        int printHeight = Math.round(
                image.getHeight() - (metrics.getHeight() * lines.length * lineDistance) - (font.getSize() * .5f)
        );
        int textx, texty;
        for (String line: lines) {
            textx = rect.x + (rect.width - metrics.stringWidth(line)) / 2;
            texty = printHeight + metrics.getAscent();

            System.out.printf("x: %d, y: %d, text: %s\n", textx, texty, line);

            printHeight += metrics.getHeight() * lineDistance;

            graphics.drawString(line, textx, texty);
        }

        try {
            ImageIO.write(image, "png", new File(
                    "img/img.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Image Created");
    }

    private static int findTextSize(BufferedImage img, int textLength) {
        double scalingByImageArea = Math.sqrt(img.getWidth() * img.getHeight()) * 0.1;
        int r = (int)Math.round((textLength < 35) ? scalingByImageArea : (scalingByImageArea * 35 / textLength));
        return r > 0 ? r : 5;
    }
}
