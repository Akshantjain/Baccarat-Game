import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DealerTest {

	private static BaccaratDealer dealer;
	private static BaccaratGameLogic logic;

	@BeforeEach
	void initialise()	{
		dealer = new BaccaratDealer();
		logic = new BaccaratGameLogic();
	}

	@Test
	void DealerConstructor()	{
		assertEquals("BaccaratDealer", dealer.getClass().getName(), "Incorrect Class Name");
	}

	@Test
	void DealerGenerateDeck1()	{
		dealer.generateDeck();
		//ArrayList<Card> cards = dealer.deck;
		assertEquals(52, dealer.deckSize(), "Incorrect Generate Deck size");
	}

	@Test
	void DealerGenerateDeck2()	{
		dealer.generateDeck();
		ArrayList<Card> cards = dealer.deck;
		assertEquals("CLUBS", cards.get(0).suite, "Incorrect Generate Deck size");
	}

	@Test
	void dealHand1()	{
		ArrayList<Card> deal = dealer.dealHand();
		assertEquals(2, deal.size(), "Incorrect dealing");
	}

	@Test
	void dealHand2()	{
		ArrayList<Card> deal = dealer.dealHand();
		assertEquals(2, deal.size(), "Incorrect dealing");
		assertEquals(50, dealer.deckSize(), "Incorrect reduction after dealing");
	}

	@Test
	void drawOne1()	{
		ArrayList<Card> deal = dealer.dealHand();
		deal.add(dealer.drawOne());
		assertEquals(3, deal.size(), "Incorrect dealing");
	}

	@Test
	void drawOne2()	{
		ArrayList<Card> deal = dealer.dealHand();
		deal.add(dealer.drawOne());
		assertEquals(3, deal.size(), "Incorrect dealing");
		assertEquals(49, dealer.deckSize(), "Incorrect reduction after dealing");
	}

	@Test
	void shuffleDeck1()	{
		dealer.shuffleDeck();
		assertNotEquals("ACE", dealer.deck.get(0).value, "Incorrect Shuffling: First Card");
	}

	@Test
	void shuffleDeck2()	{
		dealer.shuffleDeck();
		assertNotEquals("KING", dealer.deck.get(51).value, "Incorrect Shuffling: Last Card");
		//assertNotEquals("DIAMONDS", dealer.deck.get(51).suite, "Incorrect Shuffling: Last Card");
	}

	@Test
	void deckSize1()	{
		assertEquals(52, dealer.deckSize(), "Incorrect Deck Size");
	}

	@Test
	void deckSize2()	{
		Card card = dealer.drawOne();
		assertEquals(51, dealer.deckSize(), "Incorrect Deck Size");
	}

	@Test
	void dealerConstructor()	{
		assertEquals("BaccaratGameLogic", logic.getClass().getName(), "Incorrect Logic Constructor");
	}

	@Test
	void whoWon1()	{
		Card card1 = new Card("CLUBS", 10);
		Card card2 = new Card("DIAMOND", 11);
		Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand1 = new ArrayList <>();
		hand1.add(card1);
		hand1.add(card2);
		hand1.add(card3);

		Card card4 = new Card("CLUBS", 9);
		Card card5 = new Card("DIAMOND", 8);
		Card card6 = new Card("HEARTS", 10);

		ArrayList<Card> hand2 = new ArrayList <>();
		hand2.add(card4);
		hand2.add(card5);
		hand2.add(card6);

		assertEquals("Player", logic.whoWon(hand1, hand2), "Incorrect Win Logic");
	}

	@Test
	void whoWon2()	{
		Card card1 = new Card("CLUBS", 10);
		Card card2 = new Card("DIAMOND", 11);
		Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand1 = new ArrayList <>();
		hand1.add(card1);
		hand1.add(card2);
		hand1.add(card3);

		Card card4 = new Card("CLUBS", 9);
		Card card5 = new Card("DIAMOND", 8);
		Card card6 = new Card("HEARTS", 10);

		ArrayList<Card> hand2 = new ArrayList <>();
		hand2.add(card4);
		hand2.add(card5);
		hand2.add(card6);

		assertEquals("Banker", logic.whoWon(hand2, hand1), "Incorrect Win Logic");
	}

	@Test
	void handTotal1()	{
		Card card1 = new Card("CLUBS", 10);
		Card card2 = new Card("DIAMOND", 11);
		Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);

		assertEquals(9, logic.handTotal(hand), "Incorrect Total");
	}

	@Test
	void handTotal2()	{
		Card card1 = new Card("CLUBS", 9);
		Card card2 = new Card("DIAMOND", 8);
		Card card3 = new Card("HEARTS", 10);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);

		assertEquals(7, logic.handTotal(hand), "Incorrect Total");
	}

	@Test
	void bankDraw1()	{
		Card card1 = new Card("CLUBS", 10);
		Card card2 = new Card("DIAMOND", 11);
		//Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		//hand.add(card3);

		assertTrue(logic.evaluateBankerDraw(hand, new Card("DIAMOND", 2)));
	}

	@Test
	void bankDraw2()	{
		Card card1 = new Card("CLUBS", 10);
		Card card2 = new Card("DIAMOND", 6);
		//Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		//hand.add(card3);

		assertFalse(logic.evaluateBankerDraw(hand, new Card("SPADE", 2)));
	}

	@Test
	void playerDraw1()	{
		Card card1 = new Card("CLUBS", 10);
		Card card2 = new Card("DIAMOND", 1);
		//Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		hand.add(card2);
		//hand.add(card3);

		assertTrue(logic.evaluatePlayerDraw(hand), "Incorrect Player Draw Logic");
	}

	@Test
	void playerDraw2()	{
		Card card1 = new Card("CLUBS", 7);
		//Card card2 = new Card("DIAMOND", 11);
		Card card3 = new Card("SPADES", 9);

		ArrayList<Card> hand = new ArrayList <>();
		hand.add(card1);
		//hand.add(card2);
		hand.add(card3);

		assertFalse(logic.evaluatePlayerDraw(hand), "Incorrect Player Draw Logic");
	}

	@Test
	void CardConst1()	{
		Card card = new Card("DIAMOND", 2);
		assertEquals("Card", card.getClass().getName(), "Incorrect Card Constructor");
		assertEquals("DIAMOND", card.suite, "Incorrect Card Suite");
		assertEquals(2, card.value, "Incorrect Card Value");
	}
}
