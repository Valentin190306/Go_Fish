package ar.edu.unlu.poo.view;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FishermenPanel extends JPanel {
    private final FishermanButton fisherman1;
    private final FishermanButton fisherman2;
    private final FishermanButton fisherman3;

    public FishermenPanel() {
        setLayout(null); // Seguimos usando layout absoluto

        fisherman1 = new FishermanButton("fisherman1.png", 0, 0, 100, 100);
        fisherman2 = new FishermanButton("fisherman2.png", 0, 0, 100, 100);
        fisherman3 = new FishermanButton("fisherman3.png", 0, 0, 100, 100);

        add(fisherman1);
        add(fisherman2);
        add(fisherman3);

        // Ajustar botones cuando cambia el tamaño del panel
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons();
            }
        });
    }

    private void repositionButtons() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int buttonSize = Math.min(panelWidth, panelHeight) / 6; // Tamaño relativo

        // Posiciones relativas
        fisherman1.setBounds(panelWidth / 4 - buttonSize / 2, panelHeight / 2 - buttonSize / 2, buttonSize, buttonSize);
        fisherman2.setBounds(panelWidth / 2 - buttonSize / 2, panelHeight / 2 - buttonSize / 2, buttonSize, buttonSize);
        fisherman3.setBounds(3 * panelWidth / 4 - buttonSize / 2, panelHeight / 2 - buttonSize / 2, buttonSize, buttonSize);
    }
}
