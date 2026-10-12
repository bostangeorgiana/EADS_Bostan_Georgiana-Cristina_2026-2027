package com.example.lab2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// legam tot intr-un obiect
@Component
@ConfigurationProperties(prefix = "app.data")
public class DataProperties {

    private String file; // app.data.file
    private String format; // app.data.format
    private String delimiter; // app.data.delimiter
    private boolean skipHeader; // app.data.skip-header
    private String encoding; // app.data.encoding
    private String label; // app.data.label

    // Getters and setters
    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getDelimiter() { return delimiter; }
    public void setDelimiter(String delimiter) { this.delimiter = delimiter; }

    public boolean isSkipHeader() { return skipHeader; }
    public void setSkipHeader(boolean skipHeader) { this.skipHeader = skipHeader; }

    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding = encoding; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}