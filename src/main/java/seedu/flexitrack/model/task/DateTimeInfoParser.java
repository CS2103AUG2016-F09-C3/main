package seedu.flexitrack.model.task;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import seedu.flexitrack.commons.exceptions.IllegalValueException;

//@@author A0127686R
/**
 * Represents the Parser and Time related DateTimeInfo.
 */
public class DateTimeInfoParser {

    public static final String MESSAGE_DATETIMEINFO_CONSTRAINTS = "Invalid time inputed. Please check your spelling!";

    private DateGroup timingInfo;

    public DateTimeInfoParser(String givenTime) throws IllegalValueException {
        try {
            Parser parser = new Parser();
            List<DateGroup> dateParser = parser.parse(givenTime);
            setTimingInfo(dateParser);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException(MESSAGE_DATETIMEINFO_CONSTRAINTS);
        }
    }

    /**
     * Set the timingInfo variable with the time specified
     * 
     * @param dateParser
     */
    private void setTimingInfo(List<DateGroup> dateParser) {
        this.timingInfo = dateParser.get(0);
    }

    public String getParsedTimingInfo() {
        return timingInfo.getDates().toString();
    }
    
    public boolean isTimeInferred() {
        return timingInfo.isTimeInferred();
    }
    
    public boolean isDateInferred() {
        return timingInfo.isDateInferred();
    }

    //@@author A0147092E
    public Date getParsedDateTime() {
        return timingInfo.getDates().get(0);
    }
    
    //@@author A0138455Y
    public DateGroup getTimingInfo() {
        return timingInfo;
    }

}