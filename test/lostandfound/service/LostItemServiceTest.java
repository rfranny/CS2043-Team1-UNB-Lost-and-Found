package lostandfound.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

class LostItemServiceTest {

    private ItemRepository repository;
    private LostItemService service;

    @BeforeEach
    void setUp() {
        repository = new ItemRepository();
        service = new LostItemService(repository);
    }

    @Test
    void validLostItemIsSaved() {
        ItemReport report = service.reportLostItem(
            "Black Wallet",
            "Leather wallet",
            "Harriet Irving Library",
            LocalDate.of(2026, 11, 17)
        );

        assertEquals(1, repository.count());
        assertEquals(ReportType.LOST, report.getType());
        assertEquals("Black Wallet", report.getName());
        assertSame(report, repository.findById(1));
    }

    @Test
    void emptyNameIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.reportLostItem(
                "   ", "Wallet", "Library",
                LocalDate.now()
            )
        );

        assertEquals(0, repository.count());
    }

    @Test
    void missingLocationIsRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.reportLostItem(
                "Wallet", "Black wallet", "",
                LocalDate.now()
            )
        );

        assertEquals(0, repository.count());
    }


    @Test
    void reportsGetUniqueIds() {

    	ItemReport first = service.reportLostItem(
    		"Wallet",
    		"Black wallet",
    		"Library",
    		LocalDate.now()
    			);

    	ItemReport second = service.reportLostItem(
    		"Keys",
    		"Blue keychain",
    		"Head Hall",
    		LocalDate.now()
    	);

    	assertEquals(1, first.getId());
    	assertEquals(2, second.getId());
    	assertEquals(2, repository.count());
    }

}
