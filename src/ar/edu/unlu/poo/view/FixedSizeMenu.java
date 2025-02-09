package ar.edu.unlu.poo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FixedSizeMenu extends JFrame implements ActionListener {
    private JButton continueGameButton, newGameButton, changeNameButton, changeViewButton;

    public FixedSizeMenu() {
        setTitle("Menú del Juego");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel contenedor de botones con BoxLayout en eje Y
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes

        // Crear botones con tamaño fijo
        Dimension buttonSize = new Dimension(200, 40); // Tamaño fijo para los botones

        continueGameButton = new JButton("Continuar partida");
        continueGameButton.setPreferredSize(buttonSize);

        newGameButton = new JButton("Nueva partida");
        newGameButton.setPreferredSize(buttonSize);

        changeNameButton = new JButton("Cambiar nombre");
        changeNameButton.setPreferredSize(buttonSize);

        changeViewButton = new JButton("Cambiar vista");
        changeViewButton.setPreferredSize(buttonSize);

        // Panel auxiliar para centrar cada botón
        addButtonToPanel(buttonPanel, continueGameButton);
        addButtonToPanel(buttonPanel, newGameButton);
        addButtonToPanel(buttonPanel, changeNameButton);
        addButtonToPanel(buttonPanel, changeViewButton);

        // Agregar el panel de botones al centro
        add(buttonPanel, BorderLayout.CENTER);
    }

    // Método para agregar cada botón dentro de un JPanel para centrarlo
    private void addButtonToPanel(JPanel parent, JButton button) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(button);
        parent.add(wrapper);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        JOptionPane.showMessageDialog(this, "Opción seleccionada: " + source.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FixedSizeMenu frame = new FixedSizeMenu();
            frame.setVisible(true);
        });
    }
}

