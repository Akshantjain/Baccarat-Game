public class Card {
    String suite;
    String fullName;
    int value;

    Card(String theSuite, int theValue) {
        suite = theSuite;
        value = theValue;
    }

    String getImageString() {
        return fullName + ".png";
    }
}