<?php
require 'config.php';
require 'functions.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $role = $_POST['role'];
    $account_name = $_POST['account_name'];
    $password = $_POST['password'];
    $real_name = $_POST['real_name'];
    $contact_info = $_POST['contact_info'];


    $encryption_key = "superkey"; 

    try {
        $query = $pdo->prepare("
            SELECT COUNT(*) 
            FROM (
                SELECT account_name FROM Patients
                UNION
                SELECT account_name FROM Staff
            ) AS all_accounts
            WHERE account_name = ?
        ");
        $query->execute([$account_name]);
        $count = $query->fetchColumn();

        if ($count > 0) {
            $error_message = "Error: Account name already exists. Please choose a different account name.";
            echo "<script>
                    alert('$error_message');
                    window.location.href = 'register.php';
                  </script>";
            exit;
        }
    } catch (PDOException $e) {
        $error_message = "Error: " . $e->getMessage();
        echo "<script>
                alert('$error_message');
                window.location.href = 'register.php';
              </script>";
        exit;
    }

    $encryptedRealName = encryptAES128($real_name, $encryption_key);
    $encryptedContactInfo = encryptAES128($contact_info, $encryption_key);

    $salt = bin2hex(random_bytes(32));
    $hashedPassword = generateHash($password, $salt);

    try {
        if ($role === 'Patient') {
            $date_of_birth = $_POST['date_of_birth'] ?? null;
            $insurance_details = $_POST['insurance_details'] ?? null;

            if (!$date_of_birth || !$insurance_details) {
                echo "Error: Date of Birth and Insurance Details are required for patients.";
                exit;
            }

            $query = $pdo->prepare("
                INSERT INTO Patients (
                    RealName, ContactInfo, RealNameIV, ContactInfoIV, 
                    account_name, PasswordHash, Salt, DateOfBirth, InsuranceDetails
                ) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            ");
            $query->execute([
                $encryptedRealName['encryptedData'], 
                $encryptedContactInfo['encryptedData'], 
                $encryptedRealName['iv'], 
                $encryptedContactInfo['iv'], 
                $account_name, 
                $hashedPassword, 
                $salt, 
                $date_of_birth,
                $insurance_details
            ]);
        } elseif ($role === 'Lab staff' || $role === 'Secretary') {
            $roleType = $role === 'Lab staff' ? $_POST['role_type'] : 'Secretary';

            $validRoles = ['Physician', 'Pathologist'];
            if ($role === 'Lab staff' && !in_array($roleType, $validRoles)) {
                echo "Error: Invalid role type for Lab Staff. Please select 'Physician' or 'Pathologist'.";
                exit;
            }

            $query = $pdo->prepare("
                INSERT INTO Staff (
                    RealName, ContactInfo, RealNameIV, ContactInfoIV, 
                    account_name, PasswordHash, Salt, Role
                ) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ");
            $query->execute([
                $encryptedRealName['encryptedData'], 
                $encryptedContactInfo['encryptedData'], 
                $encryptedRealName['iv'], 
                $encryptedContactInfo['iv'], 
                $account_name, 
                $hashedPassword, 
                $salt, 
                $roleType
            ]);
        }
        echo "<script>
                alert('Registration successful! Press OK to continue Login In');
                window.location.href = 'login.php';
              </script>";
    } catch (PDOException $e) {
        $error_message = "Error: " . $e->getMessage();
        echo "<script>
                alert('$error_message');
                window.location.href = 'register.php';
              </script>";
    }
    exit;
}
?>

<h1>Register</h1>
<form method="post">
    <label>Role:</label>
    <select name="role" id="role" required onchange="toggleFields()">
        <option value="Patient">Patient</option>
        <option value="Lab staff">Lab Staff</option>
        <option value="Secretary">Secretary</option>
    </select><br>
    
    <label>Account Name:</label>
    <input type="text" name="account_name" required><br>
    <label>Password:</label>
    <input type="password" name="password" required><br>
    <label>Real Name:</label>
    <input type="text" name="real_name" required><br>
    <label>Contact Info:</label>
    <input type="text" name="contact_info" required><br>

    <div id="patient-fields">
        <label>Date of Birth:</label>
        <input type="date" name="date_of_birth"><br>
        <label>Insurance Details:</label>
        <input type="text" name="insurance_details"><br>
    </div>

    <div id="staff-fields" style="display:none;">
        <label>If Lab Staff, Role:</label>
        <select name="role_type">
            <option value="Physician">Physician</option>
            <option value="Pathologist">Pathologist</option>
        </select><br>
    </div>

    <button type="submit">Register</button>
</form>

<form action="index.php" method="get">
    <button type="submit">Return to Main Page</button>
</form>

<script>
function toggleFields() {
    const role = document.getElementById('role').value;
    document.getElementById('patient-fields').style.display = role === 'Patient' ? 'block' : 'none';
    document.getElementById('staff-fields').style.display = (role === 'Lab staff' || role === 'Secretary') ? 'block' : 'none';
}
</script>