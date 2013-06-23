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

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.derric_lang.trinity.Interpreter.Region;
import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class CodeView extends JTextPane {
    
    private DefaultHighlighter.DefaultHighlightPainter _shl = new DefaultHighlighter.DefaultHighlightPainter(MainFrame.BG_SEL1);
    private DefaultHighlighter.DefaultHighlightPainter _fhl = new DefaultHighlighter.DefaultHighlightPainter(MainFrame.BG_SEL2);
    private final HashMap<String, Style> _colors;
    
    public CodeView() {
        super();
        setFont(new Font("Monospaced", getFont().getStyle(), getFont().getSize()));
        setBackground(MainFrame.BG);
        setForeground(MainFrame.FG);
        
        _colors = new HashMap<String, Style>();
        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        setDocument(doc);
        addStyle(sc, "callback", MainFrame.RED);
        addStyle(sc, "comment", MainFrame.COMMENT);
        addStyle(sc, "keyword", MainFrame.BLUE);
    }
    
    private void addStyle(StyleContext sc, String name, Color c) {
        Style style = sc.addStyle(name, null);
        StyleConstants.setForeground(style, c);
        _colors.put(name, style);
    }
    
    public void addHighlight(StructureMatch match) throws UnsupportedOperationException, BadLocationException {
        getHighlighter().addHighlight(match.sequenceLocation.getOffset(), match.sequenceLocation.getOffset() + match.sequenceLocation.getLength(), _shl);
        getHighlighter().addHighlight(match.structureLocation.getOffset(), match.structureLocation.getOffset() + match.structureLocation.getLength(), _shl);
    }
    
    public void addHighlight(FieldMatch match) throws UnsupportedOperationException, BadLocationException {
        getHighlighter().addHighlight(match.sourceLocation.getOffset(), match.sourceLocation.getOffset() + match.sourceLocation.getLength(), _fhl);
    }
    
    public void clearHighlights() {
        getHighlighter().removeAllHighlights();
    }
    
    public void setViewable(Selection selection) {
        if (selection != null) {
            try {
                scrollRectToVisible(modelToView(selection.field == null ? selection.structure.structureLocation.getOffset() : selection.field.sourceLocation.getOffset()));
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void colorize(HashMap<String, Set<Region>> map) {
        for (String k : map.keySet()) {
            if (_colors.containsKey(k)) {
                Style s = _colors.get(k);
                for (Region r : map.get(k)) {
                    ((DefaultStyledDocument)getDocument()).setCharacterAttributes(r.offset, r.length, s, true);
                }
            }
        }
    }

}
