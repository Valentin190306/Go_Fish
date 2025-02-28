package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.ICard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandPanel extends JPanel {
    private final Map<Integer, ICard> uniqueCards;
    private final Map<Integer, Integer> cardCount;
    private final Map<Integer, CardButton> cardButtons;
    private final Map<Integer, JLabel> counters;

    public HandPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setOpaque(false);
        uniqueCards = new HashMap<>();
        cardCount = new HashMap<>();
        cardButtons = new HashMap<>();
        counters = new HashMap<>();
    }

    public void updateHand(ArrayList<ICard> hand) {
        removeAll();
        uniqueCards.clear();
        cardCount.clear();
        cardButtons.clear();
        counters.clear();

        for (ICard card : hand) {
            int rank = card.getNumber().ordinal();
            if (!uniqueCards.containsKey(rank)) {
                uniqueCards.put(rank, card);
            }
            cardCount.put(rank, cardCount.getOrDefault(rank, 0) + 1);
        }

        for (Map.Entry<Integer, ICard> entry : uniqueCards.entrySet()) {
            int number = entry.getKey();
            ICard card = entry.getValue();
            int count = cardCount.get(number);

            CardButton button = new CardButton(card);
            button.addActionListener(e -> System.out.println("Carta seleccionada: " + number));

            JLabel counter = new JLabel("x" + count);
            counter.setFont(new Font("Arial", Font.BOLD, 16));
            counter.setForeground(Color.WHITE);

            cardButtons.put(number, button);
            counters.put(number, counter);

            add(button);
            add(counter);
        }

        revalidate();
        repaint();
    }
}

