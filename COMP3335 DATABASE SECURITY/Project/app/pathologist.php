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
echo "<h2>Your role is: Pathologist </h2>";

$selected_function = isset($_POST['function']) ? $_POST['function'] : null;
$result = null;
$formHtml = '';

if ($selected_function) {
    switch ($selected_function) {
        case 'view_info':
            try {
                $account_name = $_SESSION['account_name'];
                $query = $pdo->prepare("SELECT RealName, ContactInfo, Role, PasswordHash, RealNameIV, ContactInfoIV FROM Staff WHERE account_name = ?");
                $query->execute([$account_name]);
                $user_info = $query->fetch(PDO::FETCH_ASSOC);

                if ($user_info) {
                    $realName = decryptAES128($user_info['RealName'], $encryption_key, $user_info['RealNameIV']);
                    $contactInfo = decryptAES128($user_info['ContactInfo'], $encryption_key, $user_info['ContactInfoIV']);
                    $result = "<h3>User Information:</h3>";
                    $result .= "<p>Account Name: " . htmlspecialchars($account_name) . "</p>";
                    $result .= "<p>Role: " . htmlspecialchars($user_info['Role']) . "</p>";
                    $result .= "<p>Real Name: " . htmlspecialchars($realName) . "</p>";
                    $result .= "<p>Contact Info: " . htmlspecialchars($contactInfo) . "</p>";
                    $result .= "<p>Hashed Password: [Hidden for security]</p>";
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
            try {
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
            } catch (PDOException $e) {
                $result = "Error retrieving test catalogs: " . htmlspecialchars($e->getMessage());
            }
            break;

        case 'print_Reports':
            try {
                $query = $pdo->prepare("SELECT * FROM Results");
                $query->execute();
                $Results = $query->fetchAll(PDO::FETCH_ASSOC);

                if ($Results) {
                    $result = "<h3>Result Reports:</h3><table border='2'><tr><th>ResultID</th><th>OrderID</th><th>ReportURL</th><th>Interpretation</th><th>ReportingPathologist</th></tr>";
                    foreach ($Results as $report) {
                        $result .= "<tr>";
                        $result .= "<td>" . htmlspecialchars($report['ResultID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['OrderID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['ReportURL']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['Interpretation']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['ReportingPathologist']) . "</td>";
                        $result .= "</tr>";
                    }
                    $result .= "</table>";
                } else {
                    $result = "No result reports found.";
                }
            } catch (PDOException $e) {
                $result = "Error retrieving result reports: " . htmlspecialchars($e->getMessage());
            }
            break;

        case 'create_results':
            if (isset($_POST['submit_result'])) {
                $order_id = $_POST['order_id'];
                $interpretation = $_POST['interpretation'];
                $report_url = $_POST['report_url'];

                if (empty($order_id) || empty($interpretation)) {
                    $result = "Please fill in all required fields (Order ID and Interpretation).";
                    break;
                }

                try {
                    $query = $pdo->prepare("SELECT COUNT(*) FROM Orders WHERE OrderID = ?");
                    $query->execute([$order_id]);
                    $orderExists = $query->fetchColumn();

                    if (!$orderExists) {
                        $result = "OrderID does not exist. Please enter a valid Order ID.";
                        break;
                    }


                    $reportingPathologist = $_SESSION['account_name'];

                    $query = $pdo->prepare("INSERT INTO Results (OrderID, ReportURL, Interpretation, ReportingPathologist) VALUES (?, ?, ?, ?)");
                    $query->execute([$order_id, $report_url, $interpretation, $reportingPathologist]);
                    $result = "New result has been created successfully!";
                } catch (PDOException $e) {
                    $result = "Error creating result: " . htmlspecialchars($e->getMessage());
                }
            } else {

                $formHtml = '
                    <form method="post">
                        <h3>Create a New Result</h3>
                        <label for="order_id">Order ID:</label>
                        <input type="text" name="order_id" id="order_id" required><br>
                        <label for="report_url">Report URL (optional):</label>
                        <input type="text" name="report_url" id="report_url"><br>
                        <label for="interpretation">Interpretation:</label>
                        <input type="text" name="interpretation" id="interpretation" required><br>
                        <input type="hidden" name="function" value="create_results">
                        <button type="submit" name="submit_result">Submit Result</button>
                    </form>';
            }
            break;

        case 'create_catalog':
            if (isset($_POST['submit_catalog'])) {
                try {
                    $testCode = $_POST['testCode'];
                    $testName = $_POST['testName'];
                    $description = $_POST['description'];
                    $cost = $_POST['cost'];

                    $query = $pdo->prepare("INSERT INTO TestsCatalog (TestCode, TestName, Description, Cost) VALUES (?, ?, ?, ?)");
                    $query->execute([$testCode, $testName, $description, $cost]);
                    $result = "New test catalog entry created successfully!";
                } catch (PDOException $e) {
                    $result = "Error creating test catalog: " . htmlspecialchars($e->getMessage());
                }
            } else {
                $formHtml = '
                    <form method="post">
                        <h3>Create a New Test Catalog</h3>
                        <label for="testCode">Test Code:</label>
                        <input type="text" name="testCode" id="testCode" required><br>
                        <label for="testName">Test Name:</label>
                        <input type="text" name="testName" id="testName" required><br>
                        <label for="description">Description:</label>
                        <textarea name="description" id="description" required></textarea><br>
                        <label for="cost">Cost:</label>
                        <input type="number" name="cost" id="cost" required><br>
                        <input type="hidden" name="function" value="create_catalog">
                        <button type="submit" name="submit_catalog">Submit Catalog</button>
                    </form>';
            }
            break;

        default:
            $result = "Invalid function selected.";
            break;

        case 'update_info':
            // Check if form data is submitted
            if (isset($_POST['submit_update'])) {
                $newRealName = $_POST['real_name'];
                $newContactInfo = $_POST['contact_info'];
                $newPassword = $_POST['password'];

                $encryption_key = "superkey"; // 16-byte key for AES encryption

                // Ensure password is not empty
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
                            UPDATE Staff 
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
                            <input type="password" name="password" id="password"><br>
            
                            <input type="hidden" name="function" value="update_info">
                            <button type="submit" name="submit_update">Submit Update</button>
                        </form>';
            }
            break;
    }
}
?>

<h1>Select a Function to Operate</h1>
<form method="post">
    <label for="function">Choose a function:</label>
    <select name="function" id="function" required onchange="toggleFields()">
        <option value="">--Select a Function--</option>
        <option value="view_info">View My Information</option>
        <option value="view_Catalogs">View All Test Catalogs</option>
        <option value="print_Reports">Print out Test reports</option>
        <option value="create_results">Create New Results</option>
        <option value="create_catalog">Create New Test Catalog</option>
        <option value="update_info">Update My Information</option>
    </select>
    <button type="submit">Submit</button>
</form>

<form action="index.php" method="get">
    <button type="submit">Return to Main Page</button>
</form>

<?php
if ($formHtml) {
    echo $formHtml;
}

if ($result) {
    echo "<h2>Result:</h2><p>$result</p>";
}
?>