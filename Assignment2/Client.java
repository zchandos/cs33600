import java.io.*;

/*
    Course: CS 33600
    Name: Zachary Chandos
    Email: zchandos@pnw.edu
    Assignment: HW2

*/

public class Client {
    public static void main(String[] args) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in);
            byte[] buffer = new byte[128];
            int bytesRead = 0;

            while (true) {
                int header = bufferedInputStream.read();

                if (header == -1) {
                    System.out.printf("End of file at byte: %d.\n", bytesRead);
                    System.exit(-1);
                }

                bytesRead++;

                if ((header & 0x80) == 0x80) {
                    System.out.printf("\nRead %d bytes from standard input.\n", bytesRead);
                    break;
                } else if ((header & 0x80) == 0) {
                    int messageType = header & 0x7F;

                    while (messageType != 0) {
                        int type = messageType & 1;

                        if (type == 0) {
                            try {
                                bytesRead += readAndPrintInt(bufferedInputStream, buffer);
                            } catch (IOException e) {
                                System.err.printf("End of file at byte: %d.\n", bytesRead);
                                System.exit(-1);
                            }
                        } else {
                            try {
                                bytesRead += readAndPrintDouble(bufferedInputStream, buffer);
                            } catch (IOException e) {
                                System.err.printf("End of file at byte: %d.\n", bytesRead);
                                System.exit(-1);
                            }
                        }

                        messageType >>= 1;
                    }
                } else {
                    try {
                        bytesRead += readAndPrintText(bufferedInputStream, buffer, header & 0x7F);
                    } catch (IOException e) {
                        System.err.printf("End of file at byte: %d.\n", bytesRead);
                        System.exit(-1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int readAndPrintInt(BufferedInputStream bufferedInputStream, byte[] buffer) throws IOException {
        int bytesRead = 0;
        int value = readInt(bufferedInputStream, buffer);
        System.out.println(value);
        bytesRead += 4;
        return bytesRead;
    }

    private static int readAndPrintDouble(BufferedInputStream bufferedInputStream, byte[] buffer) throws IOException {
        int bytesRead = 0;
        double value = readDouble(bufferedInputStream, buffer);
        System.out.println(value);
        bytesRead += 8;
        return bytesRead;
    }

    private static int readAndPrintText(BufferedInputStream bufferedInputStream, byte[] buffer, int length) throws IOException {
        int bytesRead = 0;
        String text = readText(bufferedInputStream, buffer, length);
        System.out.println(text);
        bytesRead += length;
        return bytesRead;
    }

    private static int readInt(BufferedInputStream bufferedInputStream, byte[] buffer) throws IOException {
        bufferedInputStream.read(buffer, 0, 4);
        return ((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16) | ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF);
    }

    private static double readDouble(BufferedInputStream bufferedInputStream, byte[] buffer) throws IOException {
        bufferedInputStream.read(buffer, 0, 8);
        long value = ((long)(buffer[0] & 0xFF) << 56) | ((long)(buffer[1] & 0xFF) << 48) |
                ((long)(buffer[2] & 0xFF) << 40) | ((long)(buffer[3] & 0xFF) << 32) |
                ((long)(buffer[4] & 0xFF) << 24) | ((long)(buffer[5] & 0xFF) << 16) |
                ((long)(buffer[6] & 0xFF) << 8) | (buffer[7] & 0xFF);
        return Double.longBitsToDouble(value);
    }

    private static String readText(BufferedInputStream bufferedInputStream, byte[] buffer, int length) throws IOException {
        bufferedInputStream.read(buffer, 0, length);
        return new String(buffer, 0, length);
    }
}
