package ar.edu.unlu.poo.model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ScoreSerializer {
    public static String FILE_PATH;

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

    public static void updateHighScores(HashMap<String, Integer> newScores) throws IOException, ClassNotFoundException {
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
    }

    public static LinkedHashMap<String, Integer> getSortedHighScores() throws IOException {
        HashMap<String, Integer> scores;
        try {
            scores = deserialize();
        } catch (ClassNotFoundException e) {
            return new LinkedHashMap<>();
        }

        return scores.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}