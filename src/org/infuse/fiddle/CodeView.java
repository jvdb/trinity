package org.infuse.fiddle;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class CodeView extends JTextPane {
    
    private DefaultHighlighter.DefaultHighlightPainter _shl = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
    private DefaultHighlighter.DefaultHighlightPainter _fhl = new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY);
    
    public CodeView() {
        super();
        setFont(new Font("Courier New", getFont().getStyle(), getFont().getSize()));
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

}
