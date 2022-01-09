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
    public final File file = new File(SAVE_PATH);
    private Katzenbild bild;


    public Katzenmeme(String text, int args) {
        bild = new Katzenbild();
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
        Graphics graphics = bild.getImg().getGraphics();
        graphics.setColor(Color.BLACK);
        Font font = new Font(
                "Arial",
                Font.BOLD,
                Math.round(calculateFontSize(bild.getImg(), text.length()) * textScale)
        );

        graphics.setFont(font);
        Rectangle rect = new Rectangle(bild.getImg().getWidth(), bild.getImg().getHeight());
        FontMetrics metrics = graphics.getFontMetrics(font);

        int maxCharsPerLine = (int)(bild.getImg().getWidth() / metrics.charWidth('w'));

        String[] lines = splitLines(text, maxCharsPerLine);
        int printHeight = Math.round(
                bild.getImg().getHeight() - (metrics.getHeight() * lines.length * lineDistance) - (font.getSize() * .5f)
        );
        int textx, texty;
        for (String line: lines) {
            textx = rect.x + (rect.width - metrics.stringWidth(line)) / 2;
            texty = printHeight + metrics.getAscent();

            System.out.printf("x: %d, y: %d, text: %s\n", textx, texty, line);

            printHeight += metrics.getHeight() * lineDistance;
            drawTextWithOutline(line, textx, texty, graphics, metrics.getHeight());
        }

        try {
            ImageIO.write(bild.getImg(), "png", bild.getFile());
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
        channel.sendFile(bild.getFile()).queue();
    }

    public File getFile() {
        return file;
    }

}
