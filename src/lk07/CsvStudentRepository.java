package lk07;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvStudentRepository {
    private final Path filePath;

    public CsvStudentRepository(Path filePath) {
        this.filePath = filePath;
    }

    public List<Student> loadAll() throws IOException {
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Berkas tidak ditemukan: " + filePath.toAbsolutePath());
        }

        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> fields = parseCsvLine(line);
                if (fields.size() < 3) {
                    continue;
                }

                students.add(new Student(fields.get(0), fields.get(1), fields.get(2)));
            }
        }

        return students;
    }

    public void saveAll(List<Student> students) throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (Student student : students) {
                String line = toCsvField(student.getNis()) + ","
                        + toCsvField(student.getNama()) + ","
                        + toCsvField(student.getAlamat());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void createEmptyFile() throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    private String toCsvField(String value) {
        String escaped = value == null ? "" : value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        fields.add(current.toString());

        return fields;
    }
}
