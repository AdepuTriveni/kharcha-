package com.kharcha.mcp.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** MCP server: memory search/write (§20). */
@SpringBootApplication
public class McpMemoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpMemoryApplication.class, args);
    }
}
