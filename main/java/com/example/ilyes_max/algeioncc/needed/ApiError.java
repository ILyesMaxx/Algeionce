package com.example.ilyes_max.algeioncc.needed;

import java.util.List;
import java.util.Map;

public class ApiError {

    String message;
    Map<String, List<String>> error;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getError() {
        return error;
    }
}
