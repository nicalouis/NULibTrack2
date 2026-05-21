package Services;

import Services.LibraryDB.User;

public class authServ {

    private final LibraryDB db = LibraryDB.get();

    public boolean loginPatron(String email, String password) {

        for (User u : db.patrons) {

            if (u.email.trim().equalsIgnoreCase(email.trim())
                    && u.password.equals(password)
                    && u.role.equalsIgnoreCase("PATRON")) {

                return true;
            }
        }

        return false;
    }

    public boolean loginLibrarian(String email, String password) {

        for (User u : db.librarians) {

            if (u.email.trim().equalsIgnoreCase(email.trim())
                    && u.password.equals(password)
                    && u.role.equalsIgnoreCase("LIBRARIAN")) {

                return true;
            }
        }

        return false;
    }
}