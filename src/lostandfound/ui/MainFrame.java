package lostandfound.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import lostandfound.repository.ItemRepository;
import lostandfound.service.LostItemService;

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

        JTabbedPane tabs = new JTabbedPane();

        // Existing working feature
        tabs.addTab(
            "Report Lost Item",
            new LostItemPanel(lostItemService)
        );

        // Other features will be added here later

        add(tabs);
    }
}
