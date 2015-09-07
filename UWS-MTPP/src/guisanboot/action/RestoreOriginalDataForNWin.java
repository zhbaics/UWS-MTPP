/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.DestAgent;
import guisanboot.data.DhcpClientInfo;
import guisanboot.data.SourceAgent;
import guisanboot.data.UnixFileObj;
import guisanboot.data.UnixNetCard;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefNetBootHost;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.RestoreOrigiDiskForUnixWizardDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class RestoreOriginalDataForNWin extends GeneralActionForMainUi {
    public RestoreOriginalDataForNWin(){
        super(
            ResourceCenter.BTN_ICON_RD_16,
            ResourceCenter.BTN_ICON_RD_50,
            "View.MenuItem.rstOriData",
            MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOk,needModUUID = false;
        ArrayList lvmInfo;
SanBootView.log.info(getClass().getName(),"########### Entering linux host's restore original disk action. " );

        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof BootHost ){
            BootHost host = (BootHost)selObj;

            if( host.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.isWinHost")
                );
                return;
            }

            // 判断是否成功初始化了
            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
            }

            // 判断是否从 network 启动
            isOk = view.initor.mdb.isStartupfromNetBoot( host.getIP(),host.getPort() );
            if( isOk ){
                if( !view.initor.mdb.isStartupFromNetBoot() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromNet")
                    );
                    return;
                }
            }else{
                JOptionPane.showMessageDialog( view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_IS_STARTUP_FROM_NET )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }

            String targetSrvName = view.initor.mdb.getHostName();
            if( targetSrvName.equals("") ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("EditProfileDialog.error.getHostNameFailed")
                );
                return;
            }

            isOk = view.initor.mdb.getLVMInfoFromPXELinux( ResourceCenter.CLT_IP_CONF+"/"+host.getID() + ResourceCenter.CONF_LVMINFO );
            if( isOk ){
                lvmInfo = view.initor.mdb.getLVMInfo();
                if( lvmInfo == null || lvmInfo.size() <= 0 ){
SanBootView.log.error( getClass().getName()," #### lvm info from xxx-lvm.conf is empty, exit");
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                    );
                    return;
                }
            }else{
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                );
                return;
            }
