package com.example.rest;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String fileServer = request.getParameter("fileServer");
        doDownload(fileServer, response);
        System.out.println("S-a downloadat fisierul \"" + fileServer + "\"");
    }

    private void doDownload(String fileServer, HttpServletResponse response) {
        try {
            File fs = new File(fileServer);
            String mimeType = getServletContext().getMimeType(fileServer);
            if (mimeType == null) mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setContentLength((int) fs.length());
            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + fs.getName() + "\"");
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(fileServer);
            for (; ; ) {
                byte[] b = new byte[4096];
                int n = in.read(b, 0, b.length);
                if (n < 0) {
                    break;
                }
                out.write(b, 0, n);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
