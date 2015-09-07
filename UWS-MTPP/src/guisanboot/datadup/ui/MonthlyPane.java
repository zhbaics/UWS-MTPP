package guisanboot.datadup.ui;

import guisanboot.ui.SanBootView;
import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * Title:        Odysys Backup
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author
 * @version 1.0
 */

public class MonthlyPane extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JLabel jLabel1 = new JLabel();
    JComboBox monthFreqComboBox = new JComboBox();
    JLabel jLabel2 = new JLabel();
    JPanel jPanel2 = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    MonthlyDayBakPane monPane; 

    public MonthlyPane() {
        try {
            uiInit();
      
            // 应周必灯要求，去掉monthFreqComboBox ( 2005/1/13 )
            // myInit();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
  
    void uiInit() throws Exception{
        this.setLayout(borderLayout1);
        jPanel1.setLayout( null );
        jLabel1.setText("Each ");
        jLabel2.setText("Month");
        jPanel2.setLayout( borderLayout2 );
        monthFreqComboBox.setMaximumSize(new Dimension(32767, 18));
        monthFreqComboBox.setMinimumSize(new Dimension(122, 18));
        monthFreqComboBox.setPreferredSize(new Dimension(126, 18));

        // 应周必灯要求，去掉monthFreqComboBox ( 2005/1/13 )
    //    this.add(jPanel1, BorderLayout.NORTH);
        jPanel1.add( jLabel1 );
        jLabel1.setBounds( 71,9,-1,-1 );
        jPanel1.add( monthFreqComboBox );
        monthFreqComboBox.setBounds( 120, 7,-1, -1 );
        jPanel1.add( jLabel2 );
        jLabel2.setBounds( 260,9,-1,-1 );
        add(jPanel2,  BorderLayout.CENTER);
        monPane = new MonthlyDayBakPane();
        jPanel2.add( monPane,BorderLayout.CENTER );
    }
  
    private void myInit(){
        jLabel1.setText( SanBootView.res.getString("MonthlyPane.label.each"));
        jLabel2.setText( SanBootView.res.getString("MonthlyPane.label.month"));
        monthFreqComboBox.addItem("1");
    }

    public void enableMonthlyPane( boolean val ){
        this.jLabel1.setEnabled( val );
        this.jLabel2.setEnabled( val );
        this.monthFreqComboBox.setEnabled( val );
        this.monthFreqComboBox.setOpaque( val );
        this.monPane.enableOwn( val );
    }

    public String getInfo(){
        return this.monPane.getInfo();
    }

    public void initMonthPane(Vector mlist){
        monPane.initMonthPane(mlist);
    }
}
