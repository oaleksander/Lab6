package com.company.commands;

/**
 * Command that every program action implements
 */
public interface CommandAction {

    /**
     * Command label
     *
     * @return string that user has to input to use the command
     */
    String getLabel();

    /**
     * Command argument format
     *
     * @return argument format
     */
    default String getArgumentLabel() {
        return "";
    }

    /**
     * Command description
     *
     * @return description
     */
    String getDescription();

    /**
     * Execute the command
     *
     * @param argument command arguments
     * @return Command execution result
     */
    String execute(String argument);

    /**
     * Execute the command without arguments
     * Should not usually be overridden
     *
     * @return Command execution result
     */
    default String execute() {
        return execute("");
    }

}
