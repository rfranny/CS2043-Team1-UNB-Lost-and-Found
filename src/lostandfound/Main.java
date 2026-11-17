package lostandfound;

import java.time.LocalDate;

import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;

public class Main {

    public static void main(String[] args) {

        ItemRepository repository = new ItemRepository();

        // Create a lost item
        ItemReport wallet = new ItemReport(
            1,
            "Black Wallet",
            "Leather wallet with student ID",
            "Harriet Irving Library",
            LocalDate.now(),
            ReportType.LOST
        );

        // Create a found item
        ItemReport keys = new ItemReport(
            2,
            "Keys",
            "Three keys on a blue keychain",
            "Head Hall",
            LocalDate.now(),
            ReportType.FOUND
        );

        // Save both reports
        repository.save(wallet);
        repository.save(keys);

        // Display all saved reports
        System.out.println("UNB Lost and Found");
        System.out.println("------------------");

        for (ItemReport report : repository.findAll()) {
            System.out.println(
                report.getId() + ": " +
                report.getName() + " - " +
                report.getType() + " - " +
                report.getLocation()
            );
        }

        System.out.println("Total reports: " + repository.count());
    }
}
