package com.example.SearchApp.Command;

import java.util.List;

public interface FilterCommand<T> {
    List<T> execute(List<T> items);
    List<T> undo();
}
