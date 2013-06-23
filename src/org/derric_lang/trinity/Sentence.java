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

import java.util.ArrayList;
import java.util.List;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;
import org.eclipse.imp.pdb.facts.ISourceLocation;

public class Sentence {
    
    public final boolean isValidated;
    public final List<StructureMatch> matches;
    
    public Sentence(boolean validated, List<StructureMatch> matches) {
        this.isValidated = validated;
        this.matches = matches;
    }
    
    public Selection getDataMatch(int offset) {
        for (StructureMatch s : matches) {
            if (isInside(s.inputLocation, offset)) {
                for (FieldMatch f : s.fields) {
                    if (isInside(f.inputLocation, offset)) {
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
            if (isInside(s.sequenceLocation, offset)) {
                sl.add(new Selection(s));
                continue;
            }
            if (isInside(s.structureLocation, offset)) {
                sl.add(new Selection(s));
                continue;
            }
            for (FieldMatch f : s.fields) {
                if (isInside(f.sourceLocation, offset)) {
                    sl.add(new Selection(s, f));
                    break;
                }
            }
        }
        return sl.toArray(new Selection[0]);
    }
    
    private boolean isInside(ISourceLocation loc, int offset) {
        return loc.getOffset() <= offset && (loc.getOffset() + loc.getLength()) > offset;
    }

}
