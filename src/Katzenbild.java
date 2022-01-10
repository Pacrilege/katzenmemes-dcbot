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
    private String SAVE_PATH;
    public File file;
    public BufferedImage img = null;
    private int attempts = 0;
    private static final int max_tries = 5;
    private static int amount_files = 0;
    private static final int max_files = 5;


    public Katzenbild() {
        make_File();
        try {
            imgFromUrl(getCatImageURL());
        } catch (NullPointerException | MalformedURLException e) {
            retryImageRetrival();
            System.out.println("A problem occured during image retrieval :c");
            e.printStackTrace();
        }
    }

    private void make_File(){
        if (amount_files >= max_files) amount_files = 0;
        SAVE_PATH = String.format("img/meme%d.png", amount_files);
        file = new File(SAVE_PATH);
        amount_files++;
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

            if (file.length() > 7500000){ //Don't excite the max file size (8388608 - ~text)
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
            retryURLRetrival();
            System.out.println("Error while getting URL from API");
            e.printStackTrace();
            return getCatImageURL();
        }

        if ((response == null)||(response.body() == null) ){
            System.out.println("Error while interacting with catApi, try again");
            retryURLRetrival();
            return getCatImageURL();
        }

        String str = response.body();
        String value = str.substring(str.indexOf("\"url\":\"")+7, str.indexOf("\",\"wid")); //get URL from response string
        URL url = null;
        try {
            url = new URL(value);
        } catch (Exception e){
            System.out.println("Error reading url, retry...");
            retryURLRetrival();
        }
        return url;
    }

    private void retryURLRetrival(){
        attempts++;
        System.out.printf("Error during URLRetrival. This is the %d try", attempts);
        try {
            if (attempts <max_tries) getCatImageURL();
        } catch (NullPointerException | MalformedURLException e) {
            System.out.println("A problem occured during url retrieval :c");
            e.printStackTrace();
        }
    }

    private void retryImageRetrival(){
        attempts++;
        System.out.printf("Error during ImageRetrival. This is the %d try \n", attempts);
        try {
            if (attempts <max_tries) imgFromUrl(getCatImageURL());
        } catch (NullPointerException | MalformedURLException e) {
            System.out.println("A problem occured during image retrieval :c");
            e.printStackTrace();
        }
    }

    public String getSAVE_PATH() {
        return SAVE_PATH;
    }

    public File getFile() {
        return file;
    }

    public BufferedImage getImg() {
        return img;
    }

}
