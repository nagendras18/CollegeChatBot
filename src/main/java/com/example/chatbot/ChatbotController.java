package com.example.chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * REST Controller to handle API requests for the chatbot.
 * This class serves as the backend, connecting to the database
 * to retrieve answers for the chatbot.
 */
@RestController
public class ChatbotController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatbotController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Handles GET requests to the /ask endpoint.
     * It queries the 'faq' table in the database to find an answer
     * for the given question.
     *
     * @param question The user's question, passed as a request parameter.
     * @return The corresponding answer from the database, or a default message.
     */
    @GetMapping("/ask")
    public String askQuestion(@RequestParam String question) {
        String sql = "SELECT answer FROM faq WHERE question LIKE ?";
        String normalizedQuestion = "%" + question.toLowerCase() + "%";
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, normalizedQuestion);

        if (!rows.isEmpty()) {
            // Return the first matching answer found
            return (String) rows.get(0).get("answer");
        }
        
        // If no match is found, return a default message
        return "I'm sorry, I couldn't find an answer to that. Please try rephrasing.";
    }
}

