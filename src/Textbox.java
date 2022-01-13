import java.awt.*;

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
        font = new Font("Arial", Font.BOLD, (int) Math.round(calculateFontSize()));
        metrics = graphics.getFontMetrics(font);
        maxCharsPerRow = rect.width / metrics.getMaxAdvance();
        rows = new String[text.length() / maxCharsPerRow + 1];

        String[] words = text.split("\\s+");
        StringBuilder bob = new StringBuilder();
        int wordc = 0;
        for (int i = 0; i < rows.length; i++) {
            while (bob.length() + wordc + 1 <= maxCharsPerRow) {
                bob.append(words[wordc++]);
            }
            rows[i] = bob.toString();
            bob = new StringBuilder();
        }
    }

    private String[] splitWord(String word) {
        String[] opt = new String[word.length() / maxCharsPerRow];

    }

    private Double calculateFontSize() {
        double charea = metrics.getMaxAdvance() * metrics.getHeight() / (font.getSize2D() * font.getSize2D());
        return Math.sqrt(rect.height * rect.width / 2.0 * charea * text.length());
    }

    // take a wild fucking guess
    private void drawTextWithOutline(int x, int y){
        graphics.setColor(Color.black);
        int ow = (int)(metrics.getHeight() / 15);
        ow = ow > 0 ? ow : 1;
        graphics.drawString(text, x + ow, y - ow);
        graphics.drawString(text, x + ow, y + ow);
        graphics.drawString(text, x - ow, y - ow);
        graphics.drawString(text, x - ow, y + ow);

        graphics.setColor(Color.white);
        graphics.drawString(text, x, y);
    }
}
