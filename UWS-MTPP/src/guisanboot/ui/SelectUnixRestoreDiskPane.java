/*
 * SelectRestoreDiskPane.java
 *
 * Created on 2007/1/27, PM 15:00
 */

package guisanboot.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.regex.*;

import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.ui.multiRenderTable.*;

/**
 *
 * @author  Administrator
 */
public class SelectUnixRestoreDiskPane extends javax.swing.JPanel {
    
    /** Creates new form SelectBootHostPane */
    public SelectUnixRestoreDiskPane() {
        initComponents();
    }
    
    public SelectUnixRestoreDiskPane( SanBootView view,DestAgent host,RestoreOrigiDiskForUnixWizardDialog diag ) {
        this();
        myInit( view,host,diag );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 70));
        jScrollPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jTextArea1.setLineWrap(true);
        jTextArea1.setDisabledTextColor(java.awt.Color.black);
        jTextArea1.setEnabled(false);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel6.setPreferredSize(new java.awt.Dimension(10, 25));
        jCheckBox1.setText("input before and after command for snapshot");
        jCheckBox1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jPanel6.add(jCheckBox1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    ////GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    ////GEN-END:variables
    
    JTableV table;
    SanBootView view;
    DestAgent host;
    RestoreOrigiDiskForUnixWizardDialog diag;
    Object[] header;
    Object[] label;
    RowEditorModel rm2 = new RowEditorModel();
    RowEditorModel rm0 = new RowEditorModel();
    RowEditorModel rm5 = new RowEditorModel();
    Hashtable seqArray = new Hashtable();
     
    private void myInit( SanBootView _view,DestAgent _host,RestoreOrigiDiskForUnixWizardDialog _diag ){
        view = _view;
        host = _host;
        diag = _diag;
        
        jTextArea1.setText(
            SanBootView.res.getString("RestoreOriginalDiskWizardDialog.tip1")
        );
        jCheckBox1.setText( SanBootView.res.getString("SelectRestoreDiskPane.checkBox.inputcmd"));
    }
     
    public void setupTable( Vector list  ){
        LVWrapper1 lv;
        JComboBox cb; 
        DefaultCellEditor ed;
        CheckBoxEditor ced;
        int i;
        Object[][] data;
        
        int num = list.size();
        
        // 生成恢复顺序列表
        Vector seqList = new Vector();
        for( i=0; i<num; i++ ){
            seqList.addElement( new Integer( i+1 ) );
        }
        
        data = new Object[num][6]; 
        header = new Object[6]; // select?, volMap, seq#, dest disk,label,restore?
        label = new Object[num];
        header[0] = SanBootView.res.getString("SelectRestoreDiskPane.table.select");
        header[1] = SanBootView.res.getString("SelectRestoreDiskPane.table.volMap");
        header[2] = SanBootView.res.getString("SelectRestoreDiskPane.table.seq");
        header[3] = SanBootView.res.getString("SelectRestoreDiskPane.table.dest");
        header[4] = SanBootView.res.getString("SelectRestoreDiskPane.table.label");
        header[5] = SanBootView.res.getString("SelectRestoreDiskPane.table.restore");
        
        for( i=0; i<num; i++ ){
            lv = (LVWrapper1)list.elementAt(i);
            
            data[i][0] = Boolean.TRUE;
            data[i][1] = lv;
            data[i][2] = new Integer( i+1 );
            data[i][3] = "";
            if( lv.toString().equals( ResourceCenter.SWAP_MP) ){
                data[i][4] = "";
            }else{
                data[i][4] ="ody_"+lv.toString();
                //data[i][4] = "";
            }
            data[i][5] = Boolean.FALSE;
            
            cb = new JComboBox( seqList );
            ed = new DefaultCellEditor( cb );
            rm2.addEditorForRow( i, ed );
            
            label[i]= lv.toString();
        }
        
        MyDefaultTableModelForTabV model = new MyDefaultTableModelForTabV( data,header,label );
        
        table = new JTableV( model );
        table.setRowHeight( 20 );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        
        ced = new CheckBoxEditor();
        rm0.addEditorForRow( 0, ced );
            
        ced = new CheckBoxEditor();
        rm5.addEditorForRow( 5,ced );
        
        table.setCol2EditorModel( rm2 );
        table.setCol0EditorModel( rm0 );
        table.setCol5EditorModel( rm5 );
        
        TableColumnModel tableColumnModel = table.getColumnModel();
        int colNum = tableColumnModel.getColumnCount();
        tableColumnModel.getColumn(0).setWidth( 50 );
        tableColumnModel.getColumn(1).setWidth( 92 );
        tableColumnModel.getColumn(2).setWidth( 80 );
        tableColumnModel.getColumn(3).setWidth( 92 );
        tableColumnModel.getColumn(4).setWidth( 92 );
        tableColumnModel.getColumn(5).setWidth( 60 );
        for( i=0;i<colNum;i++ )
            table.sizeColumnsToFit(i);

        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed(false);
        
        jScrollPane2.getViewport().add( table,null );
        jScrollPane2.getViewport().setBackground( Color.white );
    }
    
    public boolean isInputSnapCmd(){        
        if( getRestoreNum() >0 ){
            return jCheckBox1.isSelected();
        }else{
            return false;
        }
    }
    
    public void setInputSnapCmdFlag( boolean val ){
        jCheckBox1.setSelected( val );
    }
    
    public void updateDest( HashMap list ){
        Object val;
        RestoreMapper mapper;
        LVWrapper1 lv;
                
        if( list.size()<=0 ) return;
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            Boolean isSel = (Boolean)model.getValueAt(row, 0 );
            if( isSel.booleanValue() ){
                lv =(LVWrapper1)model.getValueAt( row,1 );
                val = list.get( lv.toString() );
                if( val != null ){
                    mapper = (RestoreMapper)val;
                    model.setValueAt( mapper.getDest(),row, 3 );
                    model.setValueAt( mapper.getLabel(),row,4 );
                }
            }
        }
    }
    
    public boolean hasRestoredDisk(){
        int cnt = 0;
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            Boolean isSel = (Boolean)model.getValueAt(row, 0 );
            if( isSel.booleanValue() ){
                cnt++;
            }
        }
        
        return ( cnt > 0 );
    }
    
    public boolean isOSDiskSelected(){
        LVWrapper1 lv;
        boolean isSel;
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            lv = (LVWrapper1)model.getValueAt( row,1 );
            if( lv.toString().equals("/") ){
                isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
                return isSel;
            }
        }
        
        return false;
    }
    
    public int getSelectedRowOnMp( String mp){
        LVWrapper1 lv;
        boolean isSel;
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            lv = (LVWrapper1)model.getValueAt( row,1 );
            if( lv.toString().equals( mp ) ){
                return row;
            }
        }
        
        return -1;
    }
    
    public boolean isMpRight( String mp,int row ){
        String _mp;
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        Object val = model.getValueAt( row, 3 );
        if( val instanceof DestDevice ){
            _mp = ((DestDevice)val).getMp();
        }else{
            _mp = (String)val;
        }
      
        return (_mp.equals(mp) );
    }
    
    public String getLocalDiskMp( int row ){
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        Object val =  model.getValueAt(row, 3);
        if( val instanceof DestDevice ){
            return ((DestDevice)val).getMp();
        }else{
            return (String)val;
        }
    }
    
    public boolean isThisRowProtected( int row ){
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        boolean isSel = ((Boolean)model.getValueAt(row, 0)).booleanValue();
        return isSel;
    }
    
    public ArrayList getDevAndLabelList(){
        boolean isSel;
        LVWrapper1 lv;
        Object val;
        String label,destStr,devname;
        DestDevice dest;
        Object[] dev_label;
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();
        ArrayList ret = new ArrayList( lineNum );
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue(); 
            if( isSel ){
                lv = (LVWrapper1)model.getValueAt( row, 1 );
                if( lv.toString().equals( ResourceCenter.SWAP_MP ) ) continue;
                if( lv.fsType.toUpperCase().startsWith( ResourceCenter.FAT_FS_PREFIX ) ) continue;

                val = model.getValueAt( row,3 );
                if( val instanceof DestDevice ){
                    dest = (DestDevice)val;
                    destStr = dest.getMp();
                }else{
                    destStr = (String)val;
                }

                label = (String)model.getValueAt( row, 4 );
                devname = diag.getDevName( destStr );
                dev_label = new String[]{ devname,label };

                ret.add( dev_label );
            }
        }
        
        return ret;
    }
    
    // 下面的检查确保恢复目的都是本地盘
    public boolean checkInfoValidity(){
        boolean isSel,isOk,toRest;
        String destStr,ip,label,devname;
        DestDevice dest;
        int port;
        Matcher matcher;
        RetValObj retVal;
        LVWrapper1 lv;
        Object val;
        
        ip = host.getIP();
        port = host.getPort();
        
        // 重新获取系统分区信息(因为用户可能又改变了恢复目的)
        if( !diag.reGetUnixPart() ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("SelectRestoreDiskPane.error.getLatestFsList")
            );
            return false;
        } 
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
//System.out.println(" ==============> : "+isSel ); 
            
            if( isSel ){
                lv = (LVWrapper1)model.getValueAt( row, 1 );
                if( !lv.toString().equals( ResourceCenter.SWAP_MP ) ){
                    val = model.getValueAt( row,3 );
                    if( val instanceof DestDevice ){
                        dest = (DestDevice)val;
                        destStr = dest.getMp();
                    }else{
                        destStr = (String)val;
                    }
                    
                    if( !destStr.startsWith("/")){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.invalidVal1") + " ["+row+",3]"
                        );
                        return false;
                    }
                    
                    if( !badDestDriver( destStr ) ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.rstOntoSelf") + " ["+row+",3]"
                        );
                        return false;
                    }
                    
                    isOk = diag.isExist( destStr );
                    if( !isOk ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.noneLocalDisk") + " ["+row+",3]"
                        );
                        return false;
                    }
                    
                    label = (String)model.getValueAt( row, 4 );
                    if( label.equals("") ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.invalidVal2") + " ["+row+",4]"
                        );
                        return false;
                    }
                    
                    if( label.getBytes().length >16 ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.badLenOfLabel") + " ["+row+",4]"
                        );
                        return false;
                    }
                    
                    if( !badLabel( label,row ) ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.sameLabel") + " ["+row+",4]"
                        );
                        return false;
                    }
                    
                    devname = diag.getDevName( destStr );
                    if( devname.equals("") ){
SanBootView.log.debug(getClass().getName(),"Can't find dev name: "+ destStr );                        
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.missingDev") + " ["+row+",3]"
                        );
                        return false;
                    }
                    
                    toRest = ((Boolean)model.getValueAt( row, 5 )).booleanValue();
                    if( toRest ){
                        
                        // 下面的比较不很科学。遇到磁盘满时，复制程序会提示失败，所以不比较了。2008-11-3

                        // 比较"源的实际数据量"和"目的空闲空间"是否匹配
                        /*
                        retVal = diag.sizeIsMatched( lv.toString(), destStr );
                        if( !retVal.isOk ){
                            JOptionPane.showMessageDialog( this,retVal.errMsg +" [" + row + ",3]" );
                            return false;   
                        }
                         */
                    }
                }else{
                    val = model.getValueAt( row,3 );
                    if( val instanceof DestDevice ){
                        dest = (DestDevice)val;
                        destStr = dest.getMp();
                    }else{
                        destStr = (String)val;
                    }
                    if( !destStr.startsWith("/")){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("SelectRestoreDiskPane.error.invalidVal1") + " ["+row+",3]"
                        );
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    private boolean badLabel( String _label,int _row ){
        String label;
    
        // check whether same label exist.
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            label =(String) model.getValueAt( row, 4 );
            if( row == _row )continue;
            
            if( label.equals( _label ) ){
                return false;
            }
        }
        return true;
    }
    
    private boolean badDestDriver( String dest ){
        LVWrapper1 lv;
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            lv =(LVWrapper1) model.getValueAt( row, 1 );
            if( lv.toString().equals( dest ) ){
                return false;
            }
        }
        return true;
    }
    
    public Vector getRstDestList(){
        boolean isSel;
        String dest;
        Object val;
        
        Vector ret = new Vector();
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            if( isSel ){
                val = model.getValueAt( row, 3 );
                if( val instanceof String ){
                    dest = (String)val;
                }else{
                    dest = ((DestDevice)val).getMp();
                }
                
                ret.addElement( dest );
            }
        }
        
        return ret;
    }
    
    public String getRstMappingTable(){
        LVWrapper1 lv;
        String destStr,volInfo,label;
        DestDevice dest;
        Object val;
        boolean isSel,isFirst=true;
        
        StringBuffer buf = new StringBuffer();
         
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            if( isSel ){
                lv = (LVWrapper1)model.getValueAt( row,1 );
                val = model.getValueAt( row, 3 );
                // volInfo全部从最新获取的partition list( list_mp.sh )中获取
                if( val instanceof String ){
                    destStr = (String)val;
                    volInfo = getDevPath( destStr );
                }else{
                    dest = (DestDevice)val;
                    destStr = dest.getMp();
                    volInfo = getDevPath( destStr );
                }
                
                label = (String)model.getValueAt( row, 4);
                
                if( isFirst ){
                    buf.append( lv.toString() + ";" + destStr + ";" + volInfo + ";" + label );
                    isFirst = false;
                }else{
                    buf.append("\n" + lv.toString() + ";" + destStr + ";" + volInfo + ";" + label );
                }
            }
        }
        
        return buf.toString();
    }
    
    private String getDevPath( String mp ){
        SystemPartitionForUnix part = view.initor.mdb.getSysPartStatistic1( mp );
        if( part != null ){
            return part.dev_path;
        }else{
            return "";
        }
    }
    
    public String getRstMpList(){
        LVWrapper1 lv;
        String dest,label;
        Object val;
        boolean isSel;
        
        StringBuffer buf = new StringBuffer();
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            if( isSel ){
                lv = (LVWrapper1)model.getValueAt( row,1 );
                val = model.getValueAt( row, 3 );
                if( val instanceof String ){
                    dest = (String)val;
                }else{
                    dest = ((DestDevice)val).getMp();
                }
                label =(String)model.getValueAt( row,4 );
                
                // swap device的label为空
                if( lv.toString().equals(ResourceCenter.SWAP_MP) ){
                    buf.append( " -info -m " + lv.toString()+" -l swap "+" -n " + dest );
                }else{
                    buf.append( " -info -m " + lv.toString()+" -l "+label +" -n " + dest );  
                }
            }
        }
        
        return buf.toString();
    }
    
    private int getRestoreNum(){
        boolean isSel,isRst;
        
        int cnt=0;
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            if( isSel ){
                 isRst = ((Boolean)model.getValueAt( row, 5 )).booleanValue();
                 if( isRst ){
                     cnt++;
                 }
            }
        }
        
        return cnt;
    }
    
    public Hashtable getSelectedVolume(){
        LVWrapper1 lv;
        BindOfVolMapandDestForUnix binder;
        boolean isSel,contained,isRst;
        int seq;
        Object val;
        String dest;
        Vector list;
        
        seqArray.clear();
        
        MyDefaultTableModelForTabV model = (MyDefaultTableModelForTabV)table.getModel();
        int lineNum = model.getRowCount();        
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            if( isSel ){
                isRst = ((Boolean)model.getValueAt( row, 5 )).booleanValue();
                if( isRst ){
                    lv = (LVWrapper1)model.getValueAt( row,1 );
                    // swap 设备不需要反向恢复
                    if( lv.toString().equals( ResourceCenter.SWAP_MP ) ) continue;
                    
                    seq = ((Integer)model.getValueAt( row, 2 )).intValue(); 
                    val = seqArray.get( new Integer( seq ) );
                    if( val != null ){
                        list = (Vector)val;
                        contained = true;
                    }else{
                        list = new Vector();
                        contained = false;
                    }
                    
                    val = model.getValueAt( row, 3 );
                    if( val instanceof String ){
                        dest = (String)val;
                    }else{
                        dest = ((DestDevice)val).getMp();
                    }
//System.out.println("dest: "+dest);                    
                    binder = new BindOfVolMapandDestForUnix();
                    binder.lv = lv;
                    binder.destDrv = dest;
                    
                    list.addElement( binder );

                    if( !contained ){
                        seqArray.put( new Integer( seq ), list );
                    }
                }
            }
        }
        
        return seqArray;
    }
    
    public void fireEditingStopMsg(){
        TableCellEditor dce;
        
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int i=0; i<lineNum; i++  ){
            for( int j=0;j<6;j++ ){ // 专门停止0,2,3,4,5列
                if( j == 1 ) continue;
                
                dce = table.getCellEditor( i,j );
                if( dce!=null ){
                    try{
                        while(!dce.stopCellEditing()){};
                    }catch(Exception ex){}
                }
            }
        }
    }
}