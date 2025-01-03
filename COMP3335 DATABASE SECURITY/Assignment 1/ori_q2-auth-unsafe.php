<?php
// Database connection
$conn = new mysqli('localhost', 'username', 'password', 'ecommerce_db');

// Retrieve user input (from login form)
$username = $_POST['username'];
$password = $_POST['password'];

// Use prepared statements to prevent SQL injection (see relevant lecture)
$query = "SELECT * FROM users WHERE username = ? AND password = ?";
$stmt = $conn->prepare($query);
$stmt->bind_param("ss", $username,password);
$stmt->execute();
$result = $stmt->get_result();

// Check if a user was found
if ($result->num_rows > 0) {
    // Fetch user data
    $user = $result->fetch_assoc();

    // Authentication successful
    echo "Welcome, " . htmlentities($user['username']) . "!";
} else {
    // Authentication failed
    echo "Invalid username or password.";
}

// Close connection
$stmt->close();
$conn->close();
