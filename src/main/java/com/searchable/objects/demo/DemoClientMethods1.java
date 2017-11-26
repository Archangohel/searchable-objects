package com.searchable.objects.demo;

import com.searchable.objects.core.annotations.ElementTypes;
import com.searchable.objects.core.annotations.UpdateToIndex;
import org.springframework.stereotype.Component;

/**
 * @auther Archan on 25/11/17.
 */
@Component
@UpdateToIndex(elementsToProcess = {ElementTypes.ARGUMENT, ElementTypes.RETURN})
public class DemoClientMethods1 {

    public void processArgs(SearchableEntity arg1) {
        return;
    }

    public SearchableEntity processReturnValue() {
        return new SearchableEntity(2222l, " ReturnedValue from public SearchableEntity processReturnValue() {\n");
    }

    public SearchableEntity process(SearchableEntity arg1) {
        return new SearchableEntity(2223l, "ReturnedValue from public SearchableEntity process(SearchableEntity arg1)");
    }

    public SearchableEntity processWithoutAdvice(SearchableEntity arg1) {
        return new SearchableEntity(2224l, "ReturnedValue from public SearchableEntity processWithoutAdvice(SearchableEntity arg1)");
    }
}
