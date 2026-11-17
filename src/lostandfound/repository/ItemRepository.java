package lostandfound.repository;

import java.util.ArrayList;
import java.util.List;

import lostandfound.model.ItemReport;

public class ItemRepository {

    private final List<ItemReport> reports;

    public ItemRepository() {
        reports = new ArrayList<>();
    }

    // Save a report
    public void save(ItemReport report) {
        if (report == null) {
            throw new IllegalArgumentException(
                "Report cannot be null"
            );
        }

        for (ItemReport existing : reports) {
            if (existing.getId() == report.getId()) {
                throw new IllegalArgumentException(
                    "Report ID already exists"
                );
            }
        }

        reports.add(report);
    }

    // Get all reports
    public List<ItemReport> findAll() {
        return new ArrayList<>(reports);
    }

    // Find a report by its ID
    public ItemReport findById(int id) {
        for (ItemReport report : reports) {
            if (report.getId() == id) {
                return report;
            }
        }

        return null;
    }

    // Count reports
    public int count() {
        return reports.size();
    }
    
    public int getNextId() {
        int maxId = 0;

        for (ItemReport report : reports) {
            if (report.getId() > maxId) {
                maxId = report.getId();
            }
        }

        return maxId + 1;
    }
    
}
