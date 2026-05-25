package LibrarianPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BooksPanel extends JPanel {

    JLabel imageLabel;

    private String selectedImage = "src/defaultbook.png";

    private DefaultTableModel model;

    public BooksPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ================= TITLE =================
        JLabel title = new JLabel("Books Management");

        title.setFont(new Font(
                "Arial",
                Font.BOLD,
                28
        ));

        JPanel top = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        top.add(title);

        // ================= TABLE =================
        String[] columns = {
                "ID",
                "Title",
                "Author",
                "Category",
                "Available"
        };

        model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);

        loadBooks();

        // ================= FORM =================
        JPanel form = new JPanel(
                new GridLayout(7,2,10,10)
        );

        JTextField titleField = new JTextField();

        JTextField authorField = new JTextField();

        JTextField categoryField = new JTextField();

        JButton uploadBtn =
                new JButton("Upload Book Image");

        imageLabel = new JLabel();

        uploadBtn.addActionListener(
                e -> uploadImage()
        );

        JButton addBtn = new JButton("Add Book");

        // ================= ADD BOOK FIXED =================
        addBtn.addActionListener(e -> {

            String titleTxt =
                    titleField.getText().trim();

            String authorTxt =
                    authorField.getText().trim();

            String categoryTxt =
                    categoryField.getText().trim();

            if (titleTxt.isEmpty()
                    || authorTxt.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Fill all fields!"
                );

                return;
            }

            // ================= ADD TO DATABASE =================
            Book newBook = new Book(
                    titleTxt,
                    authorTxt,
                    selectedImage
            );

            LibraryDB.get().books.add(newBook);

            // ================= ADD TO TABLE =================
            model.addRow(new Object[]{

                    model.getRowCount() + 1,

                    titleTxt,

                    authorTxt,

                    categoryTxt,

                    "YES"
            });

            JOptionPane.showMessageDialog(
                    this,
                    "Book Added Successfully!"
            );

            // CLEAR
            titleField.setText("");
            authorField.setText("");
            categoryField.setText("");

            imageLabel.setIcon(null);

            selectedImage = "src/defaultbook.png";
        });

        // ================= FORM =================
        form.add(new JLabel("Title"));
        form.add(titleField);

        form.add(new JLabel("Author"));
        form.add(authorField);

        form.add(new JLabel("Category"));
        form.add(categoryField);

        form.add(uploadBtn);
        form.add(imageLabel);

        form.add(addBtn);

        // ================= FINAL =================
        add(top, BorderLayout.NORTH);

        add(pane, BorderLayout.CENTER);

        add(form, BorderLayout.SOUTH);
    }

    // ================= LOAD EXISTING BOOKS =================
    private void loadBooks() {

        model.setRowCount(0);

        int id = 1;

        for (Book b : LibraryDB.get().books) {

            model.addRow(new Object[]{

                    id++,

                    b.title,

                    b.author,

                    "General",

                    "YES"
            });
        }
    }

    // ================= IMAGE UPLOAD =================
    private void uploadImage() {

        JFileChooser chooser =
                new JFileChooser();

        int result =
                chooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION){

            selectedImage =
                    chooser.getSelectedFile()
                            .getAbsolutePath();

            ImageIcon icon =
                    new ImageIcon(selectedImage);

            Image img =
                    icon.getImage()
                            .getScaledInstance(
                                    100,
                                    120,
                                    Image.SCALE_SMOOTH
                            );

            imageLabel.setIcon(
                    new ImageIcon(img)
            );
        }
    }
}