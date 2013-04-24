package org.infuse.hexview;

import java.util.List;

import org.derric_lang.validator.interpreter.StructureMatch;

public class Sentence {
    
    public final boolean isValidated;
    public final List<StructureMatch> matches;
    
    public Sentence(boolean validated, List<StructureMatch> matches) {
        this.isValidated = validated;
        this.matches = matches;
    }

}
