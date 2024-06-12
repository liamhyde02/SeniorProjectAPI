package helloworld;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class FileSingleton {
    private static FileSingleton instance = null;
    private UUID uuid;
    private ArrayList<File> files;
    private FileSingleton() {
    }

    public static FileSingleton getInstance() {
        if (instance == null) {
            instance = new FileSingleton();
        }
        return instance;
    }

    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
}
