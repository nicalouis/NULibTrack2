package LibrarianPanel;

import Services.LibraryDB;
import Services.LibraryDB.Book;
import Services.LibraryDB.BorrowRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BooksPanel extends JPanel {

    private JLabel imageLabel;
    private String selectedImage = "src/defaultbook.png";
    private DefaultTableModel model;

    // COLORS
    private final Color BLUE = new Color(0,120,215);
    private final Color YELLOW = new Color(255,204,0);

    public BooksPanel() {

        setLayout(new BorderLayout(15,15));
        setBackground(new Color(245,247,250));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // HEADER
        JLabel title = new JLabel("Books Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,15,15));
        top.setBackground(BLUE);

        top.add(title);

        add(top, BorderLayout.NORTH);

        // ================= TABLE =================

        String[] columns = {"Title","Author","Category","Available","Action"};

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220,235,255));
        table.setSelectionForeground(Color.BLACK);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // HEADER
        table.getTableHeader().setBackground(YELLOW);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));

        // COLUMN WIDTHS
        table.getColumnModel().getColumn(0).setPreferredWidth(260);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {

                Component c = super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

                setBorder(BorderFactory.createEmptyBorder(0,12,0,12));

                String titleVal = model.getValueAt(row, 0) != null
                        ? model.getValueAt(row, 0).toString()
                        : "";

                boolean isSection = titleVal.startsWith("---");

                // SECTION ROWS
                if (isSection) {

                    c.setBackground(new Color(230,235,245));
                    c.setForeground(new Color(40,40,40));

                    c.setFont(new Font("Segoe UI", Font.BOLD, 14));

                    return c;
                }

                // NORMAL ROWS
                if (!isSelected) {

                    c.setBackground(row % 2 == 0
                            ? new Color(248,250,255)
                            : Color.WHITE);
                }

                c.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                // AVAILABLE COLORS
                if (column == 3 && value != null) {

                    String v = value.toString();

                    if (v.equalsIgnoreCase("YES")) {

                        c.setForeground(new Color(0,140,0));

                    } else {

                        c.setForeground(new Color(210,50,50));
                    }

                } else {

                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        JScrollPane pane = new JScrollPane(table);

        pane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220,225,230)
                ),
                BorderFactory.createEmptyBorder(5,5,5,5)
        ));

        pane.getViewport().setBackground(Color.WHITE);

        // smoother scrolling
        pane.getVerticalScrollBar().setUnitIncrement(14);

        loadBooks();

        // ACTION BUTTON
        JButton actionBtn = new JButton("APPLY ACTION");

        actionBtn.setBackground(BLUE);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFocusPainted(false);

        actionBtn.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                13
        ));

        actionBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a book first!");
                return;
            }

            String titleSelected = model.getValueAt(row, 0).toString();

            if (titleSelected.startsWith("---")) {
                JOptionPane.showMessageDialog(this, "Select a valid book!");
                return;
            }

            String[] options = {
                    "Mark Available",
                    "Mark as Borrowed",
                    "Mark as Reserved"
            };

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choose action for: " + titleSelected,
                    "Book Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            Book target = findBook(titleSelected);

            if (target == null) return;

            // AVAILABLE
            if (choice == 0) {

                LibraryDB.get().reservations.remove(target);

                LibraryDB.get().borrowRecords.removeIf(
                        r -> r.book == target
                );

                target.available = true;
                target.borrowed = false;

                target.borrowDate = null;
                target.dueDate = null;
                target.reserveDate = null;

                if (!LibraryDB.get().books.contains(target)) {
                    LibraryDB.get().books.add(target);
                }

                LibraryDB.get().addNotification(
                        "📢 \"" + target.title + "\" is now AVAILABLE!"
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Book moved to GENERAL section!"
                );
            }

            // BORROWED
            if (choice == 1) {

                LibraryDB.get().reservations.remove(target);

                LibraryDB.get().books.remove(target);

                target.available = false;
                target.borrowed = true;

                boolean exists = false;

                for (BorrowRecord r : LibraryDB.get().borrowRecords) {

                    if (r.book == target) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {

                    LibraryDB.get().borrowRecords.add(
                            new BorrowRecord("system", target)
                    );
                }

                LibraryDB.get().addNotification(
                        "🚫 \"" + target.title + "\" was marked as BORROWED by librarian."
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Book moved to BORROWED section!"
                );
            }

            // RESERVED
            if (choice == 2) {

                LibraryDB.get().borrowRecords.removeIf(
                        r -> r.book == target
                );

                LibraryDB.get().books.remove(target);

                target.available = false;
                target.borrowed = false;

                if (!LibraryDB.get().reservations.contains(target)) {
                    LibraryDB.get().reservations.add(target);
                }

                LibraryDB.get().addNotification(
                        "📚 \"" + target.title + "\" was RESERVED by librarian."
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Book moved to RESERVED section!"
                );
            }

            loadBooks();

            try {

                if (PatronPanel.CatalogPanel.instance != null) {
                    PatronPanel.CatalogPanel.instance.refresh();
                }

            } catch (Exception ignored) {}
        });

        // FORM PANEL
        JPanel formCard = new JPanel();

        formCard.setLayout(new BoxLayout(
                formCard,
                BoxLayout.Y_AXIS
        ));

        formCard.setBackground(Color.WHITE);

        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220,225,230)
                ),
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        ));

        formCard.setPreferredSize(new Dimension(260,0));

        JTextField titleField = createField();
        JTextField authorField = createField();
        JTextField categoryField = createField();

        JLabel formTitle = new JLabel("Add New Book");

        formTitle.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                18
        ));

        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));

        formCard.add(createLabel("Title"));
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(titleField);

        formCard.add(Box.createVerticalStrut(15));

        formCard.add(createLabel("Author"));
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(authorField);

        formCard.add(Box.createVerticalStrut(15));

        formCard.add(createLabel("Category"));
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(categoryField);

        formCard.add(Box.createVerticalStrut(20));

        JButton uploadBtn = new JButton("Upload Image");

        uploadBtn.setBackground(YELLOW);
        uploadBtn.setFocusPainted(false);

        uploadBtn.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                12
        ));

        imageLabel = new JLabel();

        imageLabel.setPreferredSize(new Dimension(110,130));

        imageLabel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(180,180,180)
                )
        );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        uploadBtn.addActionListener(e -> uploadImage());

        formCard.add(uploadBtn);
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(imageLabel);
        formCard.add(Box.createVerticalStrut(20));

        JButton addBtn = new JButton("Add Book");

        addBtn.setBackground(BLUE);
        addBtn.setForeground(Color.WHITE);

        addBtn.setFocusPainted(false);

        addBtn.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                13
        ));

        addBtn.addActionListener(e -> {

            String titleTxt = titleField.getText().trim();
            String authorTxt = authorField.getText().trim();

            if (titleTxt.isEmpty() || authorTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            Book newBook = new Book(titleTxt, authorTxt, selectedImage);
            newBook.available = true;

            LibraryDB.get().books.add(newBook);

            loadBooks();

            JOptionPane.showMessageDialog(this, "Book Added!");
        });

        formCard.add(addBtn);

        JPanel rightPanel = new JPanel(new BorderLayout());

        rightPanel.setBackground(
                new Color(245,247,250)
        );

        rightPanel.add(formCard, BorderLayout.CENTER);
        rightPanel.add(actionBtn, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                pane,
                rightPanel
        );

        split.setDividerLocation(700);
        split.setBorder(null);

        add(split, BorderLayout.CENTER);
    }

    // FIND BOOK
    private Book findBook(String title) {

        for (Book b : LibraryDB.get().books)
            if (b.title.equals(title)) return b;

        for (BorrowRecord r : LibraryDB.get().borrowRecords)
            if (r.book != null && r.book.title.equals(title))
                return r.book;

        for (Book b : LibraryDB.get().reservations)
            if (b.title.equals(title)) return b;

        return null;
    }

    // LOAD BOOKS
    private void loadBooks() {

        model.setRowCount(0);

        // GENERAL
        model.addRow(new Object[]{
                "--- GENERAL BOOKS ---","","","",""
        });

        for (Book b : LibraryDB.get().books) {

            boolean isSystemBorrowed =
                    b.borrowed &&
                    (b.borrowDate != null &&
                    (b.borrowDate.equals("Jan 10, 2025") ||
                    b.borrowDate.equals("Jan 12, 2025")));

            if (b.available && !b.borrowed && !isSystemBorrowed) {

                model.addRow(new Object[]{
                        b.title,
                        b.author,
                        "General",
                        "YES",
                        "ACTION"
                });
            }
        }

        // BORROWED
        model.addRow(new Object[]{
                "--- BORROWED BOOKS ---","","","",""
        });

        for (BorrowRecord r : LibraryDB.get().borrowRecords) {

            if (r == null || r.book == null) continue;

            Book b = r.book;

            boolean isSystemBorrowed =
                    b.borrowDate != null &&
                    (b.borrowDate.equals("Jan 10, 2025") ||
                    b.borrowDate.equals("Jan 12, 2025"));

            if (b.borrowed || isSystemBorrowed) {

                model.addRow(new Object[]{
                        b.title,
                        b.author,
                        "Borrowed",
                        "NO",
                        "ACTION"
                });
            }
        }

        // RESERVED
        model.addRow(new Object[]{
                "--- RESERVED BOOKS ---","","","",""
        });

        for (Book b : LibraryDB.get().reservations) {

            model.addRow(new Object[]{
                    b.title,
                    b.author,
                    "Reserved",
                    "NO",
                    "ACTION"
                });
            }
        }
        
    // IMAGE
    private void uploadImage() {

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            selectedImage =
                    chooser.getSelectedFile().getAbsolutePath();

            ImageIcon icon = new ImageIcon(selectedImage);

            Image img = icon.getImage().getScaledInstance(
                    110,
                    130,
                    Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(img));
        }
    }

    // FIELD
    
private JTextField createField() {

    JTextField field = new JTextField();

    field.setPreferredSize(new Dimension(210,35));
    field.setMaximumSize(new Dimension(210,35));

    field.setFont(new Font(
            "Segoe UI",
            Font.PLAIN,
            13
    ));

    field.setBackground(new Color(245,248,255));

    field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLUE),
            BorderFactory.createEmptyBorder(5,10,5,10)
    ));

    return field;
}

// LABEL
private JLabel createLabel(String text) {

    JLabel label = new JLabel(text);

    label.setFont(new Font(
            "Segoe UI",
            Font.BOLD,
            13
    ));

    return label;
}
}