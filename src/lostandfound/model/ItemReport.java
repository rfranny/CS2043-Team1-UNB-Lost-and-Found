package lostandfound.model;

import java.time.LocalDate;

public class ItemReport {

    private final int id;
    private final String name;
    private final String description;
    private final String location;
    private final LocalDate date;
    private final ReportType type;

    public ItemReport(int id, String name,
                      String description, String location,
                      LocalDate date, ReportType type) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReportType getType() {
        return type;
    }
}
