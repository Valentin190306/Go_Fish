package ar.edu.unlu.poo.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class FishermanButton extends JButton {
    private ImageIcon normalIcon;
    private ImageIcon hoverIcon;

    public FishermanButton(String imagePath, int x, int y, int width, int height) {
        try {
            BufferedImage original = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
            Image scaledImage = original.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

            normalIcon = new ImageIcon(scaledImage);
            hoverIcon = new ImageIcon(scaledImage.getScaledInstance((int) (width * 1.6), (int) (height * 1.6), Image.SCALE_SMOOTH));

            setIcon(normalIcon);
            Dimension size = new Dimension((int) (width * 1.4), (int) (height * 1.4));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);

            setMargin(new Insets(0, 0, 0, 0));
            setBorderPainted(true);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);

            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setIcon(hoverIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setIcon(normalIcon);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
