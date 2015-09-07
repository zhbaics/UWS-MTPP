package guisanboot.cmdp.ui;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

public class ClockPane extends JPanel {
    GridBagLayout gridBagLayout = new GridBagLayout();
    JCheckBox jCheckBoxList[] = new JCheckBox[24];

    public ClockPane() { 
        try {
            jbInit();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setLayout( gridBagLayout );
        for( int i=0;i<24;i++ ){
            jCheckBoxList[i] = new JCheckBox();
            jCheckBoxList[i].setText(""+i);
            int comboxY = (i/8)*2+1;
            this.add(jCheckBoxList[i],new GridBagConstraints((i%8),comboxY,1,1,0.0,0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -66, 0));
            jCheckBoxList[i].setMinimumSize(new Dimension(110,18));
            jCheckBoxList[i].setPreferredSize(new Dimension(110,18));
        }
    }

    public void initClockPane(Vector list){
        for( int i=0;i<24;i++){
            if( list.contains( new Integer( i ) ) ){
                jCheckBoxList[i].setSelected( true );
            }
        }
    }
    
    public void enableOwn(boolean val){
        for( int i=0; i<24; i++ ){
            jCheckBoxList[i].setEnabled( val );
        }
    }

    public String getInfo(){
        String info = "";
        boolean flag = true;

        for( int i=0; i<24; i++ ){
            if( jCheckBoxList[i].isSelected() ){
                if( !flag )
                    info+=","+i;
                else{
                    info+=i+"";
                    flag = false;
                }
            }
        }
        
        return info;
    }
}
