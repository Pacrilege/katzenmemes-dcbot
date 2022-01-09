import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.MalformedURLException;
import net.dv8tion.jda.api.entities.MessageChannel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.util.ArrayList;

public class Katzenmeme {
    private final String SAVE_PATH = "img/meme.png";

    public Katzenmeme(String text, int args) {


        writeOnImg(text, 1.2f, 1);
    }

    //divide caption input in several lines/strings
    private String[] splitLines(String text, final int maxDigitInLine){

        String[] wordArray = text.split("\\s+");
        ArrayList<StringBuffer> lineArray = new ArrayList<StringBuffer>();

        lineArray.add(new StringBuffer());
        for (String s : wordArray) {
            lineArray.get(lineArray.size() - 1).append(s).append(" ");
            if (lineArray.get(lineArray.size() - 1).length() >= maxDigitInLine) lineArray.add(new StringBuffer());
        }

        String[] arr = new String[lineArray.size()];
        for (int i = 0; i < arr.length; i++) {
            if (!lineArray.get(i).toString().equals("")) arr[i] = lineArray.get(i).toString();
        }

        //System.out.println(Arrays.deepToString(arr));
        return arr;
    }

    // take a wild fucking guess
    private void drawTextWithOutline(String text, int x, int y, Graphics g, int fontheight){
        g.setColor(Color.black);
        int ow = (int)(fontheight / 15);
        ow = ow > 0 ? ow : 1;
        g.drawString(text, x + ow, y - ow);
        g.drawString(text, x + ow, y + ow);
        g.drawString(text, x - ow, y - ow);
        g.drawString(text, x - ow, y + ow);

        g.setColor(Color.white);
        g.drawString(text, x, y);
    }

    private void writeOnImg(String text, float lineDistance, float textScale){
        BufferedImage image = null;
        try {
            File img = new File(SAVE_PATH);
            image = ImageIO.read(img);
        } catch (IOException e){
            e.printStackTrace();
        }

        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.BLACK);
        Font font = new Font(
                "Arial",
                Font.BOLD,
                Math.round(calculateFontSize(image, text.length()) * textScale)
        );

        graphics.setFont(font);
        Rectangle rect = new Rectangle(image.getWidth(), image.getHeight());

        FontMetrics metrics = graphics.getFontMetrics(font);

        int maxCharsPerLine = (int)(image.getWidth() / metrics.charWidth('w'));

        String[] lines = splitLines(text, maxCharsPerLine);
        int printHeight = Math.round(
                image.getHeight() - (metrics.getHeight() * lines.length * lineDistance) - (font.getSize() * .5f)
        );
        int textx, texty;
        for (String line: lines) {
            textx = rect.x + (rect.width - metrics.stringWidth(line)) / 2;
            texty = printHeight + metrics.getAscent();

            System.out.printf("x: %d, y: %d, text: %s\n", textx, texty, line);

            printHeight += metrics.getHeight() * lineDistance;
            drawTextWithOutline(line, textx, texty, graphics, metrics.getHeight());
            //graphics.drawString(line, textx, texty);
        }

        try {
            ImageIO.write(image, "png", new File(SAVE_PATH));
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Image Created");
    }

    // calculates font size based on image area and text length
    private int calculateFontSize(BufferedImage img, int textLength) {
        double scalingByImageArea = Math.sqrt(img.getWidth() * img.getHeight()) * 0.1;
        int r = (int)Math.round((textLength < 35) ? scalingByImageArea : (scalingByImageArea * 35 / textLength));
        return r > 0 ? r : 5;
    }

    public void send(MessageChannel channel) {
        channel.sendFile(new File(SAVE_PATH)).queue();
    }
}
