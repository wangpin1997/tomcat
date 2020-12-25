package cn.wpin.tomcat.first;

import java.io.IOException;
import java.io.InputStream;

/**
 * 最简单版本Tomcat request对象。
 * Request类代表一个Http请求，从负责与客户端通信的Socket类中接收到inputStream,封装成Request对象
 *
 * @author wpin
 */
public class Request {

    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 转换输入流对象
     */
    public void parse() {
        StringBuilder request = new StringBuilder(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            //调用read()方法，获取HTTP请求的原始数据
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

    /**
     * 获取到请求的uri
     *
     * @param requestString 所有的请求头信息字符串
     * @return URI
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}
