package ar.edu.unlu.poo.view.graphicViewPanels;

import ar.edu.unlu.poo.interfaces.ICard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardButton extends JButton {
    public CardButton(ICard card) {
        String imagePath = "/ar/edu/unlu/poo/view/assets/cards/" + card.getNumber() + "-" + card.getSuit() + ".png";
        setIcon(resizeIcon(imagePath, 50, 70));
        setPreferredSize(new Dimension(50, 70));
        setBorder(BorderFactory.createRaisedBevelBorder());
        setContentAreaFilled(false);
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
