package org.infuse.trinity;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

public class Selection {
    
    public final StructureMatch structure;
    public final FieldMatch field;
    
    public Selection(StructureMatch structure) {
        this.structure = structure;
        this.field = null;
    }
    
    public Selection(StructureMatch structure, FieldMatch field) {
        this.structure = structure;
        if (!structure.fields.contains(field)) {
            throw new RuntimeException("Field " + field + " must be in structure " + structure);
        }
        this.field = field;
    }

}
