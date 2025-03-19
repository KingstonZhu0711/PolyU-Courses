package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * Implements the InstructionExecutor interface to handle the execution of the "Store" instruction.
 * This executor is responsible for storing tasks to a file based on the provided address.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public class StoreExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Constructs a StoreExecutor with the initial status set to false.
     */
    public StoreExecutor() {
        this.status = false;
    }

    /**
     * Executes the "Store" instruction by storing tasks to a file based on the provided address.
     *
     * @param parameters       The parameters for executing the instruction.
     * @param taskRecorder     The task recorder used to record and manage tasks.
     * @param commandRecorder  The command recorder used to record executed commands.
     * @param criterionRecorder The criterion recorder used to record and manage criteria.
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 1) {
            System.out.println("Please provide a valid address.");
            return;
        }

        String filePath = parameters[0];
        File file = new File(filePath);
        if (file.isDirectory()) {
            filePath = filePath + File.separator + "default.txt";
        }

            try (FileWriter writer = new FileWriter(filePath)) {

                Task[] allTasks = taskRecorder.getAllTasks();
                writer.write("TASK: \n");

                for (Task task : allTasks) {

                    writer.write("task: ");
                    writer.write(task.getName() + " ");
                    writer.write(task.getDescription() + " ");
                    writer.write(task.getDuration() + " ");

                    if (task.getDirectPrerequisites().length != 0) {
                        for (Task prerequisite : task.getDirectPrerequisites()) {
                            writer.write(prerequisite.getName() + ",");
                        }
                    } else {
                        writer.write(",");
                    }

                    if (task.getType()) {
                        writer.write(" " + "Primitive" + " ");
                        writer.write("," + "\n");
                    }

                    if (!task.getType()) {
                        writer.write(" " + "Composite" + " ");
                        for (Task subtask : task.getSubTasks()) {
                            writer.write(subtask.getName() + ",");
                        }
                        writer.write("\n");
                    }

                }

                Criterion[] allCriteria = criterionRecorder.getAllCriteria();
                writer.write("CRITERION: \n");

                for (Criterion criterion : allCriteria) {

                    writer.write("criterion: ");
                    writer.write(criterion.getName() + " ");
                    writer.write(criterion.getType() + " ");

                    switch (criterion.getType()) {
                        case "Basic":
                            writer.write(criterion.getProperty() + " ");
                            writer.write(criterion.getOp() + " ");
                            writer.write(String.valueOf(criterion.getValue()));
                            break;
                        case "Negated":
                            writer.write(criterion.getFirstSubCriterion().getName());
                            break;
                        case "Binary":
                            writer.write(criterion.getFirstSubCriterion().getName() + " ");
                            writer.write(criterion.getSecondSubCriterion().getName() + " ");
                            writer.write(criterion.getLogicOp());
                    }

                    writer.write("\n");

                }

                writer.flush();

            } catch (IOException e) {
                System.out.println("The file does not exist");
            }
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    @Override
    public boolean getStatus() {
        return this.status;
    }
}
