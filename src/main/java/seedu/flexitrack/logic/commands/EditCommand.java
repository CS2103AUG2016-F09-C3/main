package seedu.flexitrack.logic.commands;

import static seedu.flexitrack.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Stack;

import seedu.flexitrack.commons.core.Messages;
import seedu.flexitrack.commons.core.UnmodifiableObservableList;
import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.model.tag.UniqueTagList;
import seedu.flexitrack.model.task.DateTimeInfo;
import seedu.flexitrack.model.task.DateTimeInfoParser;
import seedu.flexitrack.model.task.Name;
import seedu.flexitrack.model.task.ReadOnlyTask;
import seedu.flexitrack.model.task.Task;
import seedu.flexitrack.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.flexitrack.model.task.UniqueTaskList.IllegalEditException;
import seedu.flexitrack.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Edits a task identified using it's last displayed index from the FlexiTrack.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_SHORTCUT = "e";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ", Shortcut [" + COMMAND_SHORTCUT + "]"
            + ": Edits the specified task attributes of the task identified by the index number used in the last task listing.\n"
            + "Parameters to edit an event: [index] (must be a positive integer) from/ [starting time] to/ [ending time]\n"
            + "Example: " + COMMAND_WORD + " 1 " + "from/ 01062016 to/ 01/072016\n"
            + "Parameters to edit a task: [index] (must be a positive integer) by/ [due date]\n" + "Example: "
            + COMMAND_WORD + " 1 " + "by/ 01062016";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited: %1$s";

    public final int targetIndex;
    public final String[] arguments;

    private static Stack<ReadOnlyTask> storeOldDataChanged = new Stack<ReadOnlyTask>(); 
    private static Stack<ReadOnlyTask> storeNewDataChanged = new Stack<ReadOnlyTask>(); 

    public EditCommand(int targetIndex, String[] arguments) {
        this.targetIndex = targetIndex;
        this.arguments = arguments;
    }
 
    /** 
     * Constructor for the undo method
     */
    public EditCommand() {
        this.targetIndex = 0 ;
        this.arguments = null;
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        Task editedTask = null;
        String duration = null; 
        
        try {
            Task oldData = new Task(new Name (lastShownList.get(targetIndex - 1).getName().toString()), 
                    new DateTimeInfo (lastShownList.get(targetIndex - 1).getDueDate().toString()), 
                    new DateTimeInfo ( lastShownList.get(targetIndex - 1).getStartTime().toString()), 
                    new DateTimeInfo (lastShownList.get(targetIndex - 1).getEndTime().toString()), 
                    new UniqueTagList (lastShownList.get(targetIndex - 1).getTags()));
            if (lastShownList.get(targetIndex - 1).getIsDone()){
                oldData.getName().setAsMark();
            }
            storeOldDataChanged.add(oldData); 
            editedTask = model.editTask(targetIndex - 1, arguments);
        } catch (TaskNotFoundException pnfe) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        } catch (IllegalEditException iee) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            assert false : "Illegal value entered";
        }
        
        if (editedTask.getIsEvent()) {
            duration =  DateTimeInfo.durationOfTheEvent(editedTask.getStartTime().toString(),
                    editedTask.getEndTime().toString());
        } else {
            duration = "";
        }
        
        recordCommand("edit"); 
        storeNewDataChanged.add(editedTask); 
        
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, lastShownList.get(targetIndex - 1).getName())
                + "\n" + duration);
    }
    
    @Override
    public void executeUndo() {
        Task toDelete = new Task (storeNewDataChanged.peek()); 
        Task toAddBack = new Task (storeOldDataChanged.peek());

        try {
            model.deleteTask(toDelete);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
        
        try {
            model.addTask(toAddBack);
        } catch (DuplicateTaskException e) {
            indicateAttemptToExecuteIncorrectCommand();
        }
        
        model.indicateFlexiTrackerChanged();

        storeNewDataChanged.pop();
        storeOldDataChanged.pop();
    }
}
