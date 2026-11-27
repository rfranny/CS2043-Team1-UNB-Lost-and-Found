package lostandfound.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import lostandfound.model.ItemCategory;
import lostandfound.model.ItemReport;
import lostandfound.model.ReportType;
import lostandfound.repository.ItemRepository;
import lostandfound.service.BrowseReportsService;

public class BrowseReportsPanel extends JPanel {

    private static final String ALL = "All";

    private final BrowseReportsService service;

    private final JComboBox<String> typeFilter;
    private final JTextField searchField;
    private final JComboBox<String> categoryFilter;
    private final DefaultTableModel tableModel;
    private final JTable reportTable;
    private final JTextArea detailsArea;
    

    public BrowseReportsPanel(ItemRepository repository) {
        this.service = new BrowseReportsService(repository);

        setLayout(new BorderLayout(5, 5));

        // Top bar: type filter and refresh button
        typeFilter = new JComboBox<>(new String[] {ALL, "Lost", "Found"});
        searchField = new JTextField(16);
        
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        for (ItemCategory category : ItemCategory.values()) {
            categoryFilter.addItem(category.name());
        }
        
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(new JLabel("Show:"));
        topBar.add(typeFilter);
        topBar.add(categoryFilter);
        JLabel searchLabel = new JLabel("Keyword:");
        searchLabel.setLabelFor(searchField);
        topBar.add(searchLabel);
        topBar.add(searchField);
        topBar.add(searchButton);
        topBar.add(refreshButton);
        add(topBar, BorderLayout.NORTH);

        // Table of reports
        tableModel = new DefaultTableModel(
            new String[] {"ID", "Item Name", "Type", "Location", "Category"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // Details of the selected report
        detailsArea = new JTextArea(6, 20);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Details"));
        add(detailsScroll, BorderLayout.SOUTH);

        typeFilter.addActionListener(e -> refreshReports());
        categoryFilter.addActionListener(e -> refreshReports());
        searchButton.addActionListener(e -> refreshReports());
        searchField.addActionListener(e -> refreshReports());
        refreshButton.addActionListener(e -> refreshReports());
        reportTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedDetails();
            }
        });

        // Reload whenever this panel becomes visible (e.g. its tab is selected)
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshReports();
            }
        });

        refreshReports();
    }

    // Rereads the shared repository so newly submitted reports appear
    public void refreshReports() {
    	
    	String selection = (String) categoryFilter.getSelectedItem();

    	ItemCategory category = null;
    	if (!"All Categories".equals(selection)) {
    	    category = ItemCategory.valueOf(selection);
    	}

    	List<ItemReport> reports = service.searchReports(
    	    searchField.getText(),
    	    selectedType(),
    	    category
    	);

        tableModel.setRowCount(0);

        for (ItemReport report : reports) {
            tableModel.addRow(new Object[] {
                report.getId(),
                report.getName(),
                report.getType(),
                report.getLocation(),
                report.getCategory()
            });
        }

        detailsArea.setText(reports.isEmpty()
            ? "No reports to show."
            : "Select a report to see its details.");
    }

    private ReportType selectedType() {
        String choice = (String) typeFilter.getSelectedItem();

        if ("Lost".equals(choice)) {
            return ReportType.LOST;
        }

        if ("Found".equals(choice)) {
            return ReportType.FOUND;
        }

        return null;
    }

    private void showSelectedDetails() {
        int row = reportTable.getSelectedRow();

        if (row < 0) {
            return;
        }

        int id = (Integer) tableModel.getValueAt(row, 0);
        ItemReport report = service.getReport(id);

        if (report == null) {
            return;
        }

        String description = report.getDescription().isBlank()
            ? "(none)"
            : report.getDescription();

        detailsArea.setText(
            "Report ID: " + report.getId()
                + "\nItem: " + report.getName()
                + "\nType: " + report.getType()
                + "\nLocation: " + report.getLocation()
                + "\nDate: " + report.getDate()
                + "\nDescription: " + description
        );
        detailsArea.setCaretPosition(0);
    }
}
