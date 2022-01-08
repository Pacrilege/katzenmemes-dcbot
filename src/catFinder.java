import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.imageio.*;
import javax.swing.*;
public class catFinder {

    //Returns url to cat image, which is fetched from "theCatApi"
    static URL getCatImageURL() {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://api.thecatapi.com/v1/images/search"))
                .header("x-api-key", System.getenv("CAT_API_TOKEN"))
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(e);
        }
        assert response != null;

        String str = response.body();
        String value = str.substring(str.indexOf("\"url\":\"")+7, str.indexOf("\",\"wid")); //get URL from response string
        URL url = null;
        try {
            url = new URL(value);
        } catch (Exception e){
            getCatImageURL();
        }
        return url;
    }

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