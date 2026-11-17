
package lostandfound.ui;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;

import lostandfound.model.ItemReport;
import lostandfound.service.LostItemService;

public class LostItemPanel extends JPanel {

    private final LostItemService service;

    private final JTextField nameField;
    private final JTextArea descriptionArea;
    private final JTextField locationField;
    private final JTextField dateField;

    private final JButton submitButton;

    public LostItemPanel(LostItemService service) {
        this.service = service;

        setLayout(new GridLayout(0, 1, 5, 5));

        nameField = new JTextField();
        descriptionArea = new JTextArea(3, 20);
        locationField = new JTextField();
        dateField = new JTextField();
        submitButton = new JButton("Submit Lost Item");

        add(new JLabel("Item Name:"));
        add(nameField);

        add(new JLabel("Description:"));
        add(new JScrollPane(descriptionArea));

        add(new JLabel("Location Lost:"));
        add(locationField);

        add(new JLabel("Date Lost (YYYY-MM-DD):"));
        add(dateField);

        add(submitButton);

        submitButton.addActionListener(e -> submitReport());
    }

    private void submitReport() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());

            ItemReport report = service.reportLostItem(
                nameField.getText(),
                descriptionArea.getText(),
                locationField.getText(),
                date
            );

            JOptionPane.showMessageDialog(
                this,
                "Lost item reported successfully!\n"
                    + "Report ID: " + report.getId()
            );

            nameField.setText("");
            descriptionArea.setText("");
            locationField.setText("");
            dateField.setText("");

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
