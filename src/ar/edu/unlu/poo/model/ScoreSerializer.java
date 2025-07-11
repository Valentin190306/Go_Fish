package ar.edu.unlu.poo.model;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class ScoreSerializer {
    private static final String FILE_PATH = "high_scores.dat";

    private static void serialize(HashMap<String, Integer> scores) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(scores);
        }
    }

    @SuppressWarnings("unchecked")
    private static HashMap<String, Integer> deserialize() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, Integer>) ois.readObject();
        }
    }

    public static void updateHighScores(HashMap<String, Integer> newScores) throws RemoteException {
        if (newScores == null || newScores.isEmpty()) {
            return;
        }

        try {
            HashMap<String, Integer> storedScores = deserialize();

            for (var entry : newScores.entrySet()) {
                String name = entry.getKey();
                int newScore = entry.getValue();

                int existingScore = storedScores.getOrDefault(name, 0);
                if (newScore > existingScore) {
                    storedScores.put(name, newScore);
                }
            }

            serialize(storedScores);

        } catch (IOException | ClassNotFoundException e) {
            throw new RemoteException("Error al persistir puntaje");
        }
    }

    public static HashMap<String, Integer> getHighScores() {
        try {
            return deserialize();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }
}