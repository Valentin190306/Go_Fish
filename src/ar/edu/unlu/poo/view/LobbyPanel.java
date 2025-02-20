package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IController;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel extends JPanel {
    private GameWindow gameWindow;
    private IController controller;
    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JButton btnVotePlay;
    private int votes = 0;
    private int requiredVotes = 4;
    private boolean hasVoted = false;

    public LobbyPanel(GameWindow gameWindow, IController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        // Título del panel
        JLabel title = new JLabel("Lobby", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        // Lista de jugadores conectados
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        playerList.setBackground(new Color(30, 30, 30));
        playerList.setForeground(Color.WHITE);
        playerList.setFont(new Font("Arial", Font.PLAIN, 16));
        add(new JScrollPane(playerList), BorderLayout.CENTER);

        // Botón para votar "Jugar"
        btnVotePlay = new JButton("Votar para Jugar");
        btnVotePlay.setFont(new Font("Arial", Font.BOLD, 16));
        btnVotePlay.setBackground(new Color(70, 130, 180));
        btnVotePlay.setForeground(Color.WHITE);
        btnVotePlay.addActionListener(e -> voteToStartGame());
        add(btnVotePlay, BorderLayout.SOUTH);
    }

    // Agregar un nuevo jugador a la lista
    public void addPlayer(String playerName) {
        if (!playerListModel.contains(playerName)) {
            playerListModel.addElement(playerName);
        }
    }

    // Remover un jugador (por desconexión)
    public void removePlayer(String playerName) {
        playerListModel.removeElement(playerName);
    }

    private void voteToStartGame() {
        if (hasVoted) return;

        hasVoted = true;
        btnVotePlay.setText("Votos: " + votes + "/" + requiredVotes);
        btnVotePlay.setEnabled(false);

        if (votes >= requiredVotes) {
            startGame();
        }
    }

    // Acción para iniciar el juego (esto debe integrarse con la lógica del servidor)
    private void startGame() {
        JOptionPane.showMessageDialog(this, "¡El juego va a comenzar!");
        // Aquí podrías notificar al servidor para empezar la partida
    }
}