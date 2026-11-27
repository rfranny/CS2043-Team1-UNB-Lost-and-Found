package lostandfound.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

class BrowseReportsServiceTest {

    private ItemRepository repository;
    private BrowseReportsService service;

    @BeforeEach
    void setUp() {
        repository = new ItemRepository();
        service = new BrowseReportsService(repository);
    }

    private ItemReport addReport(String name, ReportType type) {
        ItemReport report = new ItemReport(
            repository.getNextId(),
            name,
            name + " description",
            "Head Hall",
            LocalDate.of(2026, 11, 17),
            type
        );

        repository.save(report);
        return report;
    }

    @Test
    void emptyRepositoryReturnsNoReports() {
        assertTrue(service.getAllReports().isEmpty());
        assertTrue(service.getReportsByType(ReportType.LOST).isEmpty());
    }

    @Test
    void multipleReportsAreListedInIdOrder() {
        addReport("Keys", ReportType.LOST);
        addReport("Phone", ReportType.FOUND);
        addReport("Wallet", ReportType.LOST);

        List<ItemReport> reports = service.getAllReports();

        assertEquals(3, reports.size());
        assertEquals("Keys", reports.get(0).getName());
        assertEquals("Phone", reports.get(1).getName());
        assertEquals("Wallet", reports.get(2).getName());
    }

    @Test
    void lostAndFoundReportsBothAppear() {
        addReport("Backpack", ReportType.LOST);
        addReport("Umbrella", ReportType.FOUND);

        List<ItemReport> reports = service.getAllReports();

        assertTrue(reports.stream()
            .anyMatch(r -> r.getType() == ReportType.LOST));
        assertTrue(reports.stream()
            .anyMatch(r -> r.getType() == ReportType.FOUND));
    }

    @Test
    void filterByTypeReturnsOnlyThatType() {
        addReport("Backpack", ReportType.LOST);
        addReport("Umbrella", ReportType.FOUND);
        addReport("Laptop", ReportType.FOUND);

        List<ItemReport> found = service.getReportsByType(ReportType.FOUND);

        assertEquals(2, found.size());
        assertTrue(found.stream()
            .allMatch(r -> r.getType() == ReportType.FOUND));
        assertEquals(3, service.getReportsByType(null).size());
    }

    @Test
    void newReportsAppearOnNextRead() {
        addReport("Keys", ReportType.LOST);
        assertEquals(1, service.getAllReports().size());

        addReport("Phone", ReportType.FOUND);
        assertEquals(2, service.getAllReports().size());
    }

    @Test
    void getReportReturnsMatchingReport() {
        ItemReport saved = addReport("Wallet", ReportType.LOST);

        assertSame(saved, service.getReport(saved.getId()));
        assertNull(service.getReport(99));
    }

    @Test
    void nullRepositoryIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new BrowseReportsService(null)
        );
    }
    
    @Test
    void searchMatchesKeywordWithinSelectedType() {
        addReport("Black keys", ReportType.LOST);
        ItemReport foundKeys = addReport("Silver keys", ReportType.FOUND);
        addReport("Wallet", ReportType.FOUND);

        List<ItemReport> results = service.searchReports("  KEYS  ", ReportType.FOUND);

        assertEquals(List.of(foundKeys), results);
    }
}
