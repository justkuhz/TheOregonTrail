import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEventGUI extends JDialog {
    private JPanel contentPane;
    private JTextField inputField;
    private JLabel promptLabel;

    private Random rand = new Random();
    private OregonTrailGUI game;
    private int happiness;

    private int clothesAmt = 8;
    private int ammoAmt = 2;
    private int foodAmt = 2;
    private int medsAmt = 2;
    private int splintAmt = 5;
    private int toolsAmt = 8;
    private ArrayList<String> nameArrayList = new ArrayList<>(List.of("Felicia","Mia","Kristin","Katrina","Janet",
            "Almudena","Chika","Mary","Nicole","Jessica","Maxine","Stephany","Kendra","Kendall","Kenifer","Elise",
            "Anna","Lizzy","Minnie","Ida","Florence","Martha","Nellie","Lena","Agnes","Candace","Jane","April"));
    private ArrayList<String> itemArrayList = new ArrayList<>(List.of("clothes","ammunition","food","medicine",
            "splints","tools"));

    private int tradeAmt;
    private String tradeItem;

    private int tradeGiveAmt;


    public RandomEventGUI(OregonTrailGUI game) {
        this.game = game;
        this.happiness = game.getHappiness();

        setContentPane(contentPane);
        setModal(true);


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

/*
    public static void main(String[] args) {
        RandomEventGUI dialog = new RandomEventGUI(game);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

 */


    int eventChance(int happiness) {
        if (happiness < 75 && happiness > 25) {
            return 1; //1=neutral
        } else if (happiness >= 75) {
            return 2; //2 is happy
        } else {
            return 0; //0 is sad
        }
    }

    boolean eventType(int mood) {
        int temp;
        boolean isGood;
        if (mood == 1) {
            temp = rand.nextInt(2); //0 is bad 1 is good
            if (temp == 1) {
                isGood = true;
            } else {
                isGood = false;
            }
        } else if (mood == 2) {
            temp = rand.nextInt(4);
            if (temp == 0) {
                isGood = false;
            } else {
                isGood = true;
            }
        } else {
            temp = rand.nextInt(4);
            if (temp == 0) {
                isGood = true;
            } else {
                isGood = false;
            }
        }
        return isGood;
    }

    String eventName() {
        boolean isGood = eventType(eventChance(happiness));
        int temp;
        if (isGood) {
            temp = rand.nextInt(3);
            if (temp == 0) {
                return "encounterTraveler"; //player encounters another traveler
            } else if (temp == 1) {
                return "smallStream"; //player encounters
            } else {
                return "wagonFound"; //found abandoned wagon
            }
        } else {
            temp = rand.nextInt(4);
            if (temp == 0) {
                return "injury"; //random party member is injured
            } else if (temp == 1) {
                return "wagonDamage"; //wagon is damaged during the travels
            } else if (temp == 2) {
                return "foodSpoiled"; //some food spoils
            } else {
                return "illness"; //random party member falls ill
            }
        }
    }

    int randName1;
    int randName2;
    private void genNames() {
        randName1 = rand.nextInt(nameArrayList.size());
        randName2 = rand.nextInt(nameArrayList.size());
        if (randName2==randName1) {
            genNames();
        }
    }
    public void encounterTraveler() {
        genNames();
        promptLabel.setText("You encounter travelers " + nameArrayList.get(randName1) + " and " +
                nameArrayList.get(randName2) + " on your travels. What would you like to do?\nT=Trade S=Share Stories");
        inputField.addActionListener(encounterAL);
    }

    String traderItem = itemArrayList.get(rand.nextInt(5));
    int halfOff=99;
    private void trade(int step) {
        if (step==1) {
            inputField.removeActionListener(encounterAL);
            promptLabel.setText("What would you like to trade? Enter it in the format 'Number' 'Item' (E.g., 5 F for 5 Food)");
            inputField.addActionListener(tradeALWant);
        }
        else if (step==2) {
            inputField.removeActionListener(tradeALWant);
            if (tradeItem.equalsIgnoreCase("F")) {halfOff = foodAmt/2;}
            else if (tradeItem.equalsIgnoreCase("A")) {halfOff = ammoAmt/2;}
            else if (tradeItem.equalsIgnoreCase("M")) {halfOff = medsAmt/2;}
            else if (tradeItem.equalsIgnoreCase("C")) {halfOff = clothesAmt/2;}
            else if (tradeItem.equalsIgnoreCase("S")) {halfOff = splintAmt/2;}
            else if (tradeItem.equalsIgnoreCase("T")) {halfOff = toolsAmt/2;}
            promptLabel.setText(nameArrayList.get(randName1) + " and " + nameArrayList.get(randName2) + " want at least $" +
                    halfOff + " worth of " + traderItem + ". Would you like to trade?\nY=Yes N=No (Abandon Trade)");
            inputField.addActionListener(tradeAbandonNo);
        }
        else if (step==3) {
            inputField.removeActionListener(tradeAbandonNo);
            promptLabel.setText("You must trade at least $"+halfOff+" worth of " + traderItem+ ". How much would you" +
                    " like to trade?");
            inputField.addActionListener(tradeALgive);
        }
        else if (step==4) {
            inputField.removeActionListener(tradeALgive);
            promptLabel.setText("Yahoo, you earned " + tradeAmt + " " + tradeItem + "!");
            if (tradeItem.equalsIgnoreCase("F")) {game.setFood(game.getFood()+tradeAmt);}
            else if (tradeItem.equalsIgnoreCase("A")) {game.setAmmunition(game.getAmmunition()+tradeAmt);}
            else if (tradeItem.equalsIgnoreCase("M")) {game.setMedicine(game.getMedicine()+tradeAmt);}
            else if (tradeItem.equalsIgnoreCase("C")) {game.setClothes(game.getClothes()+tradeAmt);}
            else if (tradeItem.equalsIgnoreCase("S")) {game.setSplints(game.getSplints()+tradeAmt);}
            else if (tradeItem.equalsIgnoreCase("T")) {game.setWagonTools(game.getWagonTools()+tradeAmt);}
        }





    }

    private final ActionListener encounterAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inputField.getText().equalsIgnoreCase("T")) {
                trade(1);
            }
            else if (inputField.getText().equalsIgnoreCase("S")) {
                shareStories();
            }
        }
    };


    private final ActionListener tradeALWant = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: Check for valid input
            try {
                tradeAmt = Integer.parseInt(inputField.getText().substring(0,2));
                tradeItem = inputField.getText().substring(3,4);
                trade(2);
            }
            catch (Exception oops) {
                JOptionPane.showMessageDialog(null,"Sorry, you couldn't be understood. Please try " +
                        "again, using the correct format.","Woops",JOptionPane.ERROR_MESSAGE);
                System.out.println("RandomEventGUI.java: Something went wrong. Let's take a peek at the error: " + oops);
            }
        }
    };

    private final ActionListener tradeAbandonNo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inputField.getText().equalsIgnoreCase("Y")) {
                trade(3);
            }
            else {
                //TODO: Exit Trade;
            }
        }
    };

    private final ActionListener tradeALgive = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: Check for valid input
            try {
                int price=0;
                tradeGiveAmt = Integer.parseInt(inputField.getText().substring(0,2));
                if (traderItem.equalsIgnoreCase("clothes")) {price = clothesAmt;}
                else if (traderItem.equalsIgnoreCase("ammunition")) {price = ammoAmt;}
                else if (traderItem.equalsIgnoreCase("food")) {price = foodAmt;}
                else if (traderItem.equalsIgnoreCase("medicine")) {price = medsAmt;}
                else if (tradeItem.equalsIgnoreCase("splints")) {price = splintAmt;}
                else if (tradeItem.equalsIgnoreCase("tools")) {price = toolsAmt;}
                if (tradeGiveAmt*price<halfOff){
                    trade(4);
                }
                else {
                    JOptionPane.showMessageDialog(null,"You need to trade at least $" +
                            halfOff + " worth of " + traderItem + ", but you only traded " + tradeGiveAmt*price+". Please " +
                            "trade the required amount.");
                    trade(3);
                }


            }
            catch (Exception oops) {
                JOptionPane.showMessageDialog(null,"Sorry, you couldn't be understood. Please try " +
                        "again, using the correct format.","Woops",JOptionPane.ERROR_MESSAGE);
                System.out.println("RandomEventGUI.java: Something went wrong. Let's take a peek at the error: " + oops);
            }

        }
    };

    private void shareStories() {
        JOptionPane.showMessageDialog(null,"You had a jolly old time sharing stories and earned 10 happiness!","Share Stories",JOptionPane.PLAIN_MESSAGE);
        game.setHappiness(game.calculateHappiness(10));
    }


    void doEvent() {
        ArrayList<Character> characterArrayList = game.getCharacterArrayList();
        int characterIndex = rand.nextInt(4);
        if (eventName().equals("encounterTraveler")) {
            inputField.addActionListener(encounterAL);
            encounterTraveler();
        }
        else if (eventName().equals("injury")) {
            if (!characterArrayList.get(characterIndex).isInjured()) {
                characterArrayList.get(characterIndex).setInjured(true);
            } //No else cuz it just doesn't happen
        }
    }
}
