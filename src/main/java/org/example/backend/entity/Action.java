package org.example.backend.entity;

public class Action {

    public enum Task {
        UPDATE_CONTENT,
        UPDATE_START_DATE,
        UPDATE_END_DATE,
        UPDATE_STATUS,
        UPDATE_PRIORITY,
        ADD_SUB_TASK,
        REMOVE_SUB_TASK
    }

    public enum Plan {
        UPDATE_NAME,
        UPDATE_DESCRIPTION,
        UPDATE_IMAGE_URL,
        UPDATE_PRIORITY,
        UPDATE,
        DELETE,
        CREATE
    }

    public enum Tag {
        ADD_TAG,
        REMOVE_TAG
    }

    public enum Member {
        ADD_MEMBER,
        REMOVE_MEMBER,
        UPDATE_MEMBER_ROLE
    }
}
