
package lostandfound.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import lostandfound.repository.ItemRepository;
import lostandfound.service.LostItemService;
import lostandfound.service.FoundItemService;

public class MainFrame extends JFrame {

    private final ItemRepository repository;

    public MainFrame() {
        setTitle("UNB Lost and Found");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Shared repository for all three features
        repository = new ItemRepository();

        LostItemService lostItemService =
                new LostItemService(repository);

        FoundItemService foundItemService =
                new FoundItemService(repository);

        JTabbedPane tabs = new JTabbedPane();

        // Your feature
        tabs.addTab(
            "Report Lost Item",
            new LostItemPanel(lostItemService)
        );

        // Nathan's feature
        tabs.addTab(
            "Report Found Item",
            new FoundItemPanel(foundItemService)
        );

        // Kamsi's feature
        tabs.addTab(
            "Browse Reports",
            new BrowseReportsPanel(repository)
        );

        add(tabs);
    }
}
