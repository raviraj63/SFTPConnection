package org.example;

import com.jcraft.jsch.*;
import java.util.Vector;

public class SFTPFileChecker {

    // SFTP Server details
    private static final String SFTP_HOST = "eu-central-1.sftpcloud.io";
    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "a014740d676145998cd5a4c672f5aea2";
    private static final String SFTP_PASSWORD = "BxLRvaIk8J9ojy5GY0nqFh9EN7ecULcp";
    private static final String SFTP_DIRECTORY = "/";

    // Expected file names
    private static final String[] EXPECTED_FILES = {
            "Test11.txt","Test22.txt","Test33.txt"
    };

    public static void main(String[] args) {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.cd(SFTP_DIRECTORY);

            Vector<ChannelSftp.LsEntry> files = channelSftp.ls(".");

            for (String expectedFile : EXPECTED_FILES) {
                boolean found = false;
                for (ChannelSftp.LsEntry file : files) {
                    if (file.getFilename().equals(expectedFile)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    System.out.println("✅ File found: " + expectedFile);
                } else {
                    System.out.println("❌ File NOT found: " + expectedFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
