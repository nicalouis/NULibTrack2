package LibrarianPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BooksPanel extends JPanel {

    JLabel imageLabel;

    public BooksPanel(){

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Books Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(title);

        String[] columns = {"ID","Title","Author","Category","Available"};

        DefaultTableModel model = new DefaultTableModel(columns,0);

        JTable table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);

        JPanel form = new JPanel(new GridLayout(7,2,10,10));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();

        JButton uploadBtn = new JButton("Upload Book Image");
        imageLabel = new JLabel();

        uploadBtn.addActionListener(e -> uploadImage());

        JButton addBtn = new JButton("Add Book");

        addBtn.addActionListener(e -> {
            model.addRow(new Object[]{
                    model.getRowCount()+1,
                    titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    "YES"
            });
        });

        form.add(new JLabel("Title"));
        form.add(titleField);

        form.add(new JLabel("Author"));
        form.add(authorField);

        form.add(new JLabel("Category"));
        form.add(categoryField);

        form.add(uploadBtn);
        form.add(imageLabel);

        form.add(addBtn);

        add(top, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        add(form, BorderLayout.SOUTH);
    }

    private void uploadImage(){

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION){

            ImageIcon icon = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());

            Image img = icon.getImage().getScaledInstance(100,120,Image.SCALE_SMOOTH);

            imageLabel.setIcon(new ImageIcon(img));
        }
    }
}