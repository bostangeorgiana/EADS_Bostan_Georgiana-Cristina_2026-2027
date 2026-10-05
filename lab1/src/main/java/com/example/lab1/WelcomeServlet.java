package com.example.lab1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // Set response type to HTML
        response.setContentType("text/html"); // MIME type
        response.setCharacterEncoding("UTF-8");

        // Write the (dynamically generated) page to the client
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><meta charset='UTF-8'><title>Welcome</title></head><body>");
            out.println("<h1>Welcome!</h1>");
            out.println("<p>Page generated at: " + LocalDateTime.now() + "</p>");
            out.println("<form action='controller' method='post'>");
            out.println("  <label><input type='radio' name='page' value='1' checked> 1</label>");
            out.println("  <label><input type='radio' name='page' value='2'> 2</label>");
            out.println("  <button type='submit'>Submit</button>");
            out.println("</form>");
            out.println("</body></html>");
        }
    }
}