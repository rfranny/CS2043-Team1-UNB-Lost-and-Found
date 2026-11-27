package lostandfound.ui;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;

import lostandfound.model.ItemCategory;
import lostandfound.model.ItemReport;
import lostandfound.service.FoundItemService;

public class FoundItemPanel extends JPanel {

    private final FoundItemService service;

    private final JTextField nameField;
    private final JTextArea descriptionArea;
    private final JTextField locationField;
    private final JTextField dateField;
    private final JComboBox<ItemCategory> categoryBox;

    private final JButton submitButton;

    public FoundItemPanel(FoundItemService service) {
        this.service = service;

        setLayout(new GridLayout(0, 1, 5, 5));

        nameField = new JTextField();
        descriptionArea = new JTextArea(3, 20);
        locationField = new JTextField();
        dateField = new JTextField();
        categoryBox = new JComboBox<>(ItemCategory.values());
        categoryBox.setSelectedItem(ItemCategory.OTHER);
        submitButton = new JButton("Submit Found Item");

        add(new JLabel("Item Name:"));
        add(nameField);
        
        add(new JLabel("Category:"));
        add(categoryBox);

        add(new JLabel("Description:"));
        add(new JScrollPane(descriptionArea));

        add(new JLabel("Location Found:"));
        add(locationField);

        add(new JLabel("Date Found (YYYY-MM-DD):"));
        add(dateField);

        add(submitButton);

        submitButton.addActionListener(e -> submitReport());
    }

    private void submitReport() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());

            ItemReport report = service.reportFoundItem(
                nameField.getText(),
                descriptionArea.getText(),
                locationField.getText(),
                date,
                (ItemCategory) categoryBox.getSelectedItem()
            );

            JOptionPane.showMessageDialog(
                this,
                "Found item reported successfully!\n" + "Report ID: " + report.getId()
            );

            nameField.setText("");
            descriptionArea.setText("");
            locationField.setText("");
            dateField.setText("");
            categoryBox.setSelectedItem(ItemCategory.OTHER);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter the date as YYYY-MM-DD.",
                "Invalid Date",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Invalid Report",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
