package com.example.SearchApp.Command;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
@Component
public class FilterInvoker<T> {
    private FilterCommand<T> command;
    private List<FilterCommand<T>> commandHistory = new ArrayList<>();

    public void setCommand(FilterCommand<T> command) {
        this.command = command;
    }

    public List<T> filter(List<T> items) {
        commandHistory.add(command);
        return command.execute(items);
    }

    public List<FilterCommand<T>> getCommandHistory() {
        return commandHistory;}

    public void setCommandHistory(List<FilterCommand<T>> commandHistory) {
        this.commandHistory = commandHistory;
    }
}
