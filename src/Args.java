import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

public class Args {
    private String topText = "";
    private String bottomText = ""; //lol
    private String CATegory;

    private static Dictionary<String, Integer> categories;

    public Args(String cmd) {
        // it's a surprise tool that will help us later
        categories.put("boxes", 5);
        categories.put("clothes", 15);
        categories.put("hats", 1);
        categories.put("sinks", 14);
        categories.put("space", 2);
        categories.put("sunglasses", 4);
        categories.put("ties", 7);

        String[] args = cmd.split(" ");

        Integer top = indexOf(args, "-top");
        Integer bot = indexOf(args, "-bottom");

        if (top != null && bot != null) {
            bottomText = arrToString(args, bot, top > bot ? top : args.length);
            topText = arrToString(args, top, top < bot ? bot : args.length);
        } else if (top != null) {
            topText = arrToString(args, top);
        } else if (bot != null) {
            bottomText = arrToString(args, bot);
        } else {
            bottomText = arrToString(args);
        }
    }

    public String getTopText() {
        return topText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public String getCategory() {
        return CATegory;
    }

    private <T> Integer indexOf(T[] arr, T e) {
        for ( int i = 0; i < arr.length; i++ ) {
            if (arr[i].equals(e)) return i;
        }
        return null;
    }

    private String arrToString(String[] arr, int indexFrom, int indexTo) {
        if (indexFrom > indexTo) return null;
        StringBuilder opt = new StringBuilder();
        for (; indexFrom < indexTo; indexFrom++) {
            opt.append(arr[indexFrom])
                    .append(" ");
        }
        return opt.toString();
    }

    private String arrToString(String[] arr, int from) {
        return arrToString(arr, from, arr.length);
    }

    private String arrToString(String[] arr) {
        return arrToString(arr, 0, arr.length);
    }

    private String findCategory(String[] arr, int limit) {

    }

    private String findCategory(String[] arr) {
        return findCategory(arr, arr.length);
    }
}
