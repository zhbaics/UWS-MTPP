package guisanboot.datadup.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import guisanboot.ui.SanBootView;

/**
 * Title:        cluster virsual manager
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      odyssey
 * @author zjj
 * @version 1.0
 */

public class GeneralEditableTable extends JTable {
    //** �表格的行数�����
    private int rowCount;

    //** �表格的列���
    private int colCount;

    //** �表格数据
    private Object[][] pureData;

    //**  表头数据��ͷ���
    private String[] header;

    //** �该表格的表格模型
    private EditableTableModel model;

    //** 一个临时数组
    private Object[] temp;

    /**
     * �构建方法. <p>
     */
    public GeneralEditableTable() {
        super(); setDefaultLook();
    }

    /**
     * 设置该表格的表格模型.<p>
     * @param Object[][] allData table的数据
     */
    public void setTableModel(Object[][] allData){
        int colWidth;
        rowCount=Integer.parseInt((String)allData[0][0]);
        colCount=Integer.parseInt((String)allData[0][1]);

        pureData=new Object[rowCount][colCount];

        for(int i=0;i<rowCount;i++)
            for(int j=0;j<colCount;j++)
                pureData[i][j]=allData[2+i][j];

        temp=new Object[colCount];
        header=new String[colCount];

        for(int i=0;i<colCount;i++)
            header[i]=(String)allData[1][i];

        model=new EditableTableModel(pureData,header);
        this.setModel(model);

        //设置各列的最佳宽度,注意这段代码依赖于栏目的排列顺序(从左到右)
    /*
        for(int i=0;i<colCount;i++){
          TableColumn column=this.getColumn(header[i]);
          column.setMinWidth(83);
          this.sizeColumnsToFit(0);   //必须加这句，这是由于java的一个bug
        }
    */
        initColumns(null);
    }

    public void setTableModel(Object[][] allData,Vector stomith){
        int colWidth;

        rowCount=Integer.parseInt((String)allData[0][0]);
        colCount=Integer.parseInt((String)allData[0][1]);

        pureData=new Object[rowCount][colCount];

        for(int i=0;i<rowCount;i++)
            for(int j=0;j<colCount;j++)
                pureData[i][j]=allData[2+i][j];

        temp=new Object[colCount];
        header=new String[colCount];

        for(int i=0;i<colCount;i++)
            header[i]=(String)allData[1][i];

        model=new EditableTableModel(pureData,header);
        this.setModel(model);
        initColumns(stomith);
    }

    /**
     * �定制缺省的外观效果<br>
     */
    public void setDefaultLook(){
        this.setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /**
     * 该类是table的模型 <br>
     */
    class EditableTableModel extends AbstractTableModel{

        //** 表头名字��ͷ����
        String[] columnNames;

        //** 表的pure data
        Object[][] data;

        public EditableTableModel(Object[][] data,String[] colnames){
            this.data=data;
            this.columnNames=colnames;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public int getRowCount() {
            return data.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        @Override public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override public Class getColumnClass(int col) {
            return data[0][col].getClass();
        }

        @Override public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        @Override public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    private void initColumns(Vector v){
        int i,j,num=0;

        if( v!=null )
            num=v.size();

        for( i=0; i<colCount; i++ ){
            TableColumn col = this.getColumn(header[i]);
            if( header[i].equals(" ") ){
                //do nothing
            }else{
                JTextField tf = new JTextField();
                DefaultCellEditor defaulteditor=new DefaultCellEditor(tf);
                col.setCellEditor(defaulteditor);
            }
        }
    } 

    class GeneralComboBoxEditor extends DefaultCellEditor {
        public GeneralComboBoxEditor(JComboBox combo) {
            super(combo);
        }
        @Override public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}
