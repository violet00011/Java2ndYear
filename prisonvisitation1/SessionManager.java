
package prisonvisitation1;

public class SessionManager {

    // Static fields to track session for different user types
    public static int loggedInVisitorID = -1;
    public static int loggedInOfficerID = -1;

    // Reset all session data
    public static void logoutAll() {
        loggedInVisitorID = -1;
        loggedInOfficerID = -1;
    }

    // Optional: logout per role
    public static void logoutVisitor() {
        loggedInVisitorID = -1;
    }

    public static void logoutOfficer() {
        loggedInOfficerID = -1;
    }

    // You can add more methods here for session timeout, last login, etc.
}

