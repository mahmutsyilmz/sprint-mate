package com.yilmaz.sprintmate.modules.ai.service;

import com.yilmaz.sprintmate.modules.ai.dto.ProjectBlueprint;
import com.yilmaz.sprintmate.modules.board.enums.TaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Mock AI Service
 * 
 * Simulates an AI that generates project requirements.
 * Used when the real LLM service is unavailable or for testing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectGenerationService {

    private final Random random = new Random();

    /**
     * Generates a random project scenario
     */
    public ProjectBlueprint generateMockProject() {
        log.info("Generating a mock project scenario...");

        List<ProjectBlueprint> scenarios = Arrays.asList(
                createECommerceScenario(),
                createFintechScenario(),
                createSocialMediaScenario());

        // Pick a random scenario
        ProjectBlueprint selected = scenarios.get(random.nextInt(scenarios.size()));
        log.info("Selected scenario: {}", selected.getName());

        return selected;
    }

    private ProjectBlueprint createECommerceScenario() {
        return ProjectBlueprint.builder()
                .name("EcoMarket - Sustainable Marketplace")
                .category("E-Commerce")
                .description("A niche marketplace connecting local farmers with urban consumers. " +
                        "The goal is to reduce food miles and support local agriculture. " +
                        "Users can browse seasonal products, subscribe to weekly boxes, and track the origin of their food.")
                .tasks(Arrays.asList(
                        createTask("Implement Product Listing Page",
                                "Create a grid layout showing product cards with images, prices, and farmer info.",
                                TaskType.FEATURE, 5),
                        createTask("Shopping Cart Logic",
                                "Implement add/remove items, calculate totals, and persist cart state.",
                                TaskType.FEATURE, 7),
                        createTask("Fix Mobile Menu Glitch",
                                "The navigation menu overlaps with content on screens smaller than 375px.",
                                TaskType.BUG, 3),
                        createTask("User Authentication", "Secure login/register flow using JWT.", TaskType.FEATURE, 6),
                        createTask("Write API Documentation", "Document all product endpoints using Swagger.",
                                TaskType.CHORE, 4),
                        createTask("Farmer Profile Page",
                                "A page showing farmer's bio, location map, and their available products.",
                                TaskType.FEATURE, 5),
                        createTask("Optimize Image Loading",
                                "Implement lazy loading for product images to improve LCP score.", TaskType.CHORE, 6)))
                .build();
    }

    private ProjectBlueprint createFintechScenario() {
        return ProjectBlueprint.builder()
                .name("CoinTrack - Crypto Portfolio Tracker")
                .category("FinTech")
                .description("A dashboard for tracking cryptocurrency investments across multiple exchanges. " +
                        "Users can manually add transactions or connect via (simulated) APIs. " +
                        "Features include real-time price updates, P/L calculation, and visual charts.")
                .tasks(Arrays.asList(
                        createTask("Dashboard Layout",
                                "Create the main dashboard layout with summary cards and a sidebar.", TaskType.FEATURE,
                                4),
                        createTask("Real-time Price Integration",
                                "Fetch crypto prices from a public API (e.g. CoinGecko) every 60 seconds.",
                                TaskType.FEATURE, 8),
                        createTask("Transaction Form Validation",
                                "Ensure users cannot enter negative amounts or future dates.", TaskType.BUG, 3),
                        createTask("Portfolio Chart Component",
                                "Implement a line chart showing portfolio value over time using Recharts/Chart.js.",
                                TaskType.FEATURE, 7),
                        createTask("Database Migration", "Set up initial schema for Users, Assets, and Transactions.",
                                TaskType.CHORE, 5),
                        createTask("Dark Mode Toggle", "Implement theme switching between light and dark modes.",
                                TaskType.FEATURE, 4)))
                .build();
    }

    private ProjectBlueprint createSocialMediaScenario() {
        return ProjectBlueprint.builder()
                .name("BookWorm - Social Network for Readers")
                .category("Social")
                .description("A platform where book lovers can share reviews, create reading lists, and follow others. "
                        +
                        "Focus on rich text editing for reviews and a recommendation engine based on reading history.")
                .tasks(Arrays.asList(
                        createTask("Book Search Bar",
                                "Implement a search bar with debounce that queries the Google Books API.",
                                TaskType.FEATURE, 6),
                        createTask("Profile Page Redesign",
                                "Update the user profile design to match the new Figma mockups.", TaskType.FEATURE, 5),
                        createTask("Fix Comment Nesting", "Nested comments are rendering in the wrong order.",
                                TaskType.BUG, 7),
                        createTask("Review Editor", "Rich text editor (Bold, Italic, Link) for writing book reviews.",
                                TaskType.FEATURE, 6),
                        createTask("Reading List API", "Backend endpoint to add/remove books from 'Want to Read' list.",
                                TaskType.FEATURE, 4),
                        createTask("Unit Tests for Service Layer", "Increase test coverage for the UserService.",
                                TaskType.CHORE, 5)))
                .build();
    }

    private ProjectBlueprint.TaskBlueprint createTask(String title, String desc, TaskType type, Integer difficulty) {
        return ProjectBlueprint.TaskBlueprint.builder()
                .title(title)
                .description(desc)
                .type(type)
                .difficulty(difficulty)
                .build();
    }
}
