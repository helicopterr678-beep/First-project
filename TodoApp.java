import java.io.*;
import java.util.*;

/**
 * A simple To-Do List application with local file storage.
 * Allows users to add, view, mark complete, and delete tasks.
 */
public class TodoApp {
    private List<Task> tasks;
    private String filename = "tasks.txt";

    public TodoApp() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    /**
     * Task class represents a single to-do item
     */
    public static class Task {
        private String description;
        private boolean completed;

        public Task(String description) {
            this.description = description;
            this.completed = false;
        }

        public Task(String description, boolean completed) {
            this.description = description;
            this.completed = completed;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        @Override
        public String toString() {
            return (completed ? "[✓] " : "[ ] ") + description;
        }

        public String toFileFormat() {
            return (completed ? "1" : "0") + "|" + description;
        }

        public static Task fromFileFormat(String line) {
            String[] parts = line.split("\\|", 2);
            boolean completed = parts[0].equals("1");
            String description = parts[1];
            return new Task(description, completed);
        }
    }

    /**
     * Display the main menu
     */
    public void displayMenu() {
        System.out.println("\n========== TO-DO LIST APP ==========");
        System.out.println("1. View all tasks");
        System.out.println("2. Add a new task");
        System.out.println("3. Mark task as complete");
        System.out.println("4. Delete a task");
        System.out.println("5. Exit");
        System.out.print("Choose an option (1-5): ");
    }

    /**
     * View all tasks with numbering
     */
    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\n📋 No tasks yet! Add one to get started.");
            return;
        }

        System.out.println("\n========== YOUR TASKS ==========");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("Total: " + tasks.size() + " tasks");
    }

    /**
     * Add a new task
     */
    public void addTask(Scanner scanner) {
        System.out.print("\nEnter task description: ");
        String description = scanner.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("❌ Task cannot be empty!");
            return;
        }

        tasks.add(new Task(description));
        System.out.println("✅ Task added successfully!");
        saveTasks();
    }

    /**
     * Mark a task as complete
     */
    public void markTaskComplete(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("\n📋 No tasks to mark complete!");
            return;
        }

        viewTasks();
        System.out.print("\nEnter task number to mark complete: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (index < 0 || index >= tasks.size()) {
                System.out.println("❌ Invalid task number!");
                return;
            }

            Task task = tasks.get(index);
            if (task.isCompleted()) {
                System.out.println("⚠️  This task is already marked complete!");
            } else {
                task.setCompleted(true);
                System.out.println("✅ Task marked as complete!");
                saveTasks();
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number!");
        }
    }

    /**
     * Delete a task
     */
    public void deleteTask(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("\n📋 No tasks to delete!");
            return;
        }

        viewTasks();
        System.out.print("\nEnter task number to delete: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (index < 0 || index >= tasks.size()) {
                System.out.println("❌ Invalid task number!");
                return;
            }

            String removedTask = tasks.get(index).getDescription();
            tasks.remove(index);
            System.out.println("✅ Task '" + removedTask + "' deleted!");
            saveTasks();
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number!");
        }
    }

    /**
     * Save tasks to local file
     */
    public void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Task task : tasks) {
                writer.println(task.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Load tasks from local file
     */
    public void loadTasks() {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    tasks.add(Task.fromFileFormat(line));
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Main application loop
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("🎉 Welcome to the To-Do List Application!");

        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewTasks();
                    break;
                case "2":
                    addTask(scanner);
                    break;
                case "3":
                    markTaskComplete(scanner);
                    break;
                case "4":
                    deleteTask(scanner);
                    break;
                case "5":
                    System.out.println("\n👋 Goodbye! Your tasks have been saved.");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please choose 1-5.");
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        app.run();
    }
}
