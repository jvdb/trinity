package org.infuse.fiddle;

import java.util.ArrayList;
import java.util.List;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

public class Sentence {
    
    public final boolean isValidated;
    public final List<StructureMatch> matches;
    
    public Sentence(boolean validated, List<StructureMatch> matches) {
        this.isValidated = validated;
        this.matches = matches;
    }
    
    public Selection getDataMatch(int offset) {
        for (StructureMatch s : matches) {
            if (s.inputLocation.getOffset() <= offset && (s.inputLocation.getOffset() + s.inputLocation.getLength()) > offset) {
                for (FieldMatch f : s.fields) {
                    if (f.inputLocation.getOffset() <= offset && (f.inputLocation.getOffset() + f.inputLocation.getLength()) > offset) {
                        return new Selection(s, f);
                    }
                }
                return new Selection(s);
            }
        }
        return null;
    }
    
    public Selection[] getCodeMatches(int offset) {
        ArrayList<Selection> sl = new ArrayList<Selection>();
        for (StructureMatch s : matches) {
            if ((s.sequenceLocation.getOffset() <= offset && (s.sequenceLocation.getOffset() + s.sequenceLocation.getLength()) > offset)) {
                sl.add(new Selection(s));
                continue;
            }
            if ((s.structureLocation.getOffset() <= offset && (s.structureLocation.getOffset() + s.structureLocation.getLength()) > offset)) {
                sl.add(new Selection(s));
                continue;
            }
            for (FieldMatch f : s.fields) {
                if (f.sourceLocation.getOffset() <= offset && (f.sourceLocation.getOffset() + f.sourceLocation.getLength()) > offset) {
                    sl.add(new Selection(s, f));
                    break;
                }
            }
        }
        return sl.toArray(new Selection[0]);
    }

}
