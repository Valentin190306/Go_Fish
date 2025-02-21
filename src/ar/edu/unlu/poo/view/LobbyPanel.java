package ar.edu.unlu.poo.view;

import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.interfaces.ILobby;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.PlayerState;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LobbyPanel extends JPanel implements ILobby {
    private final GameWindow gameWindow;
    private final IController controller;
    private final DefaultTableModel tableModel;
    private final JButton btnVotePlay;

    public LobbyPanel(GameWindow gameWindow, IController controller) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        // Título del panel
        JLabel title = new JLabel("Sala de espera", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        // Tabla de jugadores con estado de voto
        tableModel = new DefaultTableModel(new Object[]{"Jugador", "Estado"}, 0);
        JTable playerTable = new JTable(tableModel);
        playerTable.setBackground(new Color(30, 30, 30));
        playerTable.setForeground(Color.WHITE);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 16));
        playerTable.setEnabled(false);
        add(new JScrollPane(playerTable), BorderLayout.CENTER);

        // Botón para votar "Jugar"
        btnVotePlay = new JButton("Votar para Jugar");
        btnVotePlay.setFont(new Font("Arial", Font.BOLD, 16));
        btnVotePlay.setBackground(new Color(70, 130, 180));
        btnVotePlay.setForeground(Color.WHITE);
        btnVotePlay.addActionListener(e -> voteToStartGame());
        add(btnVotePlay, BorderLayout.SOUTH);
    }

    private void voteToStartGame() {
        if (controller.getClientPlayer().toString().equals("READY")) return;
        btnVotePlay.setEnabled(false);
        controller.getClientPlayer().setPlayerState(PlayerState.READY);
    }

    @Override
    public void updatePlayerList(ArrayList<IPlayer> players) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (IPlayer player : players) {
                tableModel.addRow(new Object[]{player.getName(), player.getPlayerState().toString()});
            }
        });
    }

    @Override
    public void switchToGameView() {
        gameWindow.startGame();
    }
}