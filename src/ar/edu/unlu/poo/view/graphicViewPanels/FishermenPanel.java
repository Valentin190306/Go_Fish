package ar.edu.unlu.poo.view.graphicViewPanels;

import javax.swing.*;
import java.util.ArrayList;

public class FishermenPanel extends JPanel {
    private final ArrayList<FishermanButton> buttons;

    public FishermenPanel() {
        this.buttons = new ArrayList<>();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        FishermanButton fisherman1 = new FishermanButton("/ar/edu/unlu/poo/view/assets/playerIcons/fisherman1.png", "1");
        FishermanButton fisherman2 = new FishermanButton("/ar/edu/unlu/poo/view/assets/playerIcons/fisherman2.png", "2");
        FishermanButton fisherman3 = new FishermanButton("/ar/edu/unlu/poo/view/assets/playerIcons/fisherman3.png", "3");

        buttons.add(fisherman1);
        buttons.add(fisherman2);
        buttons.add(fisherman3);

        add(Box.createHorizontalGlue());
        add(fisherman1);
        add(Box.createHorizontalGlue());
        add(fisherman2);
        add(Box.createHorizontalGlue());
        add(fisherman3);
        add(Box.createHorizontalGlue());
    }

    public ArrayList<FishermanButton> getButtons() {
        return buttons;
    }
}

