package cn.wpin.tomcat.first;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单版本Tomcat HttpServer对象
 *
 * 总结
 * 在这章中你已经看到一个简单的 web 服务器是如何工作的。这章附带的程序仅仅由三个类组
 * 成，并不是全功能的。不过，它提供了一个良好的学习工具。下一章将要讨论动态内容的处理过
 * 程。
 *
 * @author wpin
 */
public class HttpServer {


    /**
     * 存放静态文件文件加，可用来测试，如我在浏览器输入 http://localhost:8080/1.txt
     * 则会到我们指定的目录区找1.txt文件
     */
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "webroot";

    /**
     * 关闭请求参数，当输入的URI为这个，则关闭服务，类比tomcat的关闭服务逻辑
     */
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    /**
     * 关闭实例标识
     */
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    /**
     * 创建一个ServerSocket实例,相当于是一台服务器，然后跳过while循环一直监听者客户端的连接
     */
    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            //监听127.0.0.1：8080
            serverSocket = new ServerSocket(port, 1,
                    InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            //异常则退出，如端口被占用
            e.printStackTrace();
            System.exit(1);
        }
// 循环监听请求
        while (!shutdown) {
            Socket socket;
            InputStream input;
            OutputStream output;
            try {
                //接收客户端的连接
                socket = serverSocket.accept();
                //创建输入输出流
                input = socket.getInputStream();
                output = socket.getOutputStream();
                // 创建一个Request对象，并调用parse方法解析Http请求
                Request request = new Request(input);
                request.parse();
                // 创建一个Response对象，把request对象设置给他，并调用sendStaticResource方法
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                //关闭连接
                socket.close();
                //如果指令是shutdown，则关闭服务
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


