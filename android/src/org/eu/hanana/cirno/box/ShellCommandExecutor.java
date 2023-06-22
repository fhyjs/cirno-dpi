package org.eu.hanana.cirno.box;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ShellCommandExecutor {
    public static ExecResult executeCommand(String base,String command) {
        ExecResult result = new ExecResult();
        StringBuilder output = new StringBuilder();

        try {
            // 执行Shell命令
            Process process = Runtime.getRuntime().exec(base);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write(command+"\n");
            writer.flush();
            writer.write("exit\n");
            writer.flush();
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null||(line = reader1.readLine()) != null) {
                output.append(line).append("\n");
            }
            // 等待命令执行完成
            process.waitFor();
            writer.close();
            reader1.close();
            reader.close();
            // 获取命令的退出状态码
            result.exitCode= process.exitValue();
            result.output=output.toString();
            result.success=process.exitValue()==0;
            result.error=false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            result.output=e.getMessage();
            result.error=true;
            result.success=false;
        }
        return result;
    }
}
