/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class LicenseCount {
    
    public static final String SYSTEM = "system";
    public static final String DATABASE = "database";
    public static final String COUNT = "count";
    
    private int systemLicense = 0;
    private int databaseLicense = 0;

    public int getDatabaseLicense() {
        return databaseLicense;
    }

    public void setDatabaseLicense(int databaseLicense) {
        this.databaseLicense = databaseLicense;
    }

    public int getSystemLicense() {
        return systemLicense;
    }

    public void setSystemLicense(int systemLicense) {
        this.systemLicense = systemLicense;
    }
    
}
