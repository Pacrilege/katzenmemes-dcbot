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
import java.util.Scanner;
import javax.imageio.*;
import javax.swing.*;
public class catFinder {


    static String getCatImageURL() {
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
        String value = str.substring(str.indexOf("\"url\":\"")+7, str.indexOf("\",\"wid"));

        //System.out.println(response.body());
        //System.out.printf("value: %s \n",value);
        return value;

    }

    static void imgFromUrl(String urlol) {
        Image image = null;
        try {
            URL url = new URL(urlol);
            BufferedImage img = ImageIO.read(url);
            File file = new File("img/img.png");

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