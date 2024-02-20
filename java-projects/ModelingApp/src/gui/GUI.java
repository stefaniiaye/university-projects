package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class GUI extends JFrame {
    private Controller ctl;
    private JTable resultTable;
    private JList<String> modelList;
    private JList<String> dataList;
    private final String dataDir = System.getProperty("user.home") + "/Modeling/data/";
    private final String scriptDir = System.getProperty("user.home") + "/Modeling/scripts/";
    private final String modelsDir = System.getProperty("user.dir") + "/src/models/";

    public GUI() {
        setTitle("Modeling GUI");
        setSize(950, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        modelList = new JList<>();
        dataList = new JList<>();
        JButton runModelButton = new JButton("Run model");
        JButton runScriptFromFileButton = new JButton("Run script from file");
        JButton runAdHocScriptButton = new JButton("Create and run ad hoc script");
        resultTable = new JTable();

        populateModelNames();
        populateDataFiles();

        runModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileSelectionDialog();
            }
        });

        runScriptFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runScriptFromFile();
            }
        });

        runAdHocScriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runAdHocScript();
            }
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(new JLabel("Select model and data"), BorderLayout.NORTH);
        Dimension preferredSize = new Dimension(100, 200);
        modelList.setPreferredSize(preferredSize);
        dataList.setPreferredSize(preferredSize);
        leftPanel.add(new JScrollPane(modelList), BorderLayout.WEST);
        leftPanel.add(new JScrollPane(dataList), BorderLayout.EAST);
        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new FlowLayout());
        bottomPanel1.add(runModelButton);
        leftPanel.add(bottomPanel1, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.setLayout(new FlowLayout());
        bottomPanel2.add(runScriptFromFileButton);
        bottomPanel2.add(runAdHocScriptButton);

        centerPanel.add(bottomPanel2, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void populateModelNames() {
        File modelsDirectory = new File(modelsDir);
        String[] modelNames = modelsDirectory.list();
        if (modelNames != null) {
            modelList.setListData(modelNames);
        }
    }

    private void populateDataFiles() {
        File dataDirectory = new File(dataDir);
        String[] dataFileNames = dataDirectory.list();
        if (dataFileNames != null) {
            dataList.setListData(dataFileNames);
        }
    }

    private void showFileSelectionDialog() {
        String selectedModel = modelList.getSelectedValue();
        String selectedDataFile = dataList.getSelectedValue();

        if (selectedModel == null || selectedDataFile == null) {
            JOptionPane.showMessageDialog(this, "Please select both model and data file.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String modelName = "models." + selectedModel.replace(".java", "");
        ctl = new Controller(modelName);

        String dataFilePath = dataDir + selectedDataFile;
        ctl.readDataFrom(dataFilePath);

        performModeling();
    }

    private void performModeling() {
        if (ctl == null) {
            JOptionPane.showMessageDialog(this, "Please select both model and data file.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ctl.runModel();

        Object[][] rowData = parseTsv(ctl.getResultsAsTsv());

        Object[] columnNames = Arrays.copyOf(rowData[0], rowData[0].length, Object[].class);

        DefaultTableModel tableModel = new DefaultTableModel(Arrays.copyOfRange(rowData, 1, rowData.length), columnNames);
        resultTable.setModel(tableModel);
        DefaultTableCellRenderer rightRenderer = new AlignmentCellRenderer();

        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
    }
    private static class AlignmentCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 0) {
                setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            return cellComponent;
        }
    }

    private void runScriptFromFile() {
        JFileChooser fileChooser = new JFileChooser(scriptDir);
        fileChooser.setDialogTitle("Select file");

        int result = fileChooser.showDialog(this, "Select file");

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String scriptFilePath = selectedFile.getAbsolutePath();

            ctl.runScriptFromFile(scriptFilePath);
            updateTable();
        }
    }

    private void runAdHocScript() {
        JTextArea adhocScriptTextArea = new JTextArea();
        adhocScriptTextArea.setRows(10);
        adhocScriptTextArea.setColumns(30);
        JScrollPane scriptScrollPane = new JScrollPane(adhocScriptTextArea);
        int result = JOptionPane.showOptionDialog(
                null, scriptScrollPane, "Script",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String script = adhocScriptTextArea.getText();
            if (script != null && !script.isEmpty()) {
                if (ctl == null) {
                    this.performModeling();
                }
                ctl.runScript(script);
                updateTable();
            }
        }
    }

    private void updateTable() {
        Object[][] rowData = parseTsv(ctl.getResultsAsTsv());
        Object[] columnNames = Arrays.copyOf(rowData[0], rowData[0].length, Object[].class);
        DefaultTableModel tableModel = new DefaultTableModel(Arrays.copyOfRange(rowData, 1, rowData.length), columnNames);
        resultTable.setModel(tableModel);
        DefaultTableCellRenderer rightRenderer = new AlignmentCellRenderer();

        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
    }

    private Object[][] parseTsv(String tsvData) {
        String[] rows = tsvData.split("\n");
        int numRows = rows.length;
        String[] columns = rows[0].split("\t");
        int numCols = columns.length;

        Object[][] data = new Object[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            String[] values = rows[i].split("\t");
            for (int j = 0; j < numCols; j++) {
                try {
                    if (i == 0 && j == 0) {
                        data[i][j] = " ";
                    } else {
                        double cellValue = Double.parseDouble(values[j]);
                        if (cellValue % 1 == 0) {
                            data[i][j] = String.format("%.0f", cellValue);
                        } else if (cellValue >= 1 && cellValue <= 100) {
                            data[i][j] = String.format("%.2f", cellValue);
                        } else if (cellValue < 1) {
                            data[i][j] = String.format("%.3f", cellValue);
                        } else if (cellValue > 100) {
                            data[i][j] = String.format("%.1f", cellValue);
                        }
                    }
                } catch (NumberFormatException e) {
                    data[i][j] = values[j];
                }
            }
        }
        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}
