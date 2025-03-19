package EDPAct;

import javax.swing.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SimpleTextEditor extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private UndoManager undoManager;

    public SimpleTextEditor() {
        setTitle("Basic Text Editor - Exercise #3");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        fileChooser = new JFileChooser();
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JTabbedPane tabbedPane = buildTabbedPane();
        JToolBar toolbar = buildToolbar();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(tabbedPane, BorderLayout.NORTH);
        topPanel.add(toolbar, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }

    private JTabbedPane buildTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.add(createMenuButton("New", e -> textArea.setText("")));
        filePanel.add(createMenuButton("Open", e -> openFile()));
        filePanel.add(createMenuButton("Save", e -> saveFile()));
        filePanel.add(createMenuButton("Exit", e -> System.exit(0)));

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editPanel.add(createMenuButton("Cut", e -> textArea.cut()));
        editPanel.add(createMenuButton("Copy", e -> textArea.copy()));
        editPanel.add(createMenuButton("Paste", e -> textArea.paste()));
        editPanel.add(createMenuButton("Undo", e -> undoAction()));

        JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        helpPanel.add(createMenuButton("About", e -> showAbout()));

        tabbedPane.addTab("File", filePanel);
        tabbedPane.addTab("Edit", editPanel);
        tabbedPane.addTab("Help", helpPanel);

        return tabbedPane;
    }

    private JToolBar buildToolbar() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\new-page.png", "New", e -> textArea.setText("")));
        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\open-folder.png", "Open", e -> openFile()));
        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\save.png", "Save", e -> saveFile()));
        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\cut-file.png", "Cut", e -> textArea.cut()));
        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\copy.png", "Copy", e -> textArea.copy()));
        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\paste.png", "Paste", e -> textArea.paste()));
        toolbar.add(createToolbarButton("C:\\Users\\villa\\Downloads\\Exercise#4edp\\undo.png", "Undo", e -> undoAction()));

        return toolbar;
    }

    private JButton createMenuButton(String title, ActionListener action) {
        JButton button = new JButton(title);
        button.addActionListener(action);
        return button;
    }

private JButton createToolbarButton(String iconPath, String tooltip, ActionListener action) {
    ImageIcon icon = new ImageIcon(iconPath);

    Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
    ImageIcon resizedIcon = new ImageIcon(scaledImage);

    JButton button = new JButton(resizedIcon);
    button.setToolTipText(tooltip);
    button.addActionListener(action);
    return button;
}


    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                textArea.read(br, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                textArea.write(bw);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void undoAction() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this, "A Basic Text Editor\nVillanueva, Armaine A\nBSIT 2FG1", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleTextEditor().setVisible(true));
    }
}
