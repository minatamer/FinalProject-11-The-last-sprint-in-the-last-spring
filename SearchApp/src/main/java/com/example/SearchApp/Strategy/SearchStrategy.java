package com.example.SearchApp.Strategy;

import java.util.List;

public interface SearchStrategy<T> {
    List<T> search(List<T> items, String query);

}
