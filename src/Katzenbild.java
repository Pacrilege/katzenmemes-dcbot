import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Katzenbild {
    private final String SAVE_PATH = "img/meme.png";
    public final File file = new File(SAVE_PATH);
    public BufferedImage img = null;
    private int tries = 0;
    private final int max_tries = 5;

    public Katzenbild(URL url) {
        try {
            imgFromUrl(getCatImageURL());
        } catch (NullPointerException | MalformedURLException e) {
            retryImageRetrival();
            System.out.println("A problem occured during image retrieval :c");
            e.printStackTrace();
        }
    }

    //save image from URL to img/img.png
    private void imgFromUrl(URL url) {
        try {
            img = ImageIO.read(url);
            //create new file or overwrite if already there
            if (file.createNewFile()) {
                ImageIO.write(img, "png", file);
                System.out.println("File created: " + file.getName());
            } else {
                ImageIO.write(img, "png", file);
                System.out.println("File already exists.");
            }

            if (file.length() > 750000){ //Don't excite the max file size (8388608 - ~text)
                System.out.println("Filesize to big, another image is generated");
                retryImageRetrival();
            }
        } catch (IOException e) {
            System.out.println("Error while getting img from URL");
            retryImageRetrival();
            e.printStackTrace();
        }
    }

    //Returns url to cat image, which is fetched from "theCatApi"
    private URL getCatImageURL() throws NullPointerException, MalformedURLException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://api.thecatapi.com/v1/images/search?mime_types=jpg,png"))
                .header("x-api-key", System.getenv("CAT_API_TOKEN"))
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            retryImageRetrival();
            System.out.println("Error while getting URL from API");
            e.printStackTrace();
            return getCatImageURL();
        }

        if ((response == null)||(response.body() == null) ){
            System.out.println("Error while interacting with catApi, try again");
            retryImageRetrival();
            return getCatImageURL();
        }

        String str = response.body();
        String value = str.substring(str.indexOf("\"url\":\"")+7, str.indexOf("\",\"wid")); //get URL from response string
        URL url = null;
        try {
            url = new URL(value);
        } catch (Exception e){
            System.out.println("Error reading url, retry...");
            retryImageRetrival();
        }
        return url;
    }

    private void retryURLRetrival(){
        tries++;
        System.out.printf("Error during URLRetrival. This is the %d try", tries);
        try {
            if (tries<max_tries) getCatImageURL();
        } catch (NullPointerException | MalformedURLException e) {
            System.out.println("A problem occured during url retrieval :c");
            e.printStackTrace();
        }
    }

    private void retryImageRetrival(){
        tries++;
        System.out.printf("Error during ImageRetrival. This is the %d try", tries);
        try {
            if (tries<max_tries) imgFromUrl(getCatImageURL());
        } catch (NullPointerException | MalformedURLException e) {
            System.out.println("A problem occured during image retrieval :c");
            e.printStackTrace();
        }
    }
}
