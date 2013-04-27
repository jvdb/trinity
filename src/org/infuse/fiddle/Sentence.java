package org.infuse.fiddle;

import java.util.List;

import org.derric_lang.validator.interpreter.StructureMatch;

public class Sentence {
    
    public final boolean isValidated;
    public final List<StructureMatch> matches;
    
    public Sentence(boolean validated, List<StructureMatch> matches) {
        this.isValidated = validated;
        this.matches = matches;
    }
    
    public StructureMatch getDataMatch(int offset) {
        for (StructureMatch m : matches) {
            if (m.inputLocation.getOffset() <= offset && (m.inputLocation.getOffset() + m.inputLocation.getLength()) > offset) {
                return m;
            }
        }
        return null;
    }
    
    public StructureMatch getCodeMatch(int offset) {
        for (StructureMatch m : matches) {
            if ((m.sequenceLocation.getOffset() <= offset && (m.sequenceLocation.getOffset() + m.sequenceLocation.getLength()) > offset) || (m.structureLocation.getOffset() <= offset && (m.structureLocation.getOffset() + m.structureLocation.getLength()) > offset)) {
                return m;
            }
        }
        return null;
    }

}
