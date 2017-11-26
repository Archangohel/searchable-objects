package com.searchable.objects.utils.jms;

import java.io.Serializable;

/**
 * @auther Archan on 25/11/17.
 */
public class JmsMessage implements Serializable {

    public enum ActionType {SAVE, DELETE}

    ;
    private Object object;
    private ActionType actionType;

    public JmsMessage(Object object, ActionType actionType) {
        this.object = object;
        this.actionType = actionType;
    }

    public Object getObject() {
        return object;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public boolean isSaveAction() {
        return ActionType.SAVE.equals(actionType);
    }

    public boolean isDeleteAction() {
        return ActionType.DELETE.equals(actionType);
    }
}
