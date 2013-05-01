package org.infuse.fiddle;

import org.derric_lang.validator.interpreter.StructureMatch;

public class Node {
    
    public final StructureMatch match;
    
    public Node(StructureMatch match) {
        this.match = match;
    }
    
    @Override
    public String toString() {
        return match.name;
    }

}
