import javax.swing.*;
import java.awt.*;

/**
 * Spike: tests whether Java Swing is a good fit for the UNB Lost & Found GUI.
 * Opens a window with item fields and a Submit button that shows the entered info.
 */
public class LostFoundSwingSpike {

    public static void main(String[] args) {
        // Swing components must be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(LostFoundSwingSpike::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("UNB Lost & Found - Swing Spike");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField nameField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JTextField locationField = new JTextField(20);
        JComboBox<String> statusBox = new JComboBox<>(new String[] {"Lost", "Found"});
        JButton submitButton = new JButton("Submit");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        addRow(form, c, 0, "Item name:", nameField);
        addRow(form, c, 1, "Description:", new JScrollPane(descriptionArea));
        addRow(form, c, 2, "Location:", locationField);
        addRow(form, c, 3, "Status:", statusBox);

        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        form.add(submitButton, c);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter an item name.",
                        "Missing information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String message = "Item name: " + name
                    + "\nDescription: " + descriptionArea.getText().trim()
                    + "\nLocation: " + locationField.getText().trim()
                    + "\nStatus: " + statusBox.getSelectedItem();
            JOptionPane.showMessageDialog(frame, message,
                    "Submitted Item", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.add(form);
        frame.pack();
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

    private static void addRow(JPanel panel, GridBagConstraints c, int row,
                               String label, JComponent field) {
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(field, c);
    }
}
