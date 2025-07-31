package main;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileConstructor {

    private final Path downloadDirectory;

    public FileConstructor() {
        Path path = Paths.get("").toAbsolutePath().getParent();

        Path downloadDirectory = null;
        boolean done = false;
        int i = 0;
        while (!done) {
            downloadDirectory = path.resolve("exercism-java-solutions" + (i == 0 ? "" : " (" + i + ")"));
            if (Files.exists(downloadDirectory)) {
                i++;
            } else {
                try {
                    Files.createDirectory(downloadDirectory);
                    done = true;
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't create a download directory");
                }
            }
        }
        this.downloadDirectory = downloadDirectory;
    }

    public void downloadSolution(String difficulty, String name, List<Map<String, String>> iterations) {
        try {
            Path difficultyDirectory = downloadDirectory.resolve(difficulty);
            if (!Files.exists(difficultyDirectory)) Files.createDirectory(difficultyDirectory);

            Path solutionDirectory = difficultyDirectory.resolve(name);
            Files.createDirectory(solutionDirectory);

            for (int i = 0; i < iterations.size(); i++) {
                Path iterationDirectory = solutionDirectory.resolve("v" + (i + 1));
                Files.createDirectory(iterationDirectory);

                Map<String, String> iteration = iterations.get(i);
                for (Map.Entry<String, String> iterationFile : iteration.entrySet()) {
                    Path file = iterationDirectory.resolve(iterationFile.getKey() + ".java");
                    List<String> lines = Arrays.asList(iterationFile.getValue().split("\n"));
                    Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't download a solution");
        }
    }
}