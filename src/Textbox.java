import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Textbox {
    private Rectangle rect;
    private String text;
    private String[] rows;
    private Font font;
    private FontMetrics metrics;
    private Integer maxCharsPerRow;
    private Graphics graphics;


    public Textbox(String text, Point upperLeft, Point lowerRight, Graphics graphics) {
        this.text = text;
        this.graphics = graphics;
        rect = new Rectangle(upperLeft.x, upperLeft.y, lowerRight.x - upperLeft.x, lowerRight.y - upperLeft.y);
        font = new Font("Arial", Font.BOLD, 12);
        graphics.setFont(font);
        metrics = graphics.getFontMetrics();
        graphics.setFont(new Font("Arial", Font.BOLD, (int) Math.round(calculateFontSize())));
        maxCharsPerRow = rect.width / metrics.getMaxAdvance();
        rows = splitLines(text);
    }

    public void draw() {
        for (int i = 0; i < rows.length; i++) {
            drawTextWithOutline(rows[i], rect.width / 2, rect.y + i * metrics.getHeight() - metrics.getAscent());
        }
    }

    private String[] splitLines(String text) {
        ArrayList<String> words = new ArrayList<String>(List.of(text.split("\\s+")));
        StringBuilder bob = new StringBuilder();
        int currentRowLength = 0;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() > maxCharsPerRow) {
                String[] insert = splitWord(words.get(i));
                words.set(i, insert[0]);
                for ( int m = 1; m < insert.length; m++) {
                    words.add(i+m, insert[m]);
                }
            }
            if (currentRowLength + words.get(i).length() > maxCharsPerRow) { bob.append('\n'); currentRowLength = 0; }
            bob.append(words.get(i)).append(" ");
            currentRowLength += words.get(i).length() + 1;
        }

        return bob.toString().split("\n");
    }

    private String[] splitWord(String word) {
        System.out.println(word.length());
        String[] opt = new String[word.length() / maxCharsPerRow +1];
        for (int i = 0; i * maxCharsPerRow < word.length(); i++) {
            int end = (i+1) * maxCharsPerRow;
            opt[i] = word.substring(end - maxCharsPerRow, Math.min(end, word.length()));
        }
        return opt;
    }

    private Double calculateFontSize() {
        double charea = metrics.getMaxAdvance() * metrics.getHeight() / (font.getSize2D() * font.getSize2D());
        return Math.sqrt(rect.height * rect.width / (2.0 * charea * text.length()));
    }

    // take a wild fucking guess
    private void drawTextWithOutline(String text, int x, int y){
        graphics.setColor(Color.black);
        int ow = metrics.getHeight() / 15;
        ow = ow > 0 ? ow : 1;
        graphics.drawString(text, x + ow, y - ow);
        graphics.drawString(text, x + ow, y + ow);
        graphics.drawString(text, x - ow, y - ow);
        graphics.drawString(text, x - ow, y + ow);

        graphics.setColor(Color.white);
        graphics.drawString(text, x, y);
    }
}
