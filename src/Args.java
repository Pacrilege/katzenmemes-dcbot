import java.util.*;

public class Args {
    private String topText = "";
    private String bottomText = ""; //lol
    private String CATegory = "";

    private static Dictionary<String, Integer> categories;

    public Args(String cmd) {
        // it's a surprise tool that will help us later
        categories = new Hashtable<>();
        categories.put("-boxes", 5);
        categories.put("-clothes", 15);
        categories.put("-hats", 1);
        categories.put("-sinks", 14);
        categories.put("-space", 2);
        categories.put("-sunglasses", 4);
        categories.put("-ties", 7);

        String[] cmdSplit = cmd.split("\\s+");
        String[] args = Arrays.copyOfRange(cmdSplit, 1, cmdSplit.length);

        if (args.length == 0) return;

        Integer top = indexOf(args, "-top");
        Integer bot = indexOf(args, "-bottom");

        if (top != null && bot != null) {
            top++; bot++;
            bottomText = arrToString(args, bot, top > bot ? top - 1 : args.length);
            topText = arrToString(args, top, top < bot ? bot - 1: args.length);
            CATegory = generateCategoryString(args, top < bot ? top : bot);
        } else if (top != null) {
            top++;
            topText = arrToString(args, top, args.length);
            CATegory = generateCategoryString(args, top);
        } else if (bot != null) {
            bot++;
            bottomText = arrToString(args, bot, args.length);
            CATegory = generateCategoryString(args, bot);
        } else {
            int i = 0;
            while (args[i].startsWith("-")) i++;
            CATegory = generateCategoryString(args, i);
            bottomText = arrToString(args, i);
        }
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String t) { topText = t; }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String t) { bottomText = t; }

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

    private String arrToString(String[] arr, int indexFrom) {
        return arrToString(arr, indexFrom, arr.length);
    }

    private String arrToString(String[] arr) {
        return arrToString(arr, 0, arr.length);
    }

    private String generateCategoryString(String[] arr, int limit) {
        StringBuilder c = new StringBuilder("?category_ids=");

        Integer cid;
        for (int i = 0; i < limit; i++) {
            cid = categories.get(arr[i]);
            if (cid != null)
                c.append(cid.toString())
                        .append(",");
        }

        return c.toString();
    }

    private String generateCategoryString(String[] arr) {
        return generateCategoryString(arr, arr.length);
    }
}
