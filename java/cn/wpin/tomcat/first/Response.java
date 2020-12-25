package cn.wpin.tomcat.first;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 最简单版Tomcat Response对象
 *
 * @author wpin
 */
public class Response {

    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream output;

    /**
     * 接收Socket传过来的OutputStream对象
     *
     * @param output OutputStream对象
     */
    public Response(OutputStream output) {
        this.output = output;
    }

    /**
     * 获取到把inputStream封装好的Request对象
     *
     * @param request 把inputStream封装好的Request对象
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * 发送静态资源给客户端
     * 此处有个问题，谷歌浏览器会请求多次，导致数据丢失，用其他浏览器可以
     *
     * @throws IOException
     */
    void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            //根据请求uri到指定目录去获取
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            //文件存在，则通过输出流输出到客户端
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
// file not found
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
// thrown if cannot instantiate a File object
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
