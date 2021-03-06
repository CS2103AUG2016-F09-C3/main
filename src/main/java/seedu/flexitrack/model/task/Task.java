package seedu.flexitrack.model.task;

import java.util.Objects;

import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.commons.util.CollectionUtil;

/**
 * Represents a Person in the address book. Guarantees: details are present and
 * not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Name name;
    private DateTimeInfo dueDate;
    private DateTimeInfo startTime;
    private DateTimeInfo endTime;
    private boolean isEvent;
    private boolean isTask;
    private boolean isDone = false;
    private boolean isBlock = false;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, DateTimeInfo dueDate, DateTimeInfo startTime, DateTimeInfo endTime) {
        assert !CollectionUtil.isAnyNull(name);
        this.name = name;
        this.dueDate = dueDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isTask = dueDate.isDateNull() ? false : true;
        this.isEvent = startTime.isDateNull() ? false : true;
        this.isDone = name.getIsDone();
        this.isBlock = name.getIsDone();
        updateTimeFormat();
    }

    // @@ author A0127686R
    /**
     * To set the time if they are inferred. Starting and DueDate will start at
     * 8 and ending time will end at 17:00
     * 
     */
    private void updateTimeFormat() {
        if (isTask) {
            this.dueDate.formatStartOrDueDateTime();
        } else if (isEvent) {
            this.startTime.formatStartOrDueDateTime();
            this.endTime.formatEndTime(this.startTime);
        }
    }
    // @@ author

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDueDate(), source.getStartTime(), source.getEndTime());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean getIsTask() {
        return isTask;
    }

    @Override
    public boolean getIsEvent() {
        return isEvent;
    }

    @Override
    public boolean getIsDone() {
        return name.getIsDone();
    }

    @Override
    public boolean getIsBlock() {
        return name.getIsBlock();
    }

    public void setIsBlock() {
        name.setBlock();
    }

    @Override
    public DateTimeInfo getDueDate() {
        return dueDate;
    }

    @Override
    public DateTimeInfo getStartTime() {
        return startTime;
    }

    @Override
    public DateTimeInfo getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                        && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing
        // your own
        return Objects.hash(name, dueDate, startTime, endTime, isTask, isEvent);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    // @@author A0138455Y
    private void setIsDone(boolean isDone) {
        if (isDone && !this.isDone) {
            name.setAsMark();
        } else if (!isDone && this.isDone) {
            name.setAsUnmark();
        }
    }

    public void markTask(boolean isDone) throws IllegalValueException {
        this.isDone = this.name.getIsDone();
        if (this.isDone && isDone) {
            throw new IllegalValueException("Task already marked!");
        } else if (!this.isDone && !isDone) {
            throw new IllegalValueException("Task already unmarked!");
        } else {
            setIsDone(isDone);
        }
    }
    // @@author

    public void setName(String name) {
        this.name.setName(name);
    }

    public void setDueDate(String dueDate) throws IllegalValueException {
        this.dueDate = new DateTimeInfo(dueDate);
    }

    public void setStartTime(String startTime) throws IllegalValueException {
        this.startTime = new DateTimeInfo(startTime);
    }

    public void setEndTime(String endTime) throws IllegalValueException {
        this.endTime = new DateTimeInfo(endTime);
    }

    public void setIsTask(Boolean bool) {
        this.isTask = bool;
    }

    public void setIsEvent(Boolean bool) {
        this.isEvent = bool;
    }

    public String getStartingTimeInString() {
        return this.startTime.toString();
    }

    public String getEndingTimeInString() {
        return this.endTime.toString();
    }

    public String getDueDateInString() {
        return this.dueDate.toString();
    }

}