System.out.println(" lvm info: \n" + view.initor.mdb.getLVMInfoString() );

            DestAgent da = new DestAgent(
                -1,
                host.getIP(),
                host.getPort(),
                host.getOS(),
                "", // mac
                "",  // desc
                0
            );
            da.setHostType( DestAgent.TYPE_ORI_HOST );
            da.setSrc_Agnt_id( host.getID() );
            da.setHostUUID( host.getUUID() );

            RestoreOrigiDiskForUnixWizardDialog dialog = new RestoreOrigiDiskForUnixWizardDialog( view,da,targetSrvName,lvmInfo,needModUUID );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }else { // DestAgent
            DestAgent da = (DestAgent)selObj;
            BrowserTreeNode chiefNBHNode = da.getFatherNode();
            ChiefNetBootHost chiefNBH = (ChiefNetBootHost)chiefNBHNode.getUserObject();
            BrowserTreeNode hostNode = chiefNBH.getFatherNode();
            Object hostObj = hostNode.getUserObject();

System.out.println(" da os: "+ da.getComment() );
            if( da.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.isWinHost")
                );
                return;
            }

            // 判断是否从 network 启动����
            isOk = view.initor.mdb.isStartupfromNetBoot( da.getDst_agent_ip(),da.getDst_agent_port() );
            if( isOk ){
                if( !view.initor.mdb.isStartupFromNetBoot() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromNet")
                    );
                    return;
                }
            }else{
                JOptionPane.showMessageDialog( view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_IS_STARTUP_FROM_NET )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }

            String targetSrvName = view.initor.mdb.getHostName();
            if( targetSrvName.equals("") ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("EditProfileDialog.error.getHostNameFailed")
                );
                return;
            }

            String tftpRootPath = view.initor.mdb.getTftpRootPath();
            if( tftpRootPath.equals("") ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("EditProfileDialog.error.tftp")
                );
                return;
            }

            isOk = view.initor.mdb.listDir( tftpRootPath+"/"+da.getDst_agent_mac().toLowerCase()+"/" );
            if( !isOk ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                );
                return;
            }else{
                ArrayList fList = view.initor.mdb.getFileList();
                if( fList.size() == 0 ){
 SanBootView.log.error( getClass().getName(),"no files found in specified dir." );
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                    );
                    return;
                }else{
                    // get iBootInfoxxx file
                    fList = getFileList( fList );
                    if( fList.size() == 0 ){
SanBootView.log.error( getClass().getName(),"not to find iBootInfo-xxx file" );
                        JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                        );
                        return;
                    }else{
                        String conf = ((UnixFileObj)fList.get(0)).getAbsPath();
                        isOk = view.initor.mdb.getLVMInfoFromPXELinux( conf );
                        if( isOk ){
                            lvmInfo = view.initor.mdb.getLVMInfo();
                            if( lvmInfo == null || lvmInfo.size() <= 0 ){
SanBootView.log.error( getClass().getName()," #### lvm info from xxx-lvm.conf is empty, exit");
                                JOptionPane.showMessageDialog( view,
                                    SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                                );
                                return;
                            }
                        }else{
                            JOptionPane.showMessageDialog( view,
                                SanBootView.res.getString("MenuAndBtnCenter.error.getLVMInfo")
                            );
                            return;
                        }
System.out.println(" lvm info: \n" + view.initor.mdb.getLVMInfoString() );
                     }
                }
            }

            if( hostObj instanceof BootHost ){
                BootHost bh = (BootHost)hostObj;
                da.setSrc_Agnt_id( bh.getID() );
                da.setHostType( DestAgent.TYPE_DST_AGNT );
                da.setHostUUID( bh.getUUID() );

                // 获取bh的网卡信息
                isOk = view.initor.mdb.getUnixNetCardFromSrv(  ResourceCenter.CLT_IP_CONF + "/" + bh.getID() + ResourceCenter.CONF_IP );
                if( !isOk ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("InitBootHostWizardDialog.log.getIpInfo")+" "+
                        SanBootView.res.getString("common.failed")
                    );
                    return;
                }else{
                    ArrayList netCardList = view.initor.mdb.getUnixNetCardFromSrv();
                    if( !this.isContainedInSet( netCardList, da.getDst_agent_mac() ) ){
                        needModUUID = true;
                    }
                }
            }else{
                SourceAgent sa = (SourceAgent)hostObj;
                da.setSrc_Agnt_id( sa.getSrc_agnt_id() );
                da.setHostType( DestAgent.TYPE_SRC_AGNT );
                da.setRealBootHostFlag( false );
                needModUUID = true;
            }

            RestoreOrigiDiskForUnixWizardDialog dialog = new RestoreOrigiDiskForUnixWizardDialog( view,da,targetSrvName,lvmInfo,needModUUID );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }
SanBootView.log.info(getClass().getName(),"########### End of linux host's restore original disk action. " );
    }

    public ArrayList getFileList( ArrayList fileList ){
        String name;
        String[] line;

        ArrayList ret = new ArrayList();

        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = ( UnixFileObj )fileList.get(i);
            name = one.getName();
            if( name == null || name.equals("") )
                continue;

            if( !one.isDir() ){
System.out.println(" file name: " + name );
                line = Pattern.compile("\\.").split( name );
                if( line.length > 2 ){
                    String end = line[line.length-1];
System.out.println(" end: "+ end );
                    if( line[0].equals("iBootInfo") ){
                        if( end.equals("NONE") || end.equals("LVM1") || end.equals("LVM2") ){
                            ret.add( one );
                        }
                    }
                }
            }
        }

        return ret;
    }

    private boolean isContainedInSet( ArrayList<UnixNetCard> list,String mac ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            UnixNetCard nc = list.get(i);
            String smac1 = DhcpClientInfo.getSimpleMac( nc.mac );
            String smac2 = DhcpClientInfo.getSimpleMac( mac );
            if( smac1.equals( smac2) ){
                return true;
            }
        }
        return false;
    }
}
