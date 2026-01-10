<?php
// Database configuration (update with your credentials)
$host = 'localhost';
$dbname = 'erp_inventory'; // Target database name
$username = 'root';       // Your DB username
$password = '';           // Your DB password

try {
    // 1. Connect to MySQL server (without specifying a database yet)
    $pdo = new PDO("mysql:host=$host;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // 2. Check if the database exists; create it if not
    $stmt = $pdo->query("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$dbname'");
    if ($stmt->rowCount() === 0) {
        // Create the database
        $pdo->exec("CREATE DATABASE $dbname");
        echo "Database '$dbname' created successfully.<br>";
    }

    // 3. Connect to the target database
    $pdo->exec("USE $dbname");

    // 4. Check if the "users" table exists; create it if not
    $stmt = $pdo->query("SHOW TABLES LIKE 'users'");
    if ($stmt->rowCount() === 0) {
        // SQL to create the users table
        $createTableSQL = "
        CREATE TABLE users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) NOT NULL UNIQUE,
            email VARCHAR(100) NOT NULL UNIQUE,
            password VARCHAR(255) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
        ";
        $pdo->exec($createTableSQL);
        echo "Table 'users' created successfully.<br>";
    }

    echo "Database setup completed.<br>";

} catch(PDOException $e) {
    die("Database setup failed: " . $e->getMessage());
}

// Close connection
$pdo = null;
?>