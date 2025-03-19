package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.Map;

import static hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder.executorCanBeUndone;

/**
 * Task Management System (TMS) class.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class TMS {

    private final TaskRecorder taskRecorder;
    private final HistoryCommandRecorder commandRecorder;
    private final CriterionRecorder criterionRecorder;

    /**
     * Constructor for the TMS class.
     * Initializes the task recorder, command recorder, and criterion recorder.
     */
    public TMS() {
        this.taskRecorder = new TaskRecorder();
        this.commandRecorder = new HistoryCommandRecorder();
        this.criterionRecorder = new CriterionRecorder();
    }

    /**
     * Activates the TMS to receive and execute instructions.
     */
    public void activate() {
        System.out.println();
        System.out.println("***************************************************************************************************************************************");
        System.out.println("|| Welcome to use the Command-Line Based Task Management System!                                                                     ||");
        System.out.println("|| The system provide you diverse commands for creating, searching and managing your tasks.                                          ||");
        System.out.println("|| Should you have any question, please feel free to email to ruijie.wang@connect.polyu.hk.                                          ||");
        System.out.println("|| The TMS system is completed by Wang Ruijie, Zhu Jin Shun, Zeng Tianyi and Liu Yuyang, as the project of PolyU COMP2021, 2023 Fall ||");
        System.out.println("***************************************************************************************************************************************");
        System.out.println();
        System.out.println("Now you can input your commands here:");

        for ( ; ; ) {

            CommandLineScanner scanner = new CommandLineScanner();
            CommandLineParser parser = new CommandLineParser(scanner.getCommandLine());

            String instructionName = parser.getInstruction();
            Map<String, InstructionEnumeration> instructionMap = InstructionEnumeration.getInstructionMap();

            InstructionEnumeration instructionEnumeration = instructionMap.get(instructionName);

            if (instructionEnumeration != null) {
                System.out.println("Instruction found: " + instructionEnumeration);

                instructionEnumeration.executeInstruction(parser.getParameters(), taskRecorder, commandRecorder, criterionRecorder);  // 执行相关的方法

                if (executorCanBeUndone(instructionEnumeration.getExecutor())) {
                    HistoryCommandRecorder.CommandRecord commandRecord = new HistoryCommandRecorder.CommandRecord(instructionEnumeration.getExecutor(), parser.getParameters());
                    commandRecorder.pushExecuted(commandRecord);
                }

                System.out.println();

            } else {
                System.out.println("Instruction not found");
            }

        }

    }

}
