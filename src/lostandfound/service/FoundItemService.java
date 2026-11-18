package lostandfound.service;

import java.time.LocalDate;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

public class FoundItemService {

    private final ItemRepository repository;

    public FoundItemService(ItemRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    public ItemReport reportFoundItem(
            String name,
            String description,
            String location,
            LocalDate date) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Item name is required"
            );
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException(
                "Location is required"
            );
        }

        if (date == null) {
            throw new IllegalArgumentException(
                "Date is required"
            );
        }

        int id = repository.getNextId();

        ItemReport report = new ItemReport(
            id,
            name.trim(),
            description == null ? "" : description.trim(),
            location.trim(),
            date,
            ReportType.FOUND
        );

        repository.save(report);

        return report;
    }
}
