package com.kharcha.mcp.refund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** MCP server: refund cases + drafts (§23). */
@SpringBootApplication
public class McpRefundApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpRefundApplication.class, args);
    }
}
