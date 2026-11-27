package lostandfound.service;

import java.time.LocalDate;

import lostandfound.model.ItemReport;
import lostandfound.model.ItemCategory;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

public class LostItemService {

    private final ItemRepository repository;

    public LostItemService(ItemRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    public ItemReport reportLostItem(
            String name,
            String description,
            String location,
            LocalDate date) {
        return reportLostItem(name, description, location, date, ItemCategory.OTHER);
    }

    public ItemReport reportLostItem(
            String name,
            String description,
            String location,
            LocalDate date,
            ItemCategory category) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name is required");
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }

        int id = repository.getNextId();

        ItemReport report = new ItemReport(
            id,
            name.trim(),
            description == null ? "" : description.trim(),
            location.trim(),
            date,
            ReportType.LOST,
            category
        );

        repository.save(report);
        return report;
    }
    
}
