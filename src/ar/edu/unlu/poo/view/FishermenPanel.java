package ar.edu.unlu.poo.view;

import javax.swing.*;

public class FishermenPanel extends JPanel {
    private final FishermanButton fisherman1;
    private final FishermanButton fisherman2;
    private final FishermanButton fisherman3;

    public FishermenPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fisherman1 = new FishermanButton("/ar/edu/unlu/poo/view/assets/playerIcons/fisherman1.png", 0, 0, 100, 100);
        fisherman2 = new FishermanButton("/ar/edu/unlu/poo/view/assets/playerIcons/fisherman2.png", 0, 0, 100, 100);
        fisherman3 = new FishermanButton("/ar/edu/unlu/poo/view/assets/playerIcons/fisherman3.png", 0, 0, 100, 100);

        add(Box.createHorizontalGlue());
        add(fisherman1);
        add(Box.createHorizontalGlue());
        add(fisherman2);
        add(Box.createHorizontalGlue());
        add(fisherman3);
        add(Box.createHorizontalGlue());

        setVisible(true);
    }
}

