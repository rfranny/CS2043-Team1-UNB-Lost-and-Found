
package lostandfound.ui;

import javax.swing.JFrame;

import lostandfound.repository.ItemRepository;
import lostandfound.service.LostItemService;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("UNB Lost and Found");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ItemRepository repository = new ItemRepository();
        LostItemService lostItemService =
            new LostItemService(repository);

        add(new LostItemPanel(lostItemService));
    }
}
