package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * This class represents a primitive task in the task management system.
 * It extends the Task class and provides a constructor to initialize the primitive task.
 *
 * @author Wang Ruijie
 * @version 3.0
 */
public class PrimitiveTask extends Task{

    /**
     * Constructs a PrimitiveTask with the specified name, description, duration, and direct prerequisites.
     *
     * @param name                the name of the primitive task
     * @param description         the description of the primitive task
     * @param duration            the duration of the primitive task
     * @param directPrerequisites the direct prerequisites of the primitive task
     */
    public PrimitiveTask(String name, String description, double duration, Task[] directPrerequisites) {
        super(name, description, duration, directPrerequisites, new Task[0], true);
    }

}