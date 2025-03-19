package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.InstructionExecutor;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Implements the InstructionExecutor interface to handle the execution of the "Load" instruction.
 * This executor is responsible for loading tasks from a file and populating the task recorder and command recorder accordingly.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public class LoadExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Constructs a LoadExecutor with the initial status set to false.
     */
    public LoadExecutor() {
        this.status = false;
    }

    /**
     * Retrieves the status of the LoadExecutor.
     *
     * @return The status of the LoadExecutor.
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
    }

    /**
     * Executes the "Load" instruction by loading tasks from a file and populating the task recorder and command recorder.
     *
     * @param parameters       The parameters for executing the instruction.
     * @param taskRecorder     The task recorder used to record and manage tasks.
     * @param commandRecorder  The command recorder used to record executed commands.
     * @param criterionRecorder The criterion recorder used to record and manage criteria.
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 1) {
            System.out.println("Please provide a valid file path");
            return;
        }

        String filePath = parameters[0];
        taskRecorder.clearRecorder();
        commandRecorder.clearRecorder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("task: ")) {
                    String t = "task: ";
                    int prefixLength = t.length();
                    String taskLine = line.substring(prefixLength); // Remove "task: " prefix
                    String[] taskInformation = taskLine.split(" ");

                    if (Objects.equals(taskInformation[4], "Primitive")) {
                        String[] newParameters = new String[4];
                        System.arraycopy(taskInformation, 0, newParameters, 0,4);
                        CreatePrimitiveTaskExecutor newExecutor = new CreatePrimitiveTaskExecutor();
                        newExecutor.executeInstruction(newParameters, taskRecorder, commandRecorder, criterionRecorder);
                        HistoryCommandRecorder.CommandRecord newCommandRecord = new HistoryCommandRecorder.CommandRecord(newExecutor, newParameters);
                        commandRecorder.pushExecuted(newCommandRecord);
                        System.out.println("The primitive task " + taskInformation[0] +" is loaded");
                    }

                    if (Objects.equals(taskInformation[4], "Composite")) {
                        String[] newParameters = new String[3];
                        newParameters[0] = taskInformation[0];
                        newParameters[1] = taskInformation[1];
                        newParameters[2] = taskInformation[5];
                        CreateCompositeTaskExecutor newExecutor = new CreateCompositeTaskExecutor();
                        newExecutor.executeInstruction(newParameters, taskRecorder, commandRecorder, criterionRecorder);
                        HistoryCommandRecorder.CommandRecord newCommandRecord = new HistoryCommandRecorder.CommandRecord(newExecutor, newParameters);
                        commandRecorder.pushExecuted(newCommandRecord);
                        System.out.println("The composite task " + taskInformation[0] +" is loaded");
                    }

                }

                if (line.startsWith("criterion: ")) {
                    String c = "criterion: ";
                    int prefixLength = c.length();
                    String criterionLine = line.substring(prefixLength); // Remove "criterion: " prefix
                    String[] criterionInformation = criterionLine.split(" ");
                    switch(criterionInformation[1]) {
                        case "Basic":
                            String[] newParametersBasic = new String[4];
                            newParametersBasic[0] = criterionInformation[0];
                            newParametersBasic[1] = criterionInformation[2];
                            newParametersBasic[2] = criterionInformation[3];
                            newParametersBasic[3] = criterionInformation[4];
                            DefineBasicCriterionExecutor newExecutorBasic = new DefineBasicCriterionExecutor();
                            newExecutorBasic.executeInstruction(newParametersBasic, taskRecorder, commandRecorder, criterionRecorder);
                            HistoryCommandRecorder.CommandRecord newCommandRecordBasic = new HistoryCommandRecorder.CommandRecord(newExecutorBasic, newParametersBasic);
                            commandRecorder.pushExecuted(newCommandRecordBasic);
                            System.out.println("The basic criterion " + criterionInformation[0] +" is loaded");
                            break;

                        case "Negated":
                            String[] newParametersNegated = new String[2];
                            newParametersNegated[0] = criterionInformation[0];
                            newParametersNegated[1] = criterionInformation[2];
                            DefineNegatedCriterionExecutor newExecutorNegated = new DefineNegatedCriterionExecutor();
                            newExecutorNegated.executeInstruction(newParametersNegated, taskRecorder, commandRecorder, criterionRecorder);
                            HistoryCommandRecorder.CommandRecord newCommandRecordNegated = new HistoryCommandRecorder.CommandRecord(newExecutorNegated, newParametersNegated);
                            commandRecorder.pushExecuted(newCommandRecordNegated);
                            System.out.println("The negated criterion " + criterionInformation[0] +" is loaded");
                            break;

                        case "Binary":
                            String[] newParametersBinary = new String[4];
                            newParametersBinary[0] = criterionInformation[0];
                            newParametersBinary[1] = criterionInformation[2];
                            newParametersBinary[2] = criterionInformation[4];
                            newParametersBinary[3] = criterionInformation[3];
                            DefineBinaryCriterionExecutor newExecutorBinary = new DefineBinaryCriterionExecutor();
                            newExecutorBinary.executeInstruction(newParametersBinary, taskRecorder, commandRecorder, criterionRecorder);
                            HistoryCommandRecorder.CommandRecord newCommandRecordBinary = new HistoryCommandRecorder.CommandRecord(newExecutorBinary, newParametersBinary);
                            commandRecorder.pushExecuted(newCommandRecordBinary);
                            System.out.println("The binary criterion " + criterionInformation[0] +" is loaded");
                            break;

                    }
                }

            }

        } catch (IOException e) {
            System.out.println("Wrong filepath");
        }

    }
}
