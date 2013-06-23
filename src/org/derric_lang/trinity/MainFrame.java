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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;

import org.derric_lang.validator.interpreter.FieldMatch;
import org.derric_lang.validator.interpreter.StructureMatch;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    
    public static final Color BG = Color.WHITE;
    public static final Color FG = Color.DARK_GRAY;
    public static final Color BG_SEL1 = new Color(0xafafdf);
    public static final Color BG_SEL2 = new Color(0xdfafaf);
    public static final Color COMMENT = Color.LIGHT_GRAY;

    public static final Color YELLOW = new Color(0xb58900);
    public static final Color ORANGE = new Color(0xcb4b16);
    public static final Color RED = new Color(0xdc322f);
    public static final Color MAGENTA = new Color(0xd33682);
    public static final Color VIOLET = new Color(0x6c71c4);
    public static final Color BLUE = new Color(0x268bd2);
    public static final Color CYAN = new Color(0x2aa198);
    public static final Color GREEN = new Color(0x859900);
    
    private final Interpreter _interpreter;

    private HexViewTable _hexView;
    private CodeView _codeView;
    private SentenceViewTree _sentenceView;

    private File _codeFile;
    private File _dataFile;
    private Sentence _current;
    
    public MainFrame() throws IOException {
        _interpreter = new Interpreter();
        initGUI();
    }
    
    private void initGUI() throws IOException {
        // Main window
        setTitle("Trinity");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Hex view
        _hexView = new HexViewTable();
        JScrollPane tsp = new JScrollPane(_hexView);
        tsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tsp.setPreferredSize(new Dimension(_hexView.getPreferredSize().width + 24, _hexView.getPreferredSize().height));
        ListSelectionListener sl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    handleHexSelection();
                }
            }
        };
        _hexView.getSelectionModel().addListSelectionListener(sl);
        _hexView.getColumnModel().getSelectionModel().addListSelectionListener(sl);
        _hexView.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                handleHexSelection();
            }
            @Override
            public void focusLost(FocusEvent e) { }
        });
        
        // Code view
        _codeView = new CodeView();
        JPanel noWrapPanel = new JPanel(new BorderLayout());
        noWrapPanel.add(_codeView);
        JScrollPane esp = new JScrollPane(noWrapPanel);
        esp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        _codeView.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                handleCodeSelection();
            }
        });
        
        JSplitPane splitContent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tsp, esp);
        
        // Sentence view
        _sentenceView = new SentenceViewTree();
        _sentenceView.setModel(new SentenceViewTreeModel("None", new ArrayList<StructureMatch>()));
        JScrollPane osp = new JScrollPane(_sentenceView);
        osp.setPreferredSize(new Dimension(160, 700));
        _sentenceView.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                handleSentenceSelection();
            }
        });
        
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
        m.addSeparator();
        JMenuItem mx = new JMenuItem("Exit", KeyEvent.VK_X);
        mx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        m.add(mx);
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
                _codeView.setCaretPosition(0);
                _codeView.colorize(_interpreter.getColorMap(sb.toString()));
                _dataFile = getFile(null);
                if (_dataFile != null) {
                    _current = _interpreter.run(_codeFile.getPath(), _dataFile.getPath());
                    setTitle(_codeFile.getName() + " on " + _dataFile.getName() + " results in: " + (_current.isValidated ? "Validated!" : "Not validated!"));
                    ((HexViewTableModel)_hexView.getModel()).setFile(_dataFile);
                    _hexView.addCoverage(_current);
                    _hexView.revalidate();
                    _sentenceView.setModel(new SentenceViewTreeModel(_dataFile.getName(), _current.matches));
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
    
    private void handleHexSelection() {
        if (_current != null) {
            if (_hexView.getSelectedColumn() > 0 && _hexView.getSelectedColumn() <= HexViewTableModel.WIDTH) {
                int offset = (_hexView.getSelectedRow() * HexViewTableModel.WIDTH) + (_hexView.getSelectedColumn() - 1);
                Selection s = _current.getDataMatch(offset);
                _codeView.setViewable(s);
                _sentenceView.setViewable(s);
                setHighlights(s);
            } else {
                setHighlights((Selection)null);
            }
        }
    }
    
    private void handleCodeSelection() {
        if (_current != null) {
            Selection[] selections = _current.getCodeMatches(_codeView.getCaretPosition());
            if (selections.length > 0) {
                _hexView.setViewable(selections[0]);
                _sentenceView.setViewable(selections[0]);
            }
            setHighlights(selections);
        }
    }
    
    private void handleSentenceSelection() {
        if (_current != null) {
            TreePath selection = _sentenceView.getSelectionPath();
            Selection s = null;
            if (selection.getPathCount() == 2) {
                StructureMatch structure = ((StructureMatch)selection.getLastPathComponent());
                s = new Selection(structure);
            } else if (selection.getPathCount() == 3) {
                StructureMatch structure = (StructureMatch)selection.getPathComponent(1);
                FieldMatch field = (FieldMatch)selection.getPathComponent(2);
                s = new Selection(structure, field);
            }
            _hexView.setViewable(s);
            _codeView.setViewable(s);
            setHighlights(s);
        }
    }
    
    private void setHighlights(Selection... selections) {
        clearHighlights();
        for (Selection selection : selections) {
            if (selection != null && selection.structure != null) {
                try {
                    _hexView.addHighlight(selection.structure);
                    _codeView.addHighlight(selection.structure);
                    _sentenceView.addHighlight(selection.structure);
                    if (selection.field != null) {
                        _hexView.addHighlight(selection.field);
                        _codeView.addHighlight(selection.field);
                        _sentenceView.addHighlight(selection.field);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        _hexView.repaint();
        _sentenceView.repaint();
    }
    
    private void clearHighlights() {
        _hexView.clearHighlights();
        _codeView.clearHighlights();
        _sentenceView.clearHighlights();
    }
    
}
