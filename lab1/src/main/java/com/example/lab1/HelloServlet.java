package com.example.lab1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // Get the "name" parameter from the query string
        String name = request.getParameter("name");
        if (name == null || name.isBlank()) {
            name = "World";
        }
        // Set response type to plain text
        response.setContentType("text/plain"); // MIME type
        // Write the response to the client
        try (PrintWriter out = response.getWriter()) {
            out.println("Hello " + name);
        }
    }
}