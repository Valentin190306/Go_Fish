package ar.edu.unlu.poo.model;

import java.io.*;

public class GameSerializer {
    private static String filePath = null;

    public String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        GameSerializer.filePath = filePath;
    }

    public void serialize(Go_Fish match) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(match);
        }
    }

    public Go_Fish deserialize() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Go_Fish) ois.readObject();
        }
    }
}
