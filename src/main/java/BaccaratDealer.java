import java.util.ArrayList;
import java.util.Collections;

public class BaccaratDealer {
    ArrayList<Card> deck;

    BaccaratDealer()    {
        generateDeck();
        shuffleDeck();
    }

    ArrayList<Card> getDeck()   {
        return deck;
    }

    public void generateDeck()  {
        try {
            deck.clear();
        } catch (Exception ignore)  {}

        deck = new ArrayList<>();
        String suite;
        String Name;
        for (int suiteType = 1; suiteType <= 4; ++suiteType) {

            if (suiteType == 1)
                suite = "CLUBS";
            else if (suiteType == 2)
                suite = "HEARTS";
            else if (suiteType == 3)
                suite = "SPADES";
            else suite = "DIAMONDS";

            for (int value = 1; value <= 13; ++ value)  {
                if (value == 1)
                    Name = "ACE";
                else if (value == 2)
                    Name = "TWO";
                else if (value == 3)
                    Name = "THREE";
                else if (value == 4)
                    Name = "FOUR";
                else if (value == 5)
                    Name = "FIVE";
                else if (value == 6)
                    Name = "SIX";
                else if (value == 7)
                    Name = "SEVEN";
                else if (value == 8)
                    Name = "EIGHT";
                else if (value == 9)
                    Name = "NINE";
                else if (value == 10)
                    Name = "TEN";
                else if (value == 11)
                    Name = "JACK";
                else if (value == 12)
                    Name = "QUEEN";
                else Name = "KING";
                Card card = new Card(suite, value);
                card.fullName = Name + "of" + suite;
                deck.add(card);
            }
        }
    }

    public ArrayList<Card> dealHand()   {
        if (deckSize() <= 6)
            generateDeck();
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(deck.get(0));
        hand.add(deck.get(1));
        deck.remove(1);
        deck.remove(0);
        return hand;
    }

    public Card drawOne()    {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    public void shuffleDeck()   {
        Collections.shuffle(deck);
    }

    public int deckSize()   {
        return (deck.size());
    }
}
