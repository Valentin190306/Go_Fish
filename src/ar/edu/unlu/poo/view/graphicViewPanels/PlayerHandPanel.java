package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.IGameController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerHandPanel extends JPanel {
    private final IGameController controller;
    private final List<JToggleButton> cardButtons;
    private final JButton requestButton;

    public PlayerHandPanel(IGameController controller) {
        this.controller = controller;
        this.cardButtons = new ArrayList<>();
        this.requestButton = new JButton("Pedir carta");

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        handPanel.setOpaque(false);

        // Panel que contendrá los botones de carta
        JScrollPane scrollPane = new JScrollPane(handPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
        add(requestButton, BorderLayout.EAST);

        // Acción del botón "Pedir carta"
        requestButton.addActionListener(e -> {
            String selectedRank = getSelectedCardRank();
            if (selectedRank != null) {
                controller.handleCardRequest(selectedRank); // Método que deberá existir en el controlador
                clearSelection();
            }
        });
    }

    public void updateHand(List<ImageIcon> cardImages, List<String> cardRanks) {
        JPanel handPanel = (JPanel) ((JScrollPane) getComponent(0)).getViewport().getView();
        handPanel.removeAll();
        cardButtons.clear();

        for (int i = 0; i < cardImages.size(); i++) {
            String rank = cardRanks.get(i);
            ImageIcon image = cardImages.get(i);
            JToggleButton cardButton = new JToggleButton(image);
            cardButton.setActionCommand(rank);
            cardButton.setOpaque(false);
            cardButton.setContentAreaFilled(false);
            cardButton.setBorderPainted(false);
            cardButton.setFocusPainted(false);
            cardButtons.add(cardButton);
            handPanel.add(cardButton);
        }

        handPanel.revalidate();
        handPanel.repaint();
    }

    private String getSelectedCardRank() {
        for (JToggleButton button : cardButtons) {
            if (button.isSelected()) {
                return button.getActionCommand(); // El "rank" de la carta
            }
        }
        return null;
    }

    private void clearSelection() {
        for (JToggleButton button : cardButtons) {
            button.setSelected(false);
        }
    }
}