package com.kharcha.mcp.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** MCP server: propose messages, never sends directly (§17.3). */
@SpringBootApplication
public class McpNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpNotifyApplication.class, args);
    }
}
