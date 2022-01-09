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
import java.util.ArrayList;

public class Katzenmeme {
    private final String SAVE_PATH = "img/meme.png";

    public Katzenmeme(String text, int args) {
        try {
            imgFromUrl(getCatImageURL());
        } catch (NullPointerException | MalformedURLException e) {
            System.out.println("A problem occured during image retrieval :c");
            e.printStackTrace();
        }

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

    //save image from URL to img/img.png
    private void imgFromUrl(URL url) {
        try {
            BufferedImage img = ImageIO.read(url);
            File file = new File(SAVE_PATH);

            //create new file or overwrite if already there
            if (file.createNewFile()) {
                ImageIO.write(img, "png", file);
                System.out.println("File created: " + file.getName());
            } else {
                ImageIO.write(img, "png", file);
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Returns url to cat image, which is fetched from "theCatApi"
    private static URL getCatImageURL() throws NullPointerException, MalformedURLException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://api.thecatapi.com/v1/images/search?mime_types=png"))
                .header("x-api-key", System.getenv("CAT_API_TOKEN"))
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((response == null)||(response.body() == null) ){
            System.out.println("Error while interacting with catApi");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatImageURL();
        }

        String str = response.body();
        String value = str.substring(str.indexOf("\"url\":\"")+7, str.indexOf("\",\"wid")); //get URL from response string
        URL url = null;
        try {
            url = new URL(value);
        } catch (Exception e){
            System.out.println("Error while finding url, retry...");
            getCatImageURL();
        }
        return url;
    }

    public void send(MessageChannel channel) {
        channel.sendFile(new File(SAVE_PATH)).queue();
    }
}
