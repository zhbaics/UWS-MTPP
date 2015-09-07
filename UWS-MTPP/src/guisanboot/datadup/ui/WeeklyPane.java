package guisanboot.datadup.ui;

import guisanboot.ui.SanBootView;
import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author
 * @version 1.0
 */

public class WeeklyPane extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    JCheckBox[] jCheckBoxList = new JCheckBox[7];
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public WeeklyPane() {
        try {
            jbInit();
            myInit();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        jPanel2.setLayout(gridBagLayout1);
        add(jPanel2, BorderLayout.CENTER);

        for( int i=0;i<7;i++){
            jCheckBoxList[i] = new JCheckBox();
        }
        jCheckBoxList[0].setText("Mon.");
        jCheckBoxList[1].setText("Tue.");
        jCheckBoxList[2].setText("Wed.");
        jCheckBoxList[3].setText("Thur.");
        jCheckBoxList[4].setText("Fri.");
        jCheckBoxList[5].setText("Sat.");
        jCheckBoxList[6].setText("Sun.");

        for( int i=0;i<7;i++){
          if( i == 0){
            jPanel2.add(jCheckBoxList[i],   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
          }else if( i == 1 ){
            jPanel2.add(jCheckBoxList[i],    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
          }else if( i == 2 ){
            jPanel2.add(jCheckBoxList[i],    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
          }else if( i == 3){
            jPanel2.add(jCheckBoxList[i],   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
          }else if( i == 4){
            jPanel2.add(jCheckBoxList[i],  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
          }else if( i == 5){
            jPanel2.add(jCheckBoxList[i],   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
          }else if( i == 6 ){
            jPanel2.add(jCheckBoxList[i],   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
          }
        }
    }

    private void myInit(){
        jCheckBoxList[0].setText( SanBootView.res.getString("WeeklyPane.checkBox.monday"));
        jCheckBoxList[1].setText( SanBootView.res.getString("WeeklyPane.checkBox.tuesday"));
        jCheckBoxList[2].setText( SanBootView.res.getString("WeeklyPane.checkBox.wednesday"));
        jCheckBoxList[3].setText( SanBootView.res.getString("WeeklyPane.checkBox.thursday"));
        jCheckBoxList[4].setText( SanBootView.res.getString("WeeklyPane.checkBox.friday"));
        jCheckBoxList[5].setText( SanBootView.res.getString("WeeklyPane.checkBox.saturday"));
        jCheckBoxList[6].setText( SanBootView.res.getString("WeeklyPane.checkBox.sunday"));
    }

    public void enableOwn(boolean val){
        for( int i=0;i<7;i++ ){
            jCheckBoxList[i].setEnabled( val );
            jCheckBoxList[i].setOpaque( val );
        }
    }

    public String getInfo(){
        String info = "";
        boolean flag = true;

        for( int i=0;i<6;i++ ){
            if( jCheckBoxList[i].isSelected() ){
                if( !flag )
                    info+=","+(i+1);
                else{
                    info+=(i+1)+"";
                    flag = false;
                }
            }
        }

        if( jCheckBoxList[6].isSelected() ){
            if( info.equals("") ){
                info ="0";
            }else{
                info = "0,"+info;
            }
        }

        return info;
    }

    public void initWeekPane( Vector dlist ){
        for( int i=0;i<6;i++){
            if( dlist.contains( new Integer( i+1 ) ) ){
                jCheckBoxList[i].setSelected( true );
            }
        }

        if( dlist.contains( new Integer(0 ) )){
            jCheckBoxList[6].setSelected( true );
        }
    }
}
