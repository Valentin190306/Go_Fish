package ar.edu.unlu.poo.model;

import java.io.*;
import java.util.*;

public class HighScoreSerializer {
    static String filePath = null;

    public static void serialize(HashMap<String, Integer> scores) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(scores);
        }
    }

    public static HashMap<String, Integer> deserialize() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (HashMap<String, Integer>) ois.readObject();
        }
    }

    public static LinkedHashMap<String, Integer> sortHighScoresManual(HashMap<String, Integer> scores) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(scores.entrySet());

        entries.sort(new Comparator<>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void updateHighScores(Map<String, Integer> scores) throws IOException {
        HashMap<String, Integer> highScores = new HashMap<>();

        try {
            highScores = deserialize();
        } catch (IOException | ClassNotFoundException e) {
            // Si hay un error (por ejemplo, el archivo no existe), seguimos con un mapa vacío
        }
        highScores.putAll(scores);
        serialize(highScores);
    }
}