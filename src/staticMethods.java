import javax.swing.*;

public class staticMethods {

    public static int noFoodCounter = 0;
    public static void notValidInput() {
        JOptionPane.showMessageDialog(null, "That is not a valid input",
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public static void resetNFC() {
        noFoodCounter = 0;
    }
}
