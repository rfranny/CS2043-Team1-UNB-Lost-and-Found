
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
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // One shared repository for the entire application
        repository = new ItemRepository();

        LostItemService lostItemService =
                new LostItemService(repository);

        FoundItemService foundItemService =
                new FoundItemService(repository);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab(
            "Report Lost Item",
            new LostItemPanel(lostItemService)
        );

        tabs.addTab(
            "Report Found Item",
            new FoundItemPanel(foundItemService)
        );

        add(tabs);
    }
}
