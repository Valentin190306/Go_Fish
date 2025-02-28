package ar.edu.unlu.poo.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class FishermanButton extends JPanel {
    private JLabel nameLabel;
    private String imagePath;
    private JButton button;
    private ImageIcon normalIcon;
    private ImageIcon hoverIcon;

    public FishermanButton(String imagePath, String playerName) {
        this.imagePath = imagePath;
        this.nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            BufferedImage original = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
            Image scaledImage = original.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

            this.normalIcon = new ImageIcon(scaledImage);
            this.hoverIcon = new ImageIcon(scaledImage.getScaledInstance(160, 160, Image.SCALE_SMOOTH));

            button = new JButton(normalIcon);
            Dimension size = new Dimension(140, 140);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);

            button.setMargin(new Insets(0, 0, 0, 0));
            button.setBorderPainted(true);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            setOpaque(false);

            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);

            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(button);
            add(Box.createVerticalStrut(5));
            add(nameLabel);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setIcon(hoverIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setIcon(normalIcon);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void setPlayerName(String nameLabel) {
        this.nameLabel.setName(nameLabel);
    }

    public String getPlayerName() {
        return nameLabel.getName();
    }

    public void setEnable(boolean isEnable) {
        button.setEnabled(isEnable);
    }

    public void isPlaying(boolean isPlaying) {
        if (isPlaying) button.setIcon(hoverIcon);
        else button.setIcon(normalIcon);
    }
}
