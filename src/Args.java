import java.util.*;

public class Args {
    private String topText = "";
    private String bottomText = ""; //lol
    private Integer CATegory;

    private static Dictionary<String, Integer> categories;

    public Args(String cmd) throws TooManyCategoriesException {
        // it's a surprise tool that will help us later
        categories.put("-boxes", 5);
        categories.put("-clothes", 15);
        categories.put("-hats", 1);
        categories.put("-sinks", 14);
        categories.put("-space", 2);
        categories.put("-sunglasses", 4);
        categories.put("-ties", 7);

        String[] args = cmd.split(" ");

        Integer top = indexOf(args, "-top");
        Integer bot = indexOf(args, "-bottom");

        if (top != null && bot != null) {
            bottomText = arrToString(args, bot, top > bot ? top : args.length);
            topText = arrToString(args, top, top < bot ? bot : args.length);
            CATegory = findCategory(args, top < bot ? top : bot);
        } else if (top != null) {
            topText = arrToString(args, top);
            CATegory = findCategory(args, top);
        } else if (bot != null) {
            bottomText = arrToString(args, bot);
            CATegory = findCategory(args, bot);
        } else {
            int i = 0;
            while (args[i].startsWith("-")) i++;
            CATegory = findCategory(args, i);
            bottomText = arrToString(args, i);
        }
    }

    public String getTopText() {
        return topText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public Integer getCategory() {
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

    private Integer findCategory(String[] arr, int limit) throws TooManyCategoriesException {
        HashSet<String> cEnum = (HashSet<String>) categories.keys();
        String c = null;
        for (int i = 0; i < limit; i++) {
            if (cEnum.contains(arr[i]))
                if (c == null) c = arr[i];
                else throw new TooManyCategoriesException();
        }
        return categories.get(c);
    }

    private Integer findCategory(String[] arr) throws TooManyCategoriesException {
        return findCategory(arr, arr.length);
    }
}
