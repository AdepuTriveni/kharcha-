package com.kharcha.mcp.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** MCP server: read-only finance tools (§17.3, §18.2, §24). */
@SpringBootApplication
public class McpFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpFinanceApplication.class, args);
    }
}
