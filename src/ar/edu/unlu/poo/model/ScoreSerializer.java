package ar.edu.unlu.poo.model;

import java.io.*;
import java.util.*;

public class ScoreSerializer {
    static String filePath = null;

    public static void serialize(HashMap<String, Integer> scores) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(scores);
        }
    }

    public static LinkedHashMap<String, Integer> deserialize() throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (LinkedHashMap<String, Integer>) ois.readObject();
        }
    }

    public static LinkedHashMap<String, Integer> sortScores(LinkedHashMap<String, Integer> scores) {
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
            // Si hay un error (si el archivo no existe), grabamos un mapa vacío
        }
        highScores.putAll(scores);
        serialize(highScores);
    }
}