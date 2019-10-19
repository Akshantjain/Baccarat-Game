import java.util.ArrayList;

public class BaccaratGameLogic {
    String whoWon(ArrayList <Card> hand1, ArrayList <Card> hand2)  {
        if (handTotal(hand1) == handTotal(hand2))
            return "Draw";
        else if (handTotal(hand1) > handTotal(hand2))
            return "Player";
        else if (handTotal(hand2) > handTotal(hand1))
            return "Banker";
        return "Draw";
    }

    int handTotal(ArrayList <Card> hand)  {
        int sum = 0;
        for (Card card : hand){
            if (card.value <= 9 && card.value >= 1)
                sum = sum + card.value;
            else
                sum += 0;
        }
        while (sum > 9) sum = sum - 10;
        return sum;
    }

    boolean evaluateBankerDraw(ArrayList <Card> hand, Card playerCard)    {
        int total = handTotal(hand);
        if (hand.size() == 3)
            return false;
        else if (total <= 2)
            return true;
        else if (total == 3 && (playerCard.value < 8 || playerCard.value == 9))
            return true;
        else if (total == 4 && ((playerCard.value >= 2 && (playerCard.value <= 7)) || playerCard.value == -1))
            return true;
        else if (total == 5 && ((playerCard.value >= 4 && playerCard.value <= 7) || playerCard.value == -1))
            return true;
        else return total == 6 && playerCard.value >= 6 && playerCard.value <= 7;
    }

    boolean evaluatePlayerDraw(ArrayList <Card> hand) {
        return (handTotal(hand) <= 5) && (hand.size() <= 2);
    }

    double evaluateEachRoundWinning(double playerBetPrice, double bankerBetPrice, double tieBetPrice) {
        if (playerBetPrice > 0)    {
            return (playerBetPrice) + (bankerBetPrice) + tieBetPrice;
        }
        else if (bankerBetPrice > 0)   {
            return (bankerBetPrice)- (bankerBetPrice * 0.05) + playerBetPrice + tieBetPrice;
        }
        else if (tieBetPrice > 0)  {
            return (tieBetPrice * 8) + playerBetPrice + bankerBetPrice;
        }
        else
            return 0;
        //return (playerBetPrice + playerBetPrice) + (bankerBetPrice + bankerBetPrice) - (bankerBetPrice * 0.05) + (tieBetPrice * 9);
    }
}
