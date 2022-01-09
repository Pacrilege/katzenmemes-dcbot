import java.awt.image.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.imageio.*;
public class catFinder {



    //save image from URL to img/img.png
    static void imgFromUrl(URL url) {
        try {
            BufferedImage img = ImageIO.read(url);
            File file = new File("img/img.png");

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

}