package Helper;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CopyCat {

    public static void runCopyCat() {
        String srcFolder = "src";
        Path srcPath = Paths.get(srcFolder);

        if (!Files.isDirectory(srcPath)) {
            System.out.println("Der angegebene Pfad ist kein Verzeichnis: " + srcFolder);
            return;
        }

        try {
            // Alle .java-Dateien finden und konvertieren
            List<Path> txtFiles = Files.walk(srcPath)
                 .filter(p -> p.toString().endsWith(".java"))
                 .map(CopyCat::convertJavaToText)
                 .collect(Collectors.toList());

            // Projektwurzel über src
            Path projectRoot = srcPath.toAbsolutePath().getParent();
            if (projectRoot == null) {
                System.out.println("Kein übergeordneter Ordner gefunden.");
                return;
            }

            // Zielort: direkt im Projektordner (MitgliedermanagementKonsole)
            Path targetFolder = projectRoot; // Kein Unterordner, nur der Projektordner

            // Prüfe, ob eine bestehende Datei existiert und finde die höchste Seriennummer
            int nextSerial = 1;
            Pattern pattern = Pattern.compile("combined_java_texts(?:_(\\d+))?\\.txt");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetFolder, "combined_java_texts*.txt")) {
                for (Path file : stream) {
                    String name = file.getFileName().toString();
                    Matcher matcher = pattern.matcher(name);
                    if (matcher.matches()) {
                        String numStr = matcher.group(1);
                        if (numStr != null) {
                            int num = Integer.parseInt(numStr);
                            if (num >= nextSerial) {
                                nextSerial = num + 1;
                            }
                        }
                    }
                }
            }

            // Name der neuen Datei
            String newFileName = "combined_java_texts" + (nextSerial > 1 ? "_" + nextSerial : "") + ".txt";
            Path combinedFile = targetFolder.resolve(newFileName);

            // Alle .txt Dateien zusammenfügen
            try (BufferedWriter writer = Files.newBufferedWriter(combinedFile)) {
                for (Path txtFile : txtFiles) {
                    if (txtFile != null && Files.exists(txtFile)) {
                        List<String> lines = Files.readAllLines(txtFile);
                        writer.write("----- Inhalt von: " + txtFile.getFileName() + " -----");
                        writer.newLine();
                        for (String line : lines) {
                            writer.write(line);
                            writer.newLine();
                        }
                        writer.newLine();
                    }
                }
            }

            System.out.println("Kombinierte Textdatei erstellt: " + combinedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path convertJavaToText(Path javaFilePath) {
        try {
            String content = Files.readString(javaFilePath);
            Path txtFilePath = Paths.get(javaFilePath.toString().replaceAll("\\.java$", ".txt"));
            Files.writeString(txtFilePath, content);
            System.out.println("Konvertiert: " + javaFilePath + " -> " + txtFilePath);
            return txtFilePath;
        } catch (IOException e) {
            System.err.println("Fehler beim Verarbeiten der Datei: " + javaFilePath);
            e.printStackTrace();
            return null;
        }
    }
}
