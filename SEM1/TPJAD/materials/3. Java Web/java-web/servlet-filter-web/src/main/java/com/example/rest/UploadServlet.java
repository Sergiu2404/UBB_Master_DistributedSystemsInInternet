package com.example.rest;

import java.io.IOException;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String uploadDir = request.getParameter("uploadDir");
        doUpload(request, uploadDir);
    }
            
    private void doUpload(HttpServletRequest request, String uploadDir) {
        try {
            for (Part part : request.getParts()) {
                String fileName = getFileName(part);
                if (fileName.isEmpty()) continue;
                part.write(uploadDir+"/"+fileName);
                part.delete(); // sterge fisierul temporar asociat
                System.out.println("Lungimea fisierului \"" + fileName + "\": "
                    + part.getSize());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }
}
