package com.searchable.objects.demo;

import com.searchable.objects.core.annotations.DeleteFromIndex;
import com.searchable.objects.core.annotations.ElementTypes;
import com.searchable.objects.core.annotations.UpdateToIndex;
import org.springframework.stereotype.Component;

/**
 * @auther Archan on 26/11/17.
 */

@Component
public class ClientCode {

    @UpdateToIndex(elementsToProcess = {ElementTypes.ARGUMENT})
    public void processArgsForUpdate(Entity1 arg1) {
        return;
    }

    @UpdateToIndex(elementsToProcess = {ElementTypes.RETURN})
    public Entity1 processReturnValueForUpdate() {
        return Entity1.newInstance();
    }

    @UpdateToIndex(elementsToProcess = {ElementTypes.RETURN, ElementTypes.ARGUMENT})
    public Entity1 processArgsAndReturnValueForUpdate(Entity1 arg1) {
        return Entity1.newInstance();
    }

    public Entity1 noAdvice(Entity1 arg1) {
        return Entity1.newInstance();
    }

    @DeleteFromIndex(elementsToProcess = {ElementTypes.RETURN})
    public Entity1 processReturnForDelete(Entity1 arg1) {
        return arg1;
    }

    @DeleteFromIndex(elementsToProcess = {ElementTypes.ARGUMENT})
    public Entity1 processArgsForDelete(Entity1 arg1) {
        return arg1;
    }

    @DeleteFromIndex(elementsToProcess = {ElementTypes.RETURN, ElementTypes.ARGUMENT})
    public Entity1 processArgsAndReturnForDelete(Entity1 arg1) {
        return Entity1.newInstance();
    }


}
