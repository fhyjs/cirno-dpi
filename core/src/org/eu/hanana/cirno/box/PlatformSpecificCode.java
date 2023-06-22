package org.eu.hanana.cirno.box;
public interface PlatformSpecificCode {
    String getStringResource(String resourceId) throws NoSuchFieldException, IllegalAccessException;
    ExecResult executeShellCmd(String cmd);

    int getDeviceDpi();
}
