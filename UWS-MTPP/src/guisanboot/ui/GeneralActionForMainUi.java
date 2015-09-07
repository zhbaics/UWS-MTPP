package guisanboot.ui;

/**
 * Title:        Odysys UWS
 * Description:
 * Copyright:    Copyright (c) 2005
 * Company:      odysys
 * @author zjj
 * @version 1.0
 */
import javax.swing.*;
import mylib.UI.BasicAction;

public class GeneralActionForMainUi extends BasicAction{
    protected SanBootView view = null;
  
    public GeneralActionForMainUi(Icon menuIcon,Icon btnIcon,String text,int funcID) {
        super(menuIcon,btnIcon,text,funcID);
    }
  
    public void setView(SanBootView _view){
        view = _view; 
    }
}
