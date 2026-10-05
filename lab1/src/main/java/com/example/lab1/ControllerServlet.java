package com.example.lab1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the "page" parameter (local variable -> thread safe, slide 34)
        String page = request.getParameter("page");

        // Write info about the request in the server log (slide 36)
        logRequest(request, page);

        // Validation (slide 32)
        if (!"1".equals(page) && !"2".equals(page)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Parameter 'page' must be 1 or 2");
            return;
        }

        // Browser or desktop app?
        String accept = request.getHeader("Accept");
        boolean fromBrowser = accept != null && accept.contains("text/html");

        if (fromBrowser) {
            // Forward to page1.html or page2.html (slide 35)
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/page" + page + ".html");
            dispatcher.forward(request, response);
        } else {
            // Plain text containing only the value of the parameter
            response.setContentType("text/plain"); // MIME type
            try (PrintWriter out = response.getWriter()) {
                out.println(page);
            }
        }
    }

    private void logRequest(HttpServletRequest request, String page) {
        ServletContext context = this.getServletContext();
        context.log("HTTP method: " + request.getMethod()
                + " | Client IP: " + request.getRemoteAddr()
                + " | User-Agent: " + request.getHeader("User-Agent")
                + " | Languages: " + Collections.list(request.getLocales())
                + " | Parameter page = " + page);
    }
}