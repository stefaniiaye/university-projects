# Pacman Java Project

## Main Java Features Used:

**1. Threads:** The game loop and menu loop utilize Java threads to manage concurrent execution.

**2. Swing GUI:** The graphical user interface (GUI) of the game is built using Swing, a Java GUI toolkit.

**3. Serialization:** High scores are saved and loaded using Java serialization, allowing data persistence between sessions.

**4. Event Handling:** Keyboard input for controlling Pacman is managed using Java's event handling mechanism.

**5. Collections:** Java collections (e.g., ArrayList, DefaultListModel) are used for managing high scores and other data structures.

## MVC (Model-View-Controller) Pattern:

This project follows the MVC architectural pattern for better code organization and separation of concerns:

- **Model:** The game logic and state are encapsulated within the `Game` class. It manages the game board, score, lives, and other game-related functionalities.

- **View:** The graphical representation of the game, including the main menu and high scores display, is handled by Swing GUI components. The `Menu` class serves as the view component.

- **Controller:** User input and interaction are controlled by the `PacmanController` class, which listens for keyboard events and translates them into actions within the game.

