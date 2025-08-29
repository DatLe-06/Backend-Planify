package org.example.backend.constant;

public class Action {

    public enum Task {
        DELETE, CREATE, UPDATE, SOFT_DELETE, HARD_DELETE, RESTORE, REMOVE_SUB_TASK
    }

    public enum Plan {
        UPDATE, DELETE, SOFT_DELETE, HARD_DELETE, RESTORE, CREATE
    }
}
