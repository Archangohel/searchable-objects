package com.searchable.objects.demo;

import com.searchable.objects.core.annotations.DeleteFromIndex;
import com.searchable.objects.core.annotations.ElementTypes;
import com.searchable.objects.core.annotations.UpdateToIndex;
import org.springframework.stereotype.Component;

/**
 * @auther Archan on 25/11/17.
 */
@Component
public class DemoClientMethods {

    @UpdateToIndex(elementsToProcess = {ElementTypes.ARGUMENT})
    public void processArgs(SearchableEntity arg1) {
        return;
    }

    @UpdateToIndex(elementsToProcess = {ElementTypes.RETURN})
    public SearchableEntity processReturnValue() {
        return new SearchableEntity(1222l, " ReturnedValue from public SearchableEntity processReturnValue() {\n");
    }

    @UpdateToIndex(elementsToProcess = {ElementTypes.RETURN, ElementTypes.ARGUMENT})
    public SearchableEntity process(SearchableEntity arg1) {
        return new SearchableEntity(1223l, "ReturnedValue from public SearchableEntity process(SearchableEntity arg1)");
    }

    public SearchableEntity processWithoutAdvice(SearchableEntity arg1) {
        return new SearchableEntity(1224l, "ReturnedValue from public SearchableEntity processWithoutAdvice(SearchableEntity arg1)");
    }

    @DeleteFromIndex(elementsToProcess = {ElementTypes.RETURN})
    public SearchableEntity deleteMethod(SearchableEntity arg1) {
        return new SearchableEntity(1223l, "ReturnedValue from public SearchableEntity process(SearchableEntity arg1)");
    }
}
