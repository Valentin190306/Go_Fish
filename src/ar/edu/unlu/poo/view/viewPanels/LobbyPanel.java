package ar.edu.unlu.poo.view.viewPanels;

import ar.edu.unlu.poo.interfaces.IController;
import ar.edu.unlu.poo.interfaces.IPlayer;
import ar.edu.unlu.poo.model.enums.GameState;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LobbyPanel extends JPanel implements IObservadorRemoto {
    private final IController controller;
    private final DefaultTableModel tableModel;
    private final JButton btnVotePlay;
    //private GameState pl_gamestate = null;

    public LobbyPanel(IController controller) throws RemoteException {
        this.controller = controller;
        this.tableModel = new DefaultTableModel(new Object[]{"Jugador", "Estado"}, 0);
        this.btnVotePlay = new JButton("Votar para Jugar");
        controller.addObserverToModel(this);
        updatePlayerList();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        JLabel title = new JLabel("Sala de espera", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JTable playerTable = new JTable(tableModel);
        playerTable.setBackground(Color.DARK_GRAY);
        playerTable.setForeground(Color.WHITE);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 16));
        playerTable.setEnabled(false);
        add(new JScrollPane(playerTable), BorderLayout.CENTER);

        btnVotePlay.setFont(new Font("Arial", Font.BOLD, 16));
        btnVotePlay.setBackground(new Color(70, 130, 180));
        btnVotePlay.setForeground(Color.WHITE);
        btnVotePlay.addActionListener(e -> voteToStartGame());
        add(btnVotePlay, BorderLayout.SOUTH);
    }

    private void voteToStartGame() {
        try {
            if (controller
                    .fetchClientPlayer()
                    .getPlayerState()
                    .toString()
                    .equals("READY")) return;
            btnVotePlay.setEnabled(false);
            controller.setClientPlayerReady();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updatePlayerList() {
        try {
            ArrayList<IPlayer> players = controller.fetchPlayers();
            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                for (IPlayer player : players) {
                    tableModel.addRow(new Object[]{player.getName(), player.getPlayerState().toString()});
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof GameState gameState) {
            if (gameState == GameState.NEW_STATUS_PLAYER) {
                updatePlayerList();
            }
        }
    }
}