package com.mattsmeets.macrokey.model.exclusion;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.mattsmeets.macrokey.model.CommandInterface;

import java.util.Arrays;
import java.util.Optional;

public class CommandExclusion implements ExclusionStrategy {

    public static final String[] includedFields = {"type", "command"};

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        Optional inter = Arrays.stream(f.getDeclaringClass().getInterfaces()).findFirst();
        if(inter.isPresent() && inter.get() == CommandInterface.class) {
            long count = Arrays.stream(includedFields).filter((field) -> field.equalsIgnoreCase(f.getName())).count();
            return count == 0;
        }

        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
