/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.ui.multiRenderTable;

/**
 * MjOption.java
 *
 * Created on 2010-8-27, 18:02:09
 */
public class MjOption {
    public boolean isContinueTransferSeled = false;
    public boolean isEncrySeled = false;
    public boolean isDeleteViewSeled = false;
    public boolean isCompressedSeled = false;
    public boolean isCopyBranchSeled = false;

    public MjOption( boolean isContinueSel,
            boolean isEncrySel,boolean isDeleteViewSel,
            boolean isCompressedSel,boolean isCopyBranchSel
    ){
        this.isContinueTransferSeled = isContinueSel;
        this.isEncrySeled = isEncrySel;
        this.isDeleteViewSeled = isDeleteViewSel;
        this.isCompressedSeled = isCompressedSel;
        this.isCopyBranchSeled = isCopyBranchSel;
    }
}
