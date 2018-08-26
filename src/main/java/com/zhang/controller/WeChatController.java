package com.zhang.controller;

import com.zhang.service.CoreService;
import com.zhang.util.WeChatUtil;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

/**
 * @author ZhangRuiyuan
 */
@Controller
@RequestMapping(value = "/CoreWeChat")
public class WeChatController {

    @RequestMapping(method = RequestMethod.GET)
    public void check(HttpServletRequest request, HttpServletResponse response) {
        boolean isGet = "get".equals(request.getMethod().toLowerCase());
        PrintWriter print;
        if(isGet){
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && WeChatUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        boolean isPost = "post".equals(request.getMethod().toLowerCase());
        PrintWriter print;
        if(isPost){
            String respXml = CoreService.processRequest(request);
            try {
                print = response.getWriter();
                print.write(respXml);
                print.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
