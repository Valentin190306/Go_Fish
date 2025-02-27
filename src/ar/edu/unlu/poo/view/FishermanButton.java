package ar.edu.unlu.poo.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FishermanButton extends JButton {
    private ImageIcon normalIcon;
    private ImageIcon hoverIcon;

    public FishermanButton(String imagePath, int x, int y, int width, int height) {
        try {
            BufferedImage original = ImageIO.read(new File(imagePath));
            BufferedImage cropped = original.getSubimage(x, y, width, height);

            normalIcon = new ImageIcon(cropped);
            hoverIcon = new ImageIcon(cropped.getScaledInstance((int) (width * 1.1), (int) (height * 1.1), Image.SCALE_SMOOTH));

            setIcon(normalIcon);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);

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
