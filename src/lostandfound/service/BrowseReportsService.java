package lostandfound.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

public class BrowseReportsService {

    private final ItemRepository repository;

    public BrowseReportsService(ItemRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    // All reports, ordered by ID (oldest first)
    public List<ItemReport> getAllReports() {
        List<ItemReport> reports = repository.findAll();
        reports.sort(Comparator.comparingInt(ItemReport::getId));
        return reports;
    }

    // Reports of one type; a null type returns every report
    public List<ItemReport> getReportsByType(ReportType type) {
        if (type == null) {
            return getAllReports();
        }

        List<ItemReport> filtered = new ArrayList<>();

        for (ItemReport report : getAllReports()) {
            if (report.getType() == type) {
                filtered.add(report);
            }
        }

        return filtered;
    }

    public ItemReport getReport(int id) {
        return repository.findById(id);
    }
}
