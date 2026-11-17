package lostandfound.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;

class ItemRepositoryTest {

    @Test
    void saveReportIncreasesCount() {
        ItemRepository repository = new ItemRepository();

        ItemReport report = new ItemReport(
            1,
            "Wallet",
            "Black leather wallet",
            "Library",
            LocalDate.now(),
            ReportType.LOST
        );

        repository.save(report);

        assertEquals(1, repository.count());
    }

    @Test
    void findReportById() {
        ItemRepository repository = new ItemRepository();

        ItemReport report = new ItemReport(
            1,
            "Keys",
            "Blue keychain",
            "Head Hall",
            LocalDate.now(),
            ReportType.FOUND
        );

        repository.save(report);

        assertEquals(report, repository.findById(1));
    }

    @Test
    void duplicateIdsAreRejected() {
        ItemRepository repository = new ItemRepository();

        ItemReport first = new ItemReport(
            1, "Wallet", "Black wallet", "Library",
            LocalDate.now(), ReportType.LOST
        );

        ItemReport second = new ItemReport(
            1, "Keys", "Blue keychain", "Head Hall",
            LocalDate.now(), ReportType.FOUND
        );

        repository.save(first);

        assertThrows(
            IllegalArgumentException.class,
            () -> repository.save(second)
        );
    }
}
