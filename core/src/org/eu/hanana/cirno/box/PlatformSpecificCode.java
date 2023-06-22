package org.eu.hanana.cirno.box;
public interface PlatformSpecificCode {
    String getStringResource(String resourceId) throws NoSuchFieldException, IllegalAccessException;
    ExecResult executeShellCmd(String base, String cmd);

    int getDeviceDpi();

    void log(int priority, String tag, String msg);
}
