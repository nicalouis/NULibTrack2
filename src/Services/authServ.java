package Services;

import Services.LibraryDB.User;

public class authServ {

    private final LibraryDB db = LibraryDB.get();

    public boolean loginPatron(String email, String password) {

        for (User u : db.patrons) {

            if (u.email.equalsIgnoreCase(email)
                    && u.password.equals(password)
                    && u.role.equals("PATRON")) {
                return true;
            }
        }
        return false;
    }

    public boolean loginLibrarian(String email, String password) {

        for (User u : db.librarians) {

            if (u.email.equalsIgnoreCase(email)
                    && u.password.equals(password)
                    && u.role.equals("LIBRARIAN")) {
                return true;
            }
        }
        return false;
    }
}