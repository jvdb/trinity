/* Copyright 2013 Netherlands Forensic Institute and
                       Centrum Wiskunde & Informatica

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.derric_lang.trinity;

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
