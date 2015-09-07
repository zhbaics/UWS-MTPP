/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class MirrorJobInfo {

    public static final String MIRROR_JOB_SUM = "summj";
    public static final String MIRROR_JOB_ERR = "errmj";
    public static final String MIRROR_JOB_DETAIL = "mjinfo";
    public static final String MIRROR_JOB_READ = "mjrio";
    public static final String MIRROR_JOB_WRITE = "mjwio";
    private String mirrorJobSum = "";
    private String mirrorJobErr = "";
    private Vector mirrorJobDetailTime = new Vector();
    private Vector mirrorJobDetailResult = new Vector();
    private Vector mirrorJobDetailVersion = new Vector();
    private Vector mirrorJobReadTime = new Vector();
    private Vector mirrorJobReadIO = new Vector();
    private Vector mirrorJobWriteTime = new Vector();
    private Vector mirrorJobWriteIO = new Vector();

    /** Creates a new instance of MirrorJobInfo */
    public MirrorJobInfo() {
    }

    public String getMirrorJobSum() {
        return mirrorJobSum;
    }

    public void addMirrorJobSum(String _mirrorJobSum) {
        mirrorJobSum = _mirrorJobSum;
    }

    public String getMirrorJobErr() {
        return mirrorJobErr;
    }

    public void addMirrorJobErr(String _mirrorJobErr) {
        mirrorJobErr = _mirrorJobErr;
    }

    public Vector getMirrorJobDetailTime() {
        return mirrorJobDetailTime;
    }

    public void addMirrorJobDetailTime(String _mirrorJobDetailTime) {
        mirrorJobDetailTime.add(_mirrorJobDetailTime);
    }

    public Vector getMirrorJobDetailResult() {
        return mirrorJobDetailResult;
    }

    public void addMirrorJobDetailResult(String _mirrorJobDetailResult) {
        mirrorJobDetailResult.add(_mirrorJobDetailResult);
    }

    public Vector getMirrorJobDetailVersion() {
        return mirrorJobDetailVersion;
    }

    public void addMirrorJobDetailVersion(String _mirrorJobDetailVersion) {
        mirrorJobDetailVersion.add(_mirrorJobDetailVersion);
    }

    public Vector getMirrorJobReadTime() {
        return mirrorJobReadTime;
    }

    public void addMirrorJobReadTime(String _mirrorJobReadTime) {
        mirrorJobReadTime.add(_mirrorJobReadTime);
    }

    public Vector getMirrorJobReadIO() {
        return mirrorJobReadIO;
    }

    public void addMirrorJobReadIO(String _mirrorJobReadIO) {
        mirrorJobReadIO.add(_mirrorJobReadIO);
    }

    public Vector getMirrorJobWriteTime() {
        return mirrorJobWriteTime;
    }

    public void addMirrorJobWriteTime(String _mirrorJobWriteTime) {
        mirrorJobWriteTime.add(_mirrorJobWriteTime);
    }

    public Vector getMirrorJobWriteIO() {
        return mirrorJobWriteIO;
    }

    public void addMirrorJobWriteIO(String _mirrorJobWriteIO) {
        mirrorJobWriteIO.add(_mirrorJobWriteIO);
    }
}
