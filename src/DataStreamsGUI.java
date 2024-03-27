import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame implements ActionListener {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchTextField;

    public DataStreamsGUI() {
        setTitle("DataStreams");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);

        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        JPanel textAreasPanel = new JPanel(new GridLayout(1, 2));
        textAreasPanel.add(originalScrollPane);
        textAreasPanel.add(filteredScrollPane);
        add(textAreasPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new FlowLayout());
        JButton loadButton = new JButton("Load File");
        loadButton.addActionListener(this);
        controlsPanel.add(loadButton);

        searchTextField = new JTextField(20);
        controlsPanel.add(searchTextField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        controlsPanel.add(searchButton);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        controlsPanel.add(quitButton);

        add(controlsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Load File")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a text file");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Path filePath = fileChooser.getSelectedFile().toPath();
                try (Stream<String> lines = Files.lines(filePath)) {
                    StringBuilder fileContent = new StringBuilder();
                    lines.forEach(line -> fileContent.append(line).append("\n"));
                    originalTextArea.setText(fileContent.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getActionCommand().equals("Search")) {
            String searchStr = searchTextField.getText();
            if (!searchStr.isEmpty()) {
                String originalText = originalTextArea.getText();
                StringBuilder filteredContent = new StringBuilder();

                try (Stream<String> lines = originalText.lines()) {
                    lines.filter(line -> line.contains(searchStr))
                            .forEach(line -> filteredContent.append(line).append("\n"));
                    filteredTextArea.setText(filteredContent.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error searching file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search string", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Quit")) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStreamsGUI gui = new DataStreamsGUI();
            gui.setVisible(true);
        });
    }
}
