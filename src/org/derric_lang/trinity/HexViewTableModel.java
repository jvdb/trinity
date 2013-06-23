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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class HexViewTableModel extends AbstractTableModel {
    
    public static final int WIDTH = 16;
    
    private byte[] _data;
    private final String[] _names = new String[] {
            "Offset", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "Ascii" };
    
    public HexViewTableModel(File f) throws IOException {
        setFile(f);
    }
    
    public void setFile(File f) throws IOException {
        if (f == null) {
            _data = new byte[0];
        } else {
            _data = new byte[(int) f.length()];
            FileInputStream input = new FileInputStream(f);
            input.read(_data);
            input.close();
        }
    }
    
    @Override
    public int getRowCount() {
        return (_data.length / WIDTH) + ((_data.length % WIDTH) > 1 ? 1 : 0);
    }
    
    @Override
    public int getColumnCount() {
        return _names.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex >= _names.length) {
            return null;
        } else {
            return _names[columnIndex];
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return String.format("%08X", rowIndex * WIDTH);
        } else if (columnIndex == 17) {
            int offset = rowIndex * WIDTH;
            int count = _data.length - offset >= WIDTH ? WIDTH : _data.length - offset;
            String ret = "";
            for (int i = 0; i < count; i++) {
                int value = _data[offset + i] & 0xFF;
                if (value < 32 || value > 126) {
                    ret += ".";
                } else {
                    ret += String.format("%c", value);
                }
            }
            return ret;
        } else {
            int offset = (rowIndex * WIDTH) + (columnIndex - 1);
            if (offset >= _data.length) {
                return "";
            } else {
                return String.format("%02X", _data[(rowIndex * WIDTH) + (columnIndex - 1)] & 0xFF);
            }
        }
    }
    
}
