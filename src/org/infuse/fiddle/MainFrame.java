package org.infuse.fiddle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.swing.CodeEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultHighlighter;

import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    
    private final Interpreter _interpreter;

    private HexViewTable _hexView;
    private CodeEditorPane _codeView;
    private SentenceViewTable _sentenceView;

    private File _codeFile;
    private File _dataFile;
    private Sentence _current;
    
    private DefaultHighlighter.DefaultHighlightPainter _hl = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
    
    public MainFrame() throws IOException {
        _interpreter = new Interpreter();
        initGUI();
    }
    
    private void initGUI() throws IOException {
        // Main window
        setTitle("Fiddle");
        setSize(800, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Hex view
        _hexView = new HexViewTable();
        JScrollPane tsp = new JScrollPane(_hexView);
        tsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tsp.setPreferredSize(new Dimension(_hexView.getPreferredSize().width + 24, _hexView.getPreferredSize().height));
        //add(tsp, BorderLayout.WEST);
        ListSelectionListener sl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (_hexView.getSelectedColumn() > 0 && _hexView.getSelectedColumn() <= HexViewTableModel.WIDTH) {
                        int offset = (_hexView.getSelectedRow() * HexViewTableModel.WIDTH) + (_hexView.getSelectedColumn() - 1);
                        StructureMatch match = _current.getDataMatch(offset);
                        setHighlightsFromHexView(match);
                    } else {
                        clearHighlights();
                    }
                    _hexView.repaint();
                }
            } 
        };
        _hexView.getSelectionModel().addListSelectionListener(sl);
        _hexView.getColumnModel().getSelectionModel().addListSelectionListener(sl);
        
        // Code view
        _codeView = new CodeEditorPane();
        _codeView.setKeywordColor(getDerricColors());
        _codeView.setVerticalLineAtPos(80);
        JScrollPane esp = new JScrollPane(_codeView);
        esp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //add(esp, BorderLayout.CENTER);
        _codeView.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (_current != null) {
                    StructureMatch[] matches = _current.getCodeMatches(e.getDot());
                    setHighlightsFromCodeView(matches);
                    _hexView.repaint();
                }
            }
        });
        
        JSplitPane splitContent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tsp, esp);
        
        // Sentence view
        _sentenceView = new SentenceViewTable();
        JScrollPane osp = new JScrollPane(_sentenceView);
        osp.setPreferredSize(new Dimension(160, 1000));
        //add(osp, BorderLayout.SOUTH);
        
        JSplitPane splitNav = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, osp, splitContent);
        add(splitNav);
        
        // Menu
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("File");
        m.setMnemonic(KeyEvent.VK_F);
        JMenuItem mi = new JMenuItem("Open...", KeyEvent.VK_O);
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFiles();
            }
        });
        m.add(mi);
        mb.add(m);
        setJMenuBar(mb);
        
        // Finalize window
        setVisible(true);
        splitContent.setDividerLocation(0.50f);
    }
    
    private void chooseFiles() {
        try {
            FileNameExtensionFilter derricFilter = new FileNameExtensionFilter("Derric descriptions", "derric");
            _codeFile = getFile(derricFilter);
            if (_codeFile != null) {
                List<String> lines = java.nio.file.Files.readAllLines(Paths.get(_codeFile.getPath()), Charset.forName("UTF8"));
                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    sb.append(line);
                    sb.append("\n");
                }
                _codeView.setText(sb.toString());
                _dataFile = getFile(null);
                if (_dataFile != null) {
                    _current = _interpreter.run(_codeFile.getPath(), _dataFile.getPath());
                    setTitle(_codeFile.getName() + " on " + _dataFile.getName() + " results in: " + (_current.isValidated ? "Validated!" : "Not validated!"));
                    ((HexViewTableModel) _hexView.getModel()).setFile(_dataFile);
                    _hexView.revalidate();
                    ((SentenceViewTableModel)_sentenceView.getModel()).setSentence(_current);
                    _sentenceView.revalidate();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private File getFile(FileNameExtensionFilter filter) {
        JFileChooser fc = new JFileChooser();
        if (filter != null) {
            fc.setFileFilter(filter);
        }
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(MainFrame.this)) {
            File sf = fc.getSelectedFile();
            if (sf.exists() && !sf.isDirectory()) {
                return sf;
            }
        }
        return null;
    }
    
    private HashMap<String, Color> getDerricColors() {
        HashMap<String, Color> syntax = new HashMap<String, Color>();
        return syntax;
    }
    
    private void setHighlightsFromHexView(StructureMatch match) {
        if (match != null) {
            clearHighlights();
            setHighlight(match);
        }
    }
    
    private void setHighlightsFromCodeView(StructureMatch[] matches) {
        clearHighlights();
        for (StructureMatch m : matches) {
            setHighlight(m);
        }
    }
    
    private void setHighlight(StructureMatch match) {
        if (match != null) {
            try {
                _hexView.addHighlight(match.inputLocation.getOffset(), match.inputLocation.getLength());
                _codeView.getHighlighter().addHighlight(match.sequenceLocation.getOffset(), match.sequenceLocation.getOffset() + match.sequenceLocation.getLength(), _hl);
                _codeView.getHighlighter().addHighlight(match.structureLocation.getOffset(), match.structureLocation.getOffset() + match.structureLocation.getLength(), _hl);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private void clearHighlights() {
        _hexView.clearHighlights();
        _codeView.getHighlighter().removeAllHighlights();
    }
    
}
