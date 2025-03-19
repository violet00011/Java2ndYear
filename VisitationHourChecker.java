
package it203finalproject;

import java.time.LocalTime;

public class VisitationHourChecker {

    private static final LocalTime VISIT_START_TIME = LocalTime.of(8, 0);  // 8:00 AM
    private static final LocalTime VISIT_END_TIME = LocalTime.of(15, 0);   // 3:00 PM

    public boolean isVisitAllowed(LocalTime visitTime) {
        // Check if the current time is within the allowed visitation hours
        return !visitTime.isBefore(VISIT_START_TIME) && !visitTime.isAfter(VISIT_END_TIME);
    }
}
