<?php
// Database connection
$conn = new mysqli('localhost', 'username', 'password', 'ecommerce_db');

// Retrieve user input (from login form)
$username = $_POST['username'];
$password = $_POST['password'];

// Use prepared statements to prevent SQL injection (see relevant lecture)
$query = "SELECT * FROM users WHERE username = ?";                      // Changed line
$stmt = $conn->prepare($query);
$stmt->bind_param("ss", $username);                        // Changed line
$stmt->execute();
$result = $stmt->get_result();

// Check if a user was found
if ($result->num_rows > 0) {
    // Fetch user data
    $user = $result->fetch_assoc();
    
    $stmt->result($hash_encrypted_password, $salt);                     // Added line
    $stmt->fetch();                                                     // Added line
    $input_password = hash('sha256', $salt . $password);    // Added line

    if ($input_password === $hash_encrypted_password) {                 // Added line
        // Authentication successful                     
        echo "Welcome, " . htmlentities($username) . "!"; 
    } else {
        // Authentication failed                                        // Added line
        echo "Invalid username or password.";                           // Added line
    }
} else {
    // Authentication failed
    echo "Invalid username or password.";
}

// Close connection
$stmt->close();
$conn->close();
