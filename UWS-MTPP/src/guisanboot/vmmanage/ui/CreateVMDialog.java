/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CreateVMDialog.java
 *
 * 创建虚拟机窗口
 *
 * Created on 2012-12-18, 9:09:16
 */
package guisanboot.vmmanage.ui;

import guisanboot.data.BindofVolAndSnap;
import guisanboot.data.SnapWrapper;
import guisanboot.data.VMHostInfo;
import guisanboot.data.View;
import guisanboot.data.ViewWrapper;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * 创建虚拟机窗口
 * @author Administrator
 */
public class CreateVMDialog extends javax.swing.JDialog {

    public final static int OP_CREATE = 1;//创建
    public final static int OP_MODIFY = 2;//修改

    //两个参数的构造器
    public CreateVMDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    //四个参数的构造器
    public CreateVMDialog(SanBootView view, int op, VMHostInfo vmHostInfo, Vector _bindlist) {
        this(view, true);
        myInit(view, op, vmHostInfo, _bindlist);
    }

    private Object[] values = null;
    private SanBootView view = null;
    private int oper = 1;
    private VMHostInfo vmhost;
    private Vector bindlist = null;
    ArrayList<String> usedvip = new ArrayList<String>();//用过的虚拟机的ip的集合
    ArrayList<String> idlevip = new ArrayList<String>();//空闲的虚拟机的ip的集合

    //初始化
    public void myInit(SanBootView _view, int op, VMHostInfo vmHostInfo, Vector _bindlist) {
        this.view = _view;
        usedvip = view.initor.mdb.getUsedVip();//获得用过的虚拟机ip
        idlevip = view.initor.mdb.getIdleVip();//获得空闲的虚拟机ip
        this.oper = op;
        this.vmhost = vmHostInfo;//虚拟机主机信息
        this.bindlist = _bindlist;
        initJComboBox(this.bindlist);//初始化组合框
        initVMHostInfo(vmHostInfo);//初始化虚拟主机信息
        setLanauge();//设置页面文字信息
    }

