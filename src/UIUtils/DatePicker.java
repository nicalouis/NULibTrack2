package UIUtils;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class DatePicker {

    // ================= MAIN PICKER =================
    public static String pickDate(Component parent) {
        return pickDate(parent, true); // default = allow future (reserve)
    }

    public static String pickDate(Component parent, boolean allowFuture) {

        JDateChooser chooser = createCalendar(allowFuture);

        int result = JOptionPane.showConfirmDialog(
                parent,
                chooser,
                "Select Date",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            Date selected = chooser.getDate();
            if (selected == null) return null;

            return format(selected);
        }

        return null;
    }

    // ================= CALENDAR =================
    public static JDateChooser createCalendar(boolean allowFuture) {

        JDateChooser chooser = new JDateChooser();

        chooser.setDate(new Date());
        chooser.setDateFormatString("MMM dd, yyyy");
        chooser.setPreferredSize(new Dimension(200, 35));

        Date today = new Date();

        if (allowFuture) {
            chooser.setMinSelectableDate(today);
            chooser.setMaxSelectableDate(null);
        } else {
            chooser.setMinSelectableDate(today);
            chooser.setMaxSelectableDate(today);
        }

        return chooser;
    }

    // ================= FORMAT =================
    public static String format(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return new java.text.SimpleDateFormat("MMM dd, yyyy")
                .format(cal.getTime());
    }

    // ================= HELPERS =================
    public static String pickBorrowDate(Component parent) {
        return format(new Date());
    }

    public static String pickReserveDate(Component parent) {
        return pickDate(parent, true);
    }
}