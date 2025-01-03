CREATE TABLE users ( 
    id INT AUTO_INCREMENT PRIMARY KEY, 
    username VARCHAR(50) NOT NULL UNIQUE, 
    /* password VARCHAR(255) NOT NULL, -- Storing plaintext passwords (unsafe) */
    hash_encrypted_password VARCHAR(255) NOT NULL, /* Convert plaintext passwords to hash codes stored */
    salt VARCHAR(255) NOT NULL,                    /* Unique salt for storing each passwords and added in converted hash passwords*/
    email VARCHAR(100) NOT NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);
