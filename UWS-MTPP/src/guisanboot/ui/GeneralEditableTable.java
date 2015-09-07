package guisanboot.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 * @author jjzhang
 * @version 1.0
 * (C)Copyright 2004, Odysys inc.
 */

/**
 *  一个可用来编辑的table.<br>
 */
public class GeneralEditableTable extends JTable {

    //
    //�成员变量�Ա����
    //

    //** 表格的行数����
    private int rowCount;

    //** 表格的列���
    private int colCount;

    //** 表格数据�����
    private Object[][] pureData;

    //** 表头数据ͷ���
    private String[] header;

    //** 该表格的表格模型
    private EditableTableModel model;

    //** 一个临时数组�
    private Object[] temp;

    //
    //�成员函数���
    //

    /**
      * 构建方法 <p>
      */
    public GeneralEditableTable() {
        super(); setDefaultLook();
    }

    /**
      *  设置该表格的表格模型 <p>
      * @param Object[][] allData table�����
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

        initColumns();
    }

  /**
    * 定制缺省的外观效果 <br>
    */
    public void setDefaultLook(){
        this.setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

  /**
   * 该类是table的模型 <br>
   */
    class EditableTableModel extends AbstractTableModel{
        //**  表头名字��ͷ����
        String[] columnNames;

        //** �表的pure data
        Object[][] data;

        /**
         * 构造函数 <br>
         * @param Object[][] data  table��pure data
         * @param Stirng[] colname table的表头
         */
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

        public boolean isCellEditable(int row, int col) {
            Object value;
            String name = getColumnName(col);

            if( name.equals(
                SanBootView.res.getString("SelectServicePane.table.service.isSel")
            )){
                return true;
            }else if(name.equals( SanBootView.res.getString("SelectNetCardPane.table.netcard.isSel"))){
                return false;
            }else if(name.equals( SanBootView.res.getString("TaskConfigPane.table.task.isFinish") )){
                return true;
            }else if( name.equals( SanBootView.res.getString("InputSnapshotCmdPane.table.precmd")) ){
                return true;
            }else if( name.equals( SanBootView.res.getString("InputSnapshotCmdPane.table.postcmd")) ){
                return true;
            }else{
                return false;
            }
        }

        public void updateNetCard( int selectedRow ){
            int r,rowCnt;
            rowCnt = getRowCount();
            
            for( r=0; r<rowCnt; ++r ){
                data[r][0] = new Boolean( r == selectedRow );
            }
            
            for( r=0; r<rowCnt; ++r ){
                fireTableCellUpdated( r, 0 );
            }
        }
    }
  
    public void updateNetCardColumn( int selectedRow ){
        model.updateNetCard( selectedRow );
    }
    
    public AbstractTableModel getAbsTableModel(){
        return model;
    }

    private void initColumns(){
        for( int i=0;i<colCount;i++ ){
            // 这些列使用系统缺省的编辑渲染器
            // 这些列的渲染类型为: combox, checkbox

            if( header[i].equals(
                SanBootView.res.getString("SelectNetCardPane.table.netcard.isSel")
            )||
                header[i].equals(
                    SanBootView.res.getString("SelectServicePane.table.service.isSel")
            )||
                header[i].equals(
                    SanBootView.res.getString("TaskConfigPane.table.task.isFinish")
            )){
                //do nothing
            }else{
                // 剩余的列使用TextField渲染类型
                TableColumn col = this.getColumn(header[i]);
                JTextField tf = new JTextField();
                DefaultCellEditor defaulteditor=new DefaultCellEditor(tf);
                col.setCellEditor(defaulteditor);
            }
        }
    }
    
  /**
   * 一般的ComboBox编辑器<br>
   */
    class GeneralComboBoxEditor extends DefaultCellEditor {
        public GeneralComboBoxEditor(JComboBox combo) {
            super(combo);
        }
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}
