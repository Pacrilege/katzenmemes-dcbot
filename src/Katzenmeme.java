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
import java.util.Arrays;

public class Katzenmeme {
    private Katzenbild bild;
    private ArrayList<Textbox> textboxes;

    public Katzenmeme(Args args) {
        textboxes = new ArrayList<>();
        bild = new Katzenbild(args.getCategory());
        Graphics graphics = bild.getImg().getGraphics();
        graphics.setColor(Color.BLACK);
        textboxes.add(new Textbox(
                args.getTopText(),
                new Point((int) (bild.getImg().getWidth() * 0.1), (int) (bild.getImg().getHeight() * 0.05)),
                new Point((int) (bild.getImg().getWidth() * 0.9), (int) (bild.getImg().getHeight() * 0.35)),
                graphics
        ));
        textboxes.add(new Textbox(
                args.getBottomText(),
                new Point((int) (bild.getImg().getWidth() * 0.1), (int) (bild.getImg().getHeight() * 0.65)),
                new Point((int) (bild.getImg().getWidth() * 0.9), (int) (bild.getImg().getHeight() * 0.95)),
                graphics
        ));
        writeOnImg();
    }

    //divide caption input in several lines/strings
    private String[] splitLines(String text, final int maxDigitInLine){

        String[] wordArray = text.split("\\s+");
        ArrayList<StringBuffer> lineArray = new ArrayList<StringBuffer>();

        lineArray.add(new StringBuffer());
        for (String s : wordArray) {
            if ((lineArray.get(lineArray.size() - 1).length() != 0)&& //If line is not empty and
                (lineArray.get(lineArray.size() - 1).length() + s.length() >= maxDigitInLine)) // would be longer than max
                lineArray.add(new StringBuffer()); //new line
            lineArray.get(lineArray.size() - 1).append(s).append(" ");
        }

        int notNullValues = 0;
        for (StringBuffer s: lineArray) {
            if ((s != null) && !s.isEmpty()){
                System.out.println(s);
                notNullValues++;
            }
        }

        String[] arr = new String[notNullValues];

        int idx_arr = 0;
        int idx_buf = 0;
        while (idx_arr < notNullValues){
            if ((!lineArray.get(idx_buf).toString().equals(""))){
                arr[idx_arr] = lineArray.get(idx_buf).toString();
                idx_buf++;
            }
            idx_arr++;
        }
        System.out.println(Arrays.deepToString(arr));
        return arr;
    }

    private void writeOnImg(){
        for (Textbox t: textboxes) {
            t.draw();
        }

        try {
            ImageIO.write(bild.getImg(), "png", bild.getFile());
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Image Created");
    }

    public void send(MessageChannel channel) {
        channel.sendFile(bild.getFile()).queue();
    }
}
