<?php
require 'config.php';
require 'functions.php';

$encryption_key = "superkey";

session_start();
if (!isset($_SESSION['account_name'])) {
    header("Location: login.php");
    exit;
}

echo "<h1>Welcome, " . htmlspecialchars($_SESSION['account_name']) . "!</h1>";
echo "<h2>Your role is: Patient</h2>";

$selected_function = isset($_POST['function']) ? $_POST['function'] : null;
$result = null;
$formHtml = '';

if ($selected_function) {
    try {
        $account_name = $_SESSION['account_name'];

        switch ($selected_function) {
            case 'view_info':
                try {
                    $query = $pdo->prepare("SELECT RealName, ContactInfo, DateOfBirth, InsuranceDetails, RealNameIV, ContactInfoIV FROM Patients WHERE account_name = ?");
                    $query->execute([$account_name]);
                    $user_info = $query->fetch(PDO::FETCH_ASSOC);

                    if ($user_info) {
                        $realName = decryptAES128($user_info['RealName'], $encryption_key, $user_info['RealNameIV']);
                        $contactInfo = decryptAES128($user_info['ContactInfo'], $encryption_key, $user_info['ContactInfoIV']);
                        $result = "<h3>Your Information:</h3>";
                        $result .= "<p>Account Name: " . htmlspecialchars($account_name) . "</p>";
                        $result .= "<p>Real Name: " . htmlspecialchars($realName) . "</p>";
                        $result .= "<p>Contact Info: " . htmlspecialchars($contactInfo) . "</p>";
                        $result .= "<p>Date of Birth: " . htmlspecialchars($user_info['DateOfBirth']) . "</p>";
                        $result .= "<p>Insurance Details: " . htmlspecialchars($user_info['InsuranceDetails']) . "</p>";
                    } else {
                        $result = "No information found for this account.";
                    }
                } catch (PDOException $e) {
                    $result = "Error retrieving information: " . htmlspecialchars($e->getMessage());
                } catch (Exception $e) {
                    $result = "Error decrypting data: " . htmlspecialchars($e->getMessage());
                }
                break;


            case 'view_Catalogs':
                $query = $pdo->prepare("SELECT * FROM TestsCatalog");
                $query->execute();
                $testCatalogs = $query->fetchAll(PDO::FETCH_ASSOC);

                if ($testCatalogs) {
                    $result = "<h3>Test Catalogs:</h3><table border='1'><tr><th>Test Code</th><th>Test Name</th><th>Description</th><th>Cost</th></tr>";
                    foreach ($testCatalogs as $catalog) {
                        $result .= "<tr>";
                        $result .= "<td>" . htmlspecialchars($catalog['TestCode']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($catalog['TestName']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($catalog['Description']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($catalog['Cost']) . "</td>";
                        $result .= "</tr>";
                    }
                    $result .= "</table>";
                } else {
                    $result = "No test catalogs found.";
                }
                break;


            case 'view_billing':
                $account_name = $_SESSION['account_name'];

                $query = $pdo->prepare("SELECT PatientID FROM Patients WHERE account_name = ?");
                $query->execute([$account_name]);
                $patient_info = $query->fetch(PDO::FETCH_ASSOC);

                if ($patient_info) {
                    $patient_id = $patient_info['PatientID'];

                    $query = $pdo->prepare("SELECT OrderID FROM Orders WHERE PatientID = ?");
                    $query->execute([$patient_id]);
                    $order_info = $query->fetch(PDO::FETCH_ASSOC);

                    if ($order_info) {
                        $order_id = $order_info['OrderID'];

                        $query = $pdo->prepare("SELECT * FROM Billing WHERE OrderID = ?");
                        $query->execute([$order_id]);
                        $billing_info = $query->fetch(PDO::FETCH_ASSOC);

                        if ($billing_info) {

                            $result = "<h3>Billing Information:</h3><table border='1'><tr><th>Order ID</th><th>Billed Amount</th><th>Payment Status</th><th>Insurance Claim Status</th></tr>";
                            $result .= "<tr>";
                            $result .= "<td>" . htmlspecialchars($billing_info['OrderID']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($billing_info['BilledAmount']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($billing_info['PaymentStatus']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($billing_info['InsuranceClaimStatus']) . "</td>";
                            $result .= "</tr>";
                            $result .= "</table>";
                        } else {
                            $result = "No billing information found for this Order ID.";
                        }
                    } else {
                        $result = "No orders found for this Patient ID.";
                    }
                } else {
                    $result = "No patient information found for this account.";
                }
                break;


            case 'view_orders':
                $query = $pdo->prepare("SELECT PatientID FROM Patients WHERE account_name = ?");
                $query->execute([$account_name]);
                $patient_info = $query->fetch(PDO::FETCH_ASSOC);

                if ($patient_info) {
                    $patient_id = $patient_info['PatientID'];

                    $query = $pdo->prepare("SELECT * FROM Orders WHERE PatientID = ?");
                    $query->execute([$patient_id]);
                    $orders = $query->fetchAll(PDO::FETCH_ASSOC);

                    if (!empty($orders)) {
                        $result = "<h3>Your Orders:</h3><table border='1'><tr><th>Order ID</th><th>Test Code</th><th>Ordering Physician</th><th>Order Date</th><th>Status</th></tr>";
                        foreach ($orders as $order) {
                            $result .= "<tr>";
                            $result .= "<td>" . htmlspecialchars($order['OrderID']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($order['TestCode']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($order['OrderingPhysician']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($order['OrderDate']) . "</td>";
                            $result .= "<td>" . htmlspecialchars($order['Status']) . "</td>";
                            $result .= "</tr>";
                        }
                        $result .= "</table>";
                    } else {
                        $result = "No orders found for this Patient ID.";
                    }
                } else {
                    $result = "No patient information found for this account.";
                }
                break;

            case 'view_results':

                $account_name = $_SESSION['account_name'];


                $query = $pdo->prepare("SELECT PatientID FROM Patients WHERE account_name = ?");
                $query->execute([$account_name]);
                $patient_info = $query->fetch(PDO::FETCH_ASSOC);

                if ($patient_info) {
                    $patient_id = $patient_info['PatientID'];


                    $query = $pdo->prepare("SELECT OrderID FROM Orders WHERE PatientID = ?");
                    $query->execute([$patient_id]);
                    $order_info = $query->fetchAll(PDO::FETCH_ASSOC);

                    if ($order_info) {

                        $result = "<h3>Your Test Results:</h3><table border='1'><tr><th>Order ID</th><th>Report URL</th><th>Interpretation</th><th>Reporting Pathologist</th></tr>";
                        foreach ($order_info as $order) {
                            $order_id = $order['OrderID'];


                            $query = $pdo->prepare("SELECT * FROM Results WHERE OrderID = ?");
                            $query->execute([$order_id]);
                            $results = $query->fetchAll(PDO::FETCH_ASSOC);


                            if ($results) {
                                foreach ($results as $result_info) {
                                    $result .= "<tr>";
                                    $result .= "<td>" . htmlspecialchars($result_info['OrderID']) . "</td>";
                                    $result .= "<td>" . htmlspecialchars($result_info['ReportURL']) . "</td>";
                                    $result .= "<td>" . htmlspecialchars($result_info['Interpretation']) . "</td>";
                                    $result .= "<td>" . htmlspecialchars($result_info['ReportingPathologist']) . "</td>";
                                    $result .= "</tr>";
                                }
                            }
                        }
                        $result .= "</table>";
                    } else {
                        $result = "No orders found for this Patient ID.";
                    }
                } else {
                    $result = "No patient information found for this account.";
                }
                break;

            case 'update_info':

                if (isset($_POST['submit_update'])) {

                    $newRealName = $_POST['real_name'];
                    $newContactInfo = $_POST['contact_info'];
                    $newPassword = $_POST['password'];

                    $encryption_key = "superkey"; // key for AES encryption

                    if (empty($newPassword)) {
                        $result = "New password is required. Please enter a valid password.";
                        break;
                    }

                    $encryptedRealName = encryptAES128($newRealName, $encryption_key);
                    $encryptedContactInfo = encryptAES128($newContactInfo, $encryption_key);

                    $salt = bin2hex(random_bytes(32));
                    $hashedPassword = generateHash($newPassword, $salt);

                    $account_name = $_SESSION['account_name'];

                    try {
                        // Prepare SQL to update user info
                        $query = $pdo->prepare("
                                            UPDATE Patients 
                                            SET 
                                                RealName = ?, 
                                                ContactInfo = ?, 
                                                RealNameIV = ?, 
                                                ContactInfoIV = ?, 
                                                PasswordHash = ?, 
                                                Salt = ?
                                            WHERE account_name = ?
                                        ");
                        $query->execute([
                            $encryptedRealName['encryptedData'],
                            $encryptedContactInfo['encryptedData'],
                            $encryptedRealName['iv'],
                            $encryptedContactInfo['iv'],
                            $hashedPassword,
                            $salt,
                            $account_name
                        ]);

                        $result = "Your information has been successfully updated.";
                    } catch (PDOException $e) {
                        $result = "Error updating information: " . htmlspecialchars($e->getMessage());
                    } catch (Exception $e) {
                        $result = "Error encrypting data: " . htmlspecialchars($e->getMessage());
                    }
                } else {
                    $formHtml = '
                                        <form method="post">
                                            <h3>Update Your Information</h3>
                                            <label for="real_name">New Real Name:</label>
                                            <input type="text" name="real_name" id="real_name" required><br>
                                            
                                            <label for="contact_info">New Contact Info:</label>
                                            <input type="text" name="contact_info" id="contact_info" required><br>
                            
                                            <label for="password">New Password (Input is Required. You can input your current password if no change is needed.):</label>
                                            <input type="password" name="password" id="password" required><br>
                                    
                                            <input type="hidden" name="function" value="update_info">
                                            <button type="submit" name="submit_update">Submit Update</button>
                                        </form>';
                }
                break;

            default:
                $result = "Invalid function selected.";
                break;
        }
    } catch (PDOException $e) {
        $result = "Database error: " . htmlspecialchars($e->getMessage());
    } catch (Exception $e) {
        $result = "Error: " . htmlspecialchars($e->getMessage());
    }
}
?>

<h1>Select a Function to Operate</h1>
<form method="post">
    <label for="function">Choose a function:</label>
    <select name="function" id="function" required onchange="toggleUpdateFields()">
        <option value="">--Select a Function--</option>
        <option value="view_info">View My Information</option>
        <option value="view_Catalogs">View All Test Catalogs</option>
        <option value="view_billing">View My Billing</option>
        <option value="view_results">View My Results</option>
        <option value="view_orders">View My Orders</option>
        <option value="update_info">Update My Information</option>
    </select>
    <button type="submit">Submit</button>
</form>

<?php
if ($formHtml) {
    echo $formHtml;
}

if ($result) {
    echo "<h2>Result:</h2><p>$result</p>";
}
?>

<form action="index.php" method="get">
    <button type="submit">Return to Main Page</button>
</form>