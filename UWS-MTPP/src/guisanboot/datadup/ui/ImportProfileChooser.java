package guisanboot.datadup.ui;

/**
 * Title:        uws
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.regex.*;

import guisanboot.ui.SanBootView;
import guisanboot.datadup.data.*;
import guisanboot.res.*;

public class ImportProfileChooser extends JDialog {

    public final static int MODE_OPEN   = 0;
    public final static int MODE_SAVEAS = 1;
    public final static int MODE_SAVE   = 2;

    JPanel panel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JScrollPane jScrollPane1 = new JScrollPane();
    JPanel jPanel2 = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    JTextField nameTextField = new JTextField();
    JLabel jLabel2 = new JLabel();
    JButton openButton = new JButton();
    JButton cancelButton = new JButton();
    Border border1;
    JComboBox typeComboBox = new JComboBox(); // 操作方式

    DefaultListModel model = new DefaultListModel();
    JList profileList = new JList( model );

    int mode;
    UniProfile profile = null;
    UniProfile saveAsProfile = null;
    Backupable bakable; 
    SanBootView view; 
    int type; // 4: file 5: db 6: raw disk
    Object[] values = null;
    
    public ImportProfileChooser(
        SanBootView view,
        Backupable bakable,
        String title, 
        boolean modal,
        int mode,
        int type
    ) {
        super((Dialog)bakable, title, modal);
        try {
            jbInit();
            myInit( view,bakable,mode,type );
            pack();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public ImportProfileChooser(SanBootView view ,Backupable bakable,String title,int mode){
        this(view,bakable,title,true,mode,ResourceCenter.BAK_TYPE_FILE );
    }
    
    void jbInit() throws Exception {
        border1 = BorderFactory.createEmptyBorder(10,10,5,5);
        panel1.setLayout(borderLayout1);
        jPanel1.setLayout(borderLayout2);
        jScrollPane1.getViewport().setBackground(Color.white);
        jPanel2.setLayout(gridBagLayout1);
        jLabel1.setText("Profile Name : ");
        nameTextField.setMinimumSize(new Dimension(4, 18));
        nameTextField.setPreferredSize(new Dimension(63, 18));
        jLabel2.setText("Profile Type : ");
        openButton.setMaximumSize(new Dimension(73, 24));
        openButton.setMinimumSize(new Dimension(73, 24));
        openButton.setPreferredSize(new Dimension(73, 24));
        openButton.setText("Open");
        openButton.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent e) {
            openButton_actionPerformed(e);
          }
        });
        cancelButton.setMaximumSize(new Dimension(73, 24));
        cancelButton.setMinimumSize(new Dimension(73, 24));
        cancelButton.setPreferredSize(new Dimension(73, 24));
        cancelButton.setMargin(new Insets(0, 5, 0, 5));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent e) {
            cancelButton_actionPerformed(e);
          }
        });
        profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileList.addMouseListener(new java.awt.event.MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            profileList_mouseClicked(e);
          }
        });
        jPanel2.setBorder(border1);
        typeComboBox.setMaximumSize(new Dimension(32767, 18));
        typeComboBox.setMinimumSize(new Dimension(122, 18));
        typeComboBox.setPreferredSize(new Dimension(176, 18));
        getContentPane().add(panel1);
        panel1.add(jPanel1,  BorderLayout.CENTER);
        jPanel1.add(jScrollPane1,  BorderLayout.CENTER);
        panel1.add(jPanel2,  BorderLayout.SOUTH);
        jPanel2.add(jLabel1,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 7, 0), 0, 0));
        jPanel2.add(nameTextField,             new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 11, 7, 0), 0, 0));
        jPanel2.add(jLabel2,            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 17, 0), 0, 0));
        jScrollPane1.getViewport().add(profileList, null);
        jPanel2.add(openButton,         new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 11, 2, 10), 0, 0));
        jPanel2.add(cancelButton,           new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 11, 9, 10), 0, 0));
        jPanel2.add(typeComboBox,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 11, 17, 0), 0, 0));
    }

    private void myInit( SanBootView _view,Backupable _bakable,int _mode,int _type ){
        view = _view;
        bakable = _bakable;
        mode = _mode;
        type = _type;
        
        setupProfileType();
        setupList();
        setupLanguage();

        if( mode == ProfileChooser.MODE_SAVEAS ||
            mode == ProfileChooser.MODE_SAVE
        ){
            saveAsProfile = new UniProfile();
            nameTextField.requestFocus();
        }
    }

    private void setupProfileType( ){
        typeComboBox.addItem("Profile(*.prf)");
    }

    private void setupList(){
        ArrayList list = view.initor.mdb.getTypeProfile( type ); 
        int size = list.size();
        for( int i=0; i<size; i++ ){
            UniProfile profile = (UniProfile)list.get(i);
            model.addElement( profile );
        }
    }

    private void setupLanguage(){
        jLabel1.setText( SanBootView.res.getString("ProfileChooser.label.name"));
        jLabel2.setText( SanBootView.res.getString("ProfileChooser.label.type"));
        if( mode == MODE_OPEN )
          openButton.setText( SanBootView.res.getString("ProfileChooser.button.open"));
        else
          openButton.setText( SanBootView.res.getString("ProfileChooser.button.save"));
        cancelButton.setText(SanBootView.res.getString("common.button.cancel"));
    }

    public Object[] getValues(){
        return values;
    }

    void openButton_actionPerformed(ActionEvent e) {
        String name = nameTextField.getText().trim();
        if( mode == ProfileChooser.MODE_OPEN ){ // open
            if( name.equals("") ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("ProfileChooser.errMsg.notSelect")
                );
                return;
            }
            
            UniProfile prof = isSameProfile( name );
            if( prof == null ){
                JOptionPane.showMessageDialog(this,
                  SanBootView.res.getString("ProfileChooser.errMsg.notExist")+
                  ": "+name
                );
                return;
            }
            
            values = new Object[1];
            values[0] = prof;
        }else{ // save as 或 save
            if( name.equals("") ){
                JOptionPane.showMessageDialog(this,
                  SanBootView.res.getString("ProfileChooser.errMsg.notName")
                );
                return;
            }
            
            String tmpName = name;
            Pattern pattern = Pattern.compile(".+\\.prf");
            Matcher matcher = pattern.matcher( tmpName );
            if ( !matcher.find() ){
                tmpName +=".prf";
            }
            
            UniProfile pf = isSameProfile( tmpName );
            if( pf != null ){ // 存 在 相 同 的 名 字 的profile ( pf )
                if( view.initor.mdb.getSchNumOnProfName( pf.getProfileName() ) >0 ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("EditProfileDialog.error.hasSch")
                    );
                    return;
                }
                
                if( JOptionPane.showConfirmDialog(// 提示信息更换
                      (Dialog)bakable,
                      SanBootView.res.getString("ProfileChooser.confirmmsg1"),
                      SanBootView.res.getString("common.confirm"),
                      JOptionPane.OK_CANCEL_OPTION
                  ) == JOptionPane.CANCEL_OPTION ){
                    return ;
                }else{
                    values = new Object[2];
                    values[0] = pf;
                    values[1] = saveAsProfile;
                }
            }else{ // 不 存 在 相 同 的 名 字
                String pname = ResourceCenter.PROFILE_DIR + name + ".prf";
                
                if( pname.indexOf("\"")>=0 || pname.indexOf("'")>=0||
                    pname.indexOf(' ')>=0 || pname.indexOf('\t')>=0){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("ProfileChooser.errmsg.badname")
                    );
                    return;
                }
                
                if( pname.getBytes().length >= 1000 ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("ProfileChooser.errmsg.tooLargePname")
                    );
                    return;
                }
                
                values = new Object[1];
                saveAsProfile.setProfileName( pname );
                values[0] = saveAsProfile;
            }
        }
        
        dispose();
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        values = null;
        this.dispose();
    }

    void profileList_mouseClicked(MouseEvent e) {
        JList theList   = (JList)e.getSource();
        ListModel aModel = theList.getModel();
        int index = theList.locationToIndex( e.getPoint() );
        if( index <0 ) return;
        UniProfile aProfile = (UniProfile)aModel.getElementAt( index );
        nameTextField.setText( aProfile.toString() );
    }

    private UniProfile isSameProfile( String name ){
        int size = model.getSize();
        for( int i=0;i<size;i++ ){
            UniProfile aProfile = (UniProfile)model.getElementAt( i );
            if( aProfile.toString().equals( name ) )
                return aProfile;
        }
        
        return null;
    }
}