    //初始化组合框,即下拉选
    public void initJComboBox(Vector list) {
        int size = list.size();
        Object binderObj;
        boolean isfirst = true;
        jComboBox2.removeAll();
        jComboBox1.removeAll();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                binderObj = list.elementAt(i);
                if (binderObj instanceof BindofVolAndSnap) {
                    VolumeMap vm = ((BindofVolAndSnap) binderObj).getVolMap();
                    if (vm.isOsVolMap()) {
                        jComboBox1.addItem(((BindofVolAndSnap) binderObj).getVolMap());
                        System.out.println(((BindofVolAndSnap) binderObj).getVolMap());
                        if (isfirst) {
                            addSomethingInJComboBox2(((BindofVolAndSnap) binderObj).getSnapList());
                            isfirst = false;
                        }
                    }
                }
            }
        }
    }

    //初始化虚拟机主机信息---------虚拟机主机信息
    public void initVMHostInfo(VMHostInfo vmh) {
        if (vmh != null) {
            jTextField1.setText(vmh.getVm_ip());
            jTextField2.setText(vmh.getVm_port());
            if (vmh.getVm_vip() != null && !"".equals(vmh.getVm_vip())) {
                jTextField3.setText(vmh.getVm_vip());
            } else {
                jTextField3.setText(getIdleVMHostVip());
            }
            String vmpath = vmh.getVm_path();
            jTextField4.setText(vmpath.replaceAll("&nbsp", " "));

            if (vmh.getVm_letter() != null && !"".equals(vmh.getVm_letter())) {
                setSomethingInJCombobox(vmh.getVm_letter(), vmh.getVm_viewid());
            }
        }
    }

    //是否为使用过的虚拟ip
    public boolean isUsedVip(String ip) {
        for (int i = 0; i < usedvip.size(); i++) {
            if (ip.equals(usedvip.get(i))) {
                return true;
            }
        }
        return false;
    }

    //得到空闲的虚拟机主机的虚拟ip
    public String getIdleVMHostVip() {
        int isize = idlevip.size();
        boolean isidlevip = true;
        String vip = "";
        if (isize > 0) {
            for (int i = isize - 1; i >= 0; i--) {
                String idleip = idlevip.get(i);
                for (int j = 0; j < usedvip.size(); j++) {
                    if (idleip.equals(usedvip.get(j))) {
                        isidlevip = false;
                        break;
                    }
                }
                if (isidlevip) {
                    vip = idleip;
                    break;
                } else {
                    isidlevip = true;
                }
            }
        }
        return vip;
    }

    //在组合框(下拉选)内设置选项---参数-------------证书------------视图id
    public void setSomethingInJCombobox(String letter, String viewid) {
        int id = Integer.parseInt(viewid);
        for (int i = 0; i < this.bindlist.size(); i++) {
            VolumeMap vm = ((BindofVolAndSnap) this.bindlist.get(i)).getVolMap();
            if (vm.getVolDiskLabel().toLowerCase().startsWith(letter)) {
                jComboBox1.setSelectedItem(vm);
                ArrayList list = ((BindofVolAndSnap) this.bindlist.get(i)).getSnapList();
                for (int j = 0; j < list.size(); j++) {
                    Object obj = list.get(j);
                    if (obj instanceof ViewWrapper) {
                        ViewWrapper vw = (ViewWrapper) obj;
                        if (vw.view.getSnap_localid() == id) {
                            jComboBox2.setSelectedItem(obj);
                        }
                    }
                }
            }
        }
    }

    //设置页面文字信息
    public void setLanauge() {
        this.jButton1.setText(SanBootView.res.getString("CreateVM.label.ok"));
//        this.jButton2.setText( SanBootView.res.getString("CreateVM.label.modify"));
        this.jButton3.setText(SanBootView.res.getString("CreateVM.label.cancel"));
        this.jLabel1.setText(SanBootView.res.getString("CreateVM.label.ip"));
        this.jLabel2.setText(SanBootView.res.getString("CreateVM.label.port"));
        this.jLabel3.setText(SanBootView.res.getString("CreateVM.label.vip"));
        this.jLabel4.setText(SanBootView.res.getString("CreateVM.label.path"));
        this.jLabel5.setText(SanBootView.res.getString("CreateVM.label.letter"));
        this.jLabel6.setText(SanBootView.res.getString("CreateVM.label.snap-view"));
        this.jCheckBox1.setText(SanBootView.res.getString("CreateVM.label.recoverIP"));
        this.jLabel8.setText(SanBootView.res.getString("CreateVM.label.standby_maxTime"));
        this.jLabel9.setText(SanBootView.res.getString("CreateVM.label.standby_ip"));
        this.jCheckBox2.setText(SanBootView.res.getString("CreateVM.label.standby_check"));
        this.jPanel2.remove(this.jLabel5);
        this.jPanel2.remove(this.jComboBox1);

        Font f = new Font("Dialog", 0, 24);
        this.jLabel7.setFont(f);
        if (this.oper == OP_CREATE) {
            this.jLabel7.setText(SanBootView.res.getString("CreateVM.label.tip"));
        } else {
            this.jLabel7.setText(SanBootView.res.getString("CreateVM.label.tip2"));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 80));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 40));

        jButton1.setText("ok");
        jButton1.setPreferredSize(new java.awt.Dimension(70, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton3.setText("cancel");
        jButton3.setPreferredSize(new java.awt.Dimension(70, 23));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);
        jPanel1.add(jSeparator1, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(10, 38));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel6.add(jCheckBox1);

        jCheckBox2.setText("standby_check");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel6.add(jCheckBox2);

        jPanel1.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 260));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("ip:");
        jLabel1.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel1, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField1, gridBagConstraints);

        jLabel2.setText("port:");
        jLabel2.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel2, gridBagConstraints);

        jTextField2.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField2, gridBagConstraints);

        jLabel3.setText("vip:");
        jLabel3.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        jTextField3.setPreferredSize(new java.awt.Dimension(250, 21));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField3, gridBagConstraints);

        jLabel4.setText("path:");
        jLabel4.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel4, gridBagConstraints);

        jTextField4.setPreferredSize(new java.awt.Dimension(250, 21));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField4, gridBagConstraints);

        jLabel5.setText("letter:");
        jLabel5.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);

        jComboBox1.setPreferredSize(new java.awt.Dimension(250, 21));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jComboBox1, gridBagConstraints);

        jLabel6.setText("snap-view:");
        jLabel6.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        jComboBox2.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jComboBox2, gridBagConstraints);

        jTextField5.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField5, gridBagConstraints);

        jLabel8.setText("standby_maxTime:");
        jLabel8.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel8, gridBagConstraints);

        jTextField6.setPreferredSize(new java.awt.Dimension(250, 21));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField6, gridBagConstraints);

        jLabel9.setText("stanby_ip:");
        jLabel9.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jLabel9, gridBagConstraints);

        jTextField7.setPreferredSize(new java.awt.Dimension(250, 21));
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(jTextField7, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel4.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel7.setText("jLabel7");
        jPanel5.add(jLabel7, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);
        jPanel4.add(jSeparator2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

//确认按钮的事件--没有被提出来成为单独的方法
private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    String strip = jTextField1.getText().trim();//获得宿主机ip信息
    Matcher matcher = pattern.matcher(strip);//根据ip找到的匹配程序
    if (!matcher.matches()) {//如果不能够匹配
        JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateVMHost.error.wrongIP"));//IP格式错误，请重新输入IP
        return;
    }
    String strvip = jTextField3.getText().trim();//获得虚拟机ip信息
    if (strvip != null && !"".equals(strvip)) {//判断不为 NULL或""
        matcher = pattern.matcher(strvip);//根据虚拟机ip找到匹配器
        if (!matcher.matches()) {//如果不能够匹配
            JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("CreateVMHost.error.wrongIP"));//IP格式错误，请重新输入IP
            return;
        }

        String oldvip = this.vmhost.getVm_vip();//在虚拟主机中获得虚拟机ip
        if (!strvip.equals(oldvip)) {//判断--虚拟主机中获得的虚拟机ip与之前文本框中获得的虚拟机ip是否一致
            if (isUsedVip(strvip)) {//判断--文本框中获得的虚拟机ip是否被占用
                JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("CreateVMHost.error.usedIP"));//该IP已经被使用，请重新填写VIP。
                return;
            }
        }
    } else {
        strvip = strip;
    }

    int port = -1;
    String strport = jTextField2.getText().trim();//获得第二个文本框中的端口号
    try {
        port = Integer.valueOf(strport);//强转成int
        if (port < 0 || port > 65535) {//判断端口格式
            JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("CreateVMHost.error.wrongPort"));//端口格式错误，请重新输入
            return;
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateVMHost.error.wrongPort"));//端口格式错误，请重新输入
        return;
    }

    String strpath = jTextField4.getText().trim();//从第四个文本框中获取路径
    if (!strpath.endsWith(".vmx")) {//判断是否以.vmx结尾
        JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateVMHost.error.wrongpath"));//路径格式错误，请重新输入
        return;
    }

    String standby_maxTime = jTextField6.getText().trim();  //从第五个文本框中获取待机超时时间
    
    String standby_ip = jTextField7.getText().trim();       //从第六个文本框中获取待机检测IP
    if(!"".equals(standby_ip)&&standby_ip!=null){
        matcher = pattern.matcher(standby_ip);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("CreateVMHost.error.wrongIP"));//IP格式错误，请重新输入IP
            return;
        }
    }
    
    String standby_isCheck;
    if(this.jCheckBox2.isSelected()){
        standby_isCheck = "1";
    } else{
        standby_isCheck = "0";
    }

    String tpath = strpath.replaceAll("\\\\", "/");//替换 不明白\\\\代表什么 获得地址值
    String strname = strpath.substring(tpath.lastIndexOf("/") + 1, tpath.length());//通过截取获得某名字
    String vm_name = vmhost.getVm_name();//通过虚拟机主机获得虚拟机名
    if ("".equals(vm_name)) {//如果虚拟机名为空
        vm_name = strname.substring(0, strname.lastIndexOf("."));//虚拟机名为刚得到的名字的 . 前内容
    }

    Object lObj = jComboBox1.getSelectedItem();//获得第一个下拉选选中项--证书--页面中不显示
    String letter = "";//证书
    String targetid = "";//目标id
    String snapid = "";//临时id
    String viewid = "";//视图id
    if (lObj instanceof VolumeMap) {//如果选中的启动快照在卷集合中存在
        VolumeMap volmap = (VolumeMap) lObj;//强制类型转换
        if (volmap.getVolDiskLabel().toLowerCase().startsWith("c")) {//如果获得的字符串以c开头(忽略大小写)
            letter = "c";
            Object obj = jComboBox2.getSelectedItem();//第二个下拉选的选中项--快照
            if (obj instanceof SnapWrapper) {//如果目标符合快照格式
                SnapWrapper swrapper = (SnapWrapper) obj;//强制类型转换
                String newViewName = ResourceCenter.NET_START_VIEW + letter.toUpperCase() + "_used_by_virtual_machine";//创建新的视图名称
                boolean isOk = view.initor.mdb.addView(newViewName, swrapper.snap.getSnap_root_id(), swrapper.snap.getSnap_local_snapid());//添加视图 返回是否成功
                if (isOk) {//添加视图成功
                    View newView = view.initor.mdb.getCrtView();//定位刚创建的视图
                    targetid = String.valueOf(newView.getTargetID());//获得目标id
                    viewid = String.valueOf(newView.getSnap_localid());//获得视图id
                } else {//添加视图失败
                    JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("CreateVMHost.error.wrongcrtview"));//为所选快照创建副本失败，请重新操作
                    return;
                }
            } else if (obj instanceof ViewWrapper) {//如果目标符合视图格式
                ViewWrapper vwrapper = (ViewWrapper) obj;//强制类型转换
                targetid = String.valueOf(vwrapper.view.getTargetID());//获得目标id
                viewid = String.valueOf(vwrapper.view.getSnap_localid());//获得视图id
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("CreateVMHost.error.wrongletter"));//请选择系统盘
            return;
        }
    }
    int tmprecoverip = 0;//临时恢复id
    if (this.jCheckBox1.isSelected()) {//第一个下拉选有选中项
        tmprecoverip = 1;
    } else {//没有选中项
        tmprecoverip = 0;
    }
    String fianlpath = tpath.trim().replaceAll(" ", "&nbsp");//最终地址
    if (this.vmhost != null) {//虚拟机主机不为空 赋值
        this.vmhost.setVm_ip(strip);
        this.vmhost.setVm_vip(strvip);
        this.vmhost.setVm_port(strport);
        this.vmhost.setVm_path(fianlpath);
        this.vmhost.setVm_name(vm_name);
        this.vmhost.setVm_targetid(targetid);
        this.vmhost.setVm_viewid(viewid);
        this.vmhost.setVm_snapid(snapid);
        this.vmhost.setVm_letter(letter);
        this.vmhost.setVm_recoverip(tmprecoverip);
        this.vmhost.setVm_pingip(standby_ip);
        this.vmhost.setVm_ischeck(standby_isCheck);
        this.vmhost.setVm_maxdisctime(standby_maxTime);

    } else {//虚拟机主机为空 创建一个新的然后赋值
        this.vmhost = new VMHostInfo();
        this.vmhost.setVm_ip(strip);
        this.vmhost.setVm_vip(strvip);
        this.vmhost.setVm_port(strport);
        this.vmhost.setVm_path(fianlpath);
        this.vmhost.setVm_name(vm_name);
        this.vmhost.setVm_targetid(targetid);
        this.vmhost.setVm_viewid(viewid);
        this.vmhost.setVm_snapid(snapid);
        this.vmhost.setVm_letter(letter);
        this.vmhost.setVm_recoverip(tmprecoverip);
        this.vmhost.setVm_pingip(standby_ip);
        this.vmhost.setVm_ischeck(standby_isCheck);
        this.vmhost.setVm_maxdisctime(standby_maxTime);
    }
    values = new Object[1];
    values[0] = this.vmhost;
    dispose();

}//GEN-LAST:event_jButton1ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

    values = null;
    dispose();
}//GEN-LAST:event_jButton3ActionPerformed

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    Object item = jComboBox1.getSelectedItem();
    if (item instanceof VolumeMap) {
        VolumeMap volmap = (VolumeMap) item;
        for (int i = 0; i < this.bindlist.size(); i++) {
            VolumeMap tmp = ((BindofVolAndSnap) this.bindlist.get(i)).getVolMap();
            if (volmap.getVolName().equals(tmp.getVolName())) {
                addSomethingInJComboBox2(((BindofVolAndSnap) this.bindlist.get(i)).getSnapList());
            }
        }
    }

}//GEN-LAST:event_jComboBox1ActionPerformed

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox1ActionPerformed

private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField4ActionPerformed

private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox2ActionPerformed

private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField3ActionPerformed

private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField6ActionPerformed

private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextField7ActionPerformed

    //在第二个下拉选中添加一些选项
    public void addSomethingInJComboBox2(ArrayList list) {
        this.jComboBox2.removeAllItems();
        for (int i = 0; i < list.size(); i++) {
            this.jComboBox2.addItem(list.get(i));
        }
    }

    //主函数 无意义 
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateVMDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateVMDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateVMDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateVMDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CreateVMDialog dialog = new CreateVMDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables

    //给外界用的获得values的方法
    public Object[] getValues() {
        return values;
    }
}
