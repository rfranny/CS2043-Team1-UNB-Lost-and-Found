import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LostItemSpike {

    static final Path FILE = Paths.get("lost_items.csv");

    record LostItemReport(String id, String itemName, String description,
                          String locationLost, String dateLost, String contactEmail) {

        String toCsv() {
            return String.join(",", esc(id), esc(itemName), esc(description),
                    esc(locationLost), esc(dateLost), esc(contactEmail));
        }

        static LostItemReport fromCsv(String line) {
            List<String> f = parse(line);
            return new LostItemReport(f.get(0), f.get(1), f.get(2), f.get(3), f.get(4), f.get(5));
        }
    }

    // Wrap in quotes and double any quotes so commas/quotes in text don't break the CSV
    static String esc(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    // Minimal CSV line parser that handles quoted fields
    static List<String> parse(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else if (c == '"') {
                    inQuotes = false;
                } else {
                    cur.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }

    static void save(List<LostItemReport> reports) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,itemName,description,locationLost,dateLost,contactEmail");
        for (LostItemReport r : reports) lines.add(r.toCsv());
        Files.write(FILE, lines);
    }

    static List<LostItemReport> load() throws IOException {
        List<LostItemReport> reports = new ArrayList<>();
        List<String> lines = Files.readAllLines(FILE);
        for (int i = 1; i < lines.size(); i++) { // skip header
            if (!lines.get(i).isBlank()) reports.add(LostItemReport.fromCsv(lines.get(i)));
        }
        return reports;
    }

    static void print(List<LostItemReport> reports) {
        for (LostItemReport r : reports) {
            System.out.println("[" + r.id() + "] " + r.itemName() + " - " + r.description());
            System.out.println("     Lost at: " + r.locationLost() + " on " + r.dateLost());
            System.out.println("     Contact: " + r.contactEmail());
        }
    }

    public static void main(String[] args) throws IOException {
        String mode = args.length > 0 ? args[0] : "save";

        if (mode.equals("save")) {
            List<LostItemReport> reports = List.of(
                new LostItemReport("1", "Black Backpack",
                        "Black Jansport, has a laptop, \"UNB\" sticker", "Head Hall, Room 12",
                        "2026-11-05", "student@unb.ca"),
                new LostItemReport("2", "Student Card",
                        "Blue wallet, contains card and bus pass", "Harriet Irving Library",
                        "2026-11-06", "other@unb.ca"));
            save(reports);
            System.out.println("Saved " + reports.size() + " report(s) to " + FILE.toAbsolutePath());
        } else if (mode.equals("load")) {
            if (!Files.exists(FILE)) {
                System.out.println("No saved file found.");
                return;
            }
            List<LostItemReport> reports = load();
            System.out.println("Loaded " + reports.size() + " report(s):");
            print(reports);
        } else {
            System.out.println("Usage: java LostItemSpike [save|load]");
        }
    }
}
