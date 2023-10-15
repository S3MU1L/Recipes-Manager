package cz.fi.muni.pv168.easyfood.ui;

public class Utility {
    public static int parseIntFromString(String string) {
        int result = 0;

        if (!string.isEmpty()) {
            try {
                result = Integer.parseInt(string);
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }

    public static double parseDoubleFromString(String string) {
        double result = 0.0;

        if (!string.isEmpty()) {
            try {
                result = Integer.parseInt(string);
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
}
