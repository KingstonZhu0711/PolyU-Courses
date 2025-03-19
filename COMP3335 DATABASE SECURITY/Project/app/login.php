<?php
require 'config.php';
require 'functions.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);

session_start();

$invalid_credentials = false;
$error_message = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $role = $_POST['role'];
    $account_name = $_POST['account_name'];
    $password = $_POST['password'];

    try {
        // Always query the Staff table for both lab_staff and secretary roles
        if ($role === 'patient') {
            $query = $pdo->prepare("SELECT PasswordHash, Salt FROM Patients WHERE account_name = ?");
        } else {
            $query = $pdo->prepare("SELECT PasswordHash, Salt, Role FROM Staff WHERE account_name = ?");
        }

        $query->execute([$account_name]);
        $user = $query->fetch(PDO::FETCH_ASSOC);

        if ($user && generateHash($password, $user['Salt']) === $user['PasswordHash']) {
            $_SESSION['account_name'] = $account_name;
            $_SESSION['role'] = $role;

            if ($role === 'patient') {
                header("Location: patient.php");
                exit;
            } else {
                // Check actual role from database
                $actual_role = $user['Role'];
                
                if ($role === 'lab_staff' && ($actual_role === 'Physician' || $actual_role === 'Pathologist')) {
                    if ($actual_role === 'Physician') {
                        header("Location: physician.php");
                    } else {
                        header("Location: pathologist.php");
                    }
                    exit;
                } elseif ($role === 'secretary' && $actual_role === 'Secretary') {
                    header("Location: secretary.php");
                    exit;
                } else {
                    $invalid_credentials = true;
                    $error_message = "Invalid role selection for this account.";
                }
            }
        } else {
            $invalid_credentials = true;
            $error_message = "Invalid account name or password.";
        }
    } catch (PDOException $e) {
        $invalid_credentials = true;
        $error_message = "Database error: " . $e->getMessage();
    }
}
?>

<h1>Login</h1>
<?php if ($invalid_credentials): ?>
    <h2 style="color: red;"><?php echo htmlspecialchars($error_message); ?></h2>
<?php endif; ?>

<form method="post">
    <label>Role:</label>
    <select name="role" required>
        <option value="patient">Patient</option>
        <option value="lab_staff">Lab Staff</option>
        <option value="secretary">Secretary</option>
    </select><br>
    <label>Account Name:</label>
    <input type="text" name="account_name" required><br>
    <label>Password:</label>
    <input type="password" name="password" required><br>
    <button type="submit">Login</button>
</form>

<form action="index.php" method="get">
    <button type="submit">Return to Main Page</button>
</form>