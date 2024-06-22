package game.logic;


public final class TreasureManager {
    private final String[] chosenTreasures;

    public static final String[] treasureChoices = new String[]{
            "Bag of Gold Coins", "Bat", "Book with Clasp", "Dragon", "Owl", "Rat",
            "Ghost (in bottle)", "Ghost (waving)", "Gold Crown", "Gold Menorah",
            "Gold Ring", "Helmet (armor)", "Jewel", "Lady Pig", "Lizard", "Moth",
            "Scarab", "Set of Keys", "Skull", "Sorceress", "Spider on Web",
            "Sword", "Treasure Chest", "Treasure Map"
    };

    public TreasureManager(String[] treasureChoices) {
        if (treasureChoices != null && treasureChoices.length > 0) {
            chosenTreasures = treasureChoices;
        } else {
            chosenTreasures = TreasureManager.treasureChoices;
        }
    }


    public String getRandom() {
        return chosenTreasures[LabyrinthService.random.nextInt(getCount())];
    }

    public int getCount() {
        return chosenTreasures.length;
    }

    public String[] getChosenTreasures() {
        return chosenTreasures;
    }
}