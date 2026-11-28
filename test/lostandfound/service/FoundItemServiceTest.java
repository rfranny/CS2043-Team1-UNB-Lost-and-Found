package lostandfound.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

class FoundItemServiceTest {

    private ItemRepository repository;
    private FoundItemService service;

    @BeforeEach
    void setUp() {
        repository = new ItemRepository();
        service = new FoundItemService(repository);
    }

    @Test
    void validFoundItemIsSavedAsFound() {
        ItemReport report = service.reportFoundItem(
            "Blue Backpack",
            "Nike backpack with a laptop",
            "Head Hall",
            LocalDate.of(2026, 11, 17)
        );

        assertEquals(1, repository.count());
        assertEquals(ReportType.FOUND, report.getType());
        assertEquals("Blue Backpack", report.getName());
        assertSame(report, repository.findById(1));
    }

    @Test
    void emptyNameIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.reportFoundItem(
                "   ", "Backpack", "Library",
                LocalDate.now()
            )
        );

        assertEquals(0, repository.count());
    }

    @Test
    void emptyLocationIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.reportFoundItem(
                "Backpack", "Blue backpack", "",
                LocalDate.now()
            )
        );

        assertEquals(0, repository.count());
    }

    @Test
    void reportsGetUniqueIds() {
        ItemReport first = service.reportFoundItem(
            "Keys", "Blue keychain", "Gym",
            LocalDate.now()
        );

        ItemReport second = service.reportFoundItem(
            "Phone", "Black phone", "Cafeteria",
            LocalDate.now()
        );

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
        assertEquals(2, repository.count());
    }
}
