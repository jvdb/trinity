package org.infuse.fiddle;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.SourceLocation;
import org.derric_lang.validator.interpreter.StructureMatch;
import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.values.ValueFactoryFactory;

public class Interpreter {
    
    private final String DERRIC_SRC_PATH = "../derric/src";
    private final String MODULE_NAME = "lang::derric::testparse";
    private final String EXECUTE_FUNCTION_NAME = "execute";
    private final String COLOR_FUNCTION_NAME = "getSyntaxMap";
    
    private final Evaluator _evaluator;
    private final IValueFactory _values;
    
    public Interpreter() {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        _values = ValueFactoryFactory.getValueFactory();
        _evaluator = new Evaluator(_values, new PrintWriter(System.err), new PrintWriter(System.out), root, heap);
        _evaluator.addRascalSearchPath(new File(DERRIC_SRC_PATH).toURI());
        _evaluator.doImport(null, MODULE_NAME);
    }
    
    public Sentence run(String derricPath, String dataPath) {
        ISourceLocation derricLoc = _values.sourceLocation(derricPath);
        ISourceLocation dataLoc = _values.sourceLocation(dataPath);
        ITuple ret = (ITuple)_evaluator.call(EXECUTE_FUNCTION_NAME, new IValue[] { derricLoc, dataLoc });
        boolean validated = ((IBool)ret.get(0)).getValue();
        List<StructureMatch> matches = buildMatches((IList)ret.get(1));
        return new Sentence(validated, matches);
    }

    private List<StructureMatch> buildMatches(IList matches) {
        ArrayList<StructureMatch> out = new ArrayList<StructureMatch>();
        for (int i = 0; i < matches.length(); i++) {
            ITuple item = (ITuple)matches.get(i);
            String structureName = ((IString)item.get(0)).getValue();
            SourceLocation seqLoc = new SourceLocation(((ISourceLocation)item.get(1)).getURI(), ((ISourceLocation)item.get(1)).getOffset(), ((ISourceLocation)item.get(1)).getLength());
            SourceLocation strLoc = new SourceLocation(((ISourceLocation)item.get(2)).getURI(), ((ISourceLocation)item.get(2)).getOffset(), ((ISourceLocation)item.get(2)).getLength());
            SourceLocation datLoc = new SourceLocation(((ISourceLocation)item.get(3)).getURI(), ((ISourceLocation)item.get(3)).getOffset(), ((ISourceLocation)item.get(3)).getLength());
            IList fieldData = (IList)item.get(4);
            ArrayList<FieldMatch> fields = new ArrayList<FieldMatch>();
            for (int j = 0; j < fieldData.length(); j++) {
                ITuple fitem = (ITuple)fieldData.get(j);
                String fname = ((IString)fitem.get(0)).getValue();
                SourceLocation fstrLoc = new SourceLocation(((ISourceLocation)fitem.get(1)).getURI(), ((ISourceLocation)fitem.get(1)).getOffset(), ((ISourceLocation)fitem.get(1)).getLength());
                SourceLocation fdatLoc = new SourceLocation(((ISourceLocation)fitem.get(2)).getURI(), ((ISourceLocation)fitem.get(2)).getOffset(), ((ISourceLocation)fitem.get(2)).getLength());
                fields.add(new FieldMatch(fname, fstrLoc, fdatLoc));
            }
            out.add(new StructureMatch(structureName, seqLoc, strLoc, datLoc, fields));
        }
        return out;
    }
    
    public class Region {
        public final int offset;
        public final int length;
        public Region(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }
    }
    
    public HashMap<String, Set<Region>> getColorMap(String source) {
        ITuple ret = (ITuple)_evaluator.call(COLOR_FUNCTION_NAME, _values.string(source));
        if (!((IBool)ret.get(0)).getValue()) { return null; }
        HashMap<String, Set<Region>> out = new HashMap<String, Set<Region>>();
        IMap map = (IMap)ret.get(1);
        for (IValue k : map) {
            String key = ((IString)k).getValue();
            HashSet<Region> value = new HashSet<Region>();
            ISet regions = (ISet)map.get(k);
            for (IValue r : regions) {
                value.add(new Region(((ISourceLocation)r).getOffset(), ((ISourceLocation)r).getLength()));
            }
            out.put(key, value);
        }
        return out;
    }

}
