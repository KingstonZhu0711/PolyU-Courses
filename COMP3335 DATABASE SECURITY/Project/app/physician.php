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
echo "<h2>Your role is: Physician </h2>";

$selected_function = isset($_POST['function']) ? $_POST['function'] : null;
$result = null;
$formHtml = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST' && $selected_function) {
    switch ($selected_function) {
        case 'view_info':
            try {
                $account_name = $_SESSION['account_name'];
                $query = $pdo->prepare("SELECT RealName, ContactInfo, Role, RealNameIV, ContactInfoIV FROM Staff WHERE account_name = ?");
                $query->execute([$account_name]);
                $user_info = $query->fetch(PDO::FETCH_ASSOC);

                if ($user_info) {
                    $realName = decryptAES128(
                        $user_info['RealName'],
                        $encryption_key,
                        $user_info['RealNameIV']
                    );

                    $contactInfo = decryptAES128(
                        $user_info['ContactInfo'],
                        $encryption_key,
                        $user_info['ContactInfoIV']
                    );

                    $result = "<h3>User Information:</h3>";
                    $result .= "<p>Account Name: " . htmlspecialchars($account_name) . "</p>";
                    $result .= "<p>Role: " . htmlspecialchars($user_info['Role']) . "</p>";
                    $result .= "<p>Real Name: " . htmlspecialchars($realName) . "</p>";
                    $result .= "<p>Contact Info: " . htmlspecialchars($contactInfo) . "</p>";
                } else {
                    $result = "No information found for this account.";
                }
            } catch (PDOException $e) {
                $result = "Error retrieving information: " . htmlspecialchars($e->getMessage());
            } catch (Exception $e) {
                $result = "Error decrypting data: " . htmlspecialchars($e->getMessage());
            }
            break;

        case 'update_info':

            if (isset($_POST['submit_update'])) {
                $newRealName = $_POST['real_name'];
                $newContactInfo = $_POST['contact_info'];
                $newPassword = $_POST['password'];

                $encryption_key = "superkey"; // key for AES encryption

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

        case 'view_Orders':
            try {
                $query = $pdo->prepare("SELECT * FROM Orders");
                $query->execute();
                $orders = $query->fetchAll(PDO::FETCH_ASSOC);

                if ($orders) {
                    $result = "<h3>Orders:</h3><table border='1'><tr><th>Order ID</th><th>PatientID</th><th>TestCode</th><th>OrderingPhysician</th><th>OrderDate</th><th>Status</th></tr>";
                    foreach ($orders as $order) {
                        $result .= "<tr>";
                        $result .= "<td>" . htmlspecialchars($order['OrderID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($order['PatientID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($order['TestCode']) . "</td>";
                        $result .= "<td>" . htmlspecialchars((string) $order['OrderingPhysician']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($order['OrderDate']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($order['Status']) . "</td>";
                        $result .= "</tr>";
                    }
                    $result .= "</table>";
                } else {
                    $result = "No orders found.";
                }
            } catch (PDOException $e) {
                $result = "Error retrieving orders: " . htmlspecialchars($e->getMessage());
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

        case 'create_Orders':
            if (isset($_POST['submit_order'])) {
                $patientID = $_POST['patientID'];
                $testCode = $_POST['testCode'];
                $status = $_POST['status'];

                $validStatuses = ['Pending', 'Completed', 'Canceled'];
                if (!in_array($status, $validStatuses)) {
                    $result = "Invalid status selected. Please choose from 'Pending', 'Completed', or 'Canceled'.";
                    break;
                }

                // Check if PatientID and TestCode exist in the database
                try {
                    $query = $pdo->prepare("SELECT COUNT(*) FROM Patients WHERE PatientID = ?");
                    $query->execute([$patientID]);
                    $patientExists = $query->fetchColumn();

                    $query = $pdo->prepare("SELECT COUNT(*) FROM TestsCatalog WHERE TestCode = ?");
                    $query->execute([$testCode]);
                    $testExists = $query->fetchColumn();

                    if (!$patientExists) {
                        $result = "PatientID does not exist. Please enter a valid Patient ID.";
                        break;
                    }

                    if (!$testExists) {
                        $result = "TestCode does not exist. Please enter a valid Test Code.";
                        break;
                    }

                    $orderingPhysician = $_SESSION['account_name'];
                    $orderDate = date('Y-m-d H:i:s');

                    $query = $pdo->prepare("INSERT INTO Orders (PatientID, TestCode, OrderingPhysician, OrderDate, Status) VALUES (?, ?, ?, ?, ?)");
                    $query->execute([$patientID, $testCode, $orderingPhysician, $orderDate, $status]);
                    $result = "New order created successfully!";
                } catch (PDOException $e) {
                    $result = "Error creating order: " . htmlspecialchars($e->getMessage());
                }
            } else {
                $formHtml = '
                                <form method="post">
                                    <h3>Create a New Order</h3>
                                    <label for="patientID">Patient ID:</label>
                                    <input type="text" name="patientID" id="patientID" required><br>
                                    <label for="testCode">Test Code:</label>
                                    <input type="text" name="testCode" id="testCode" required><br>
                                    <label for="status">Status:</label>
                                    <select name="status" id="status" required>
                                        <option value="">--Select Status--</option>
                                        <option value="Pending">Pending</option>
                                        <option value="Completed">Completed</option>
                                        <option value="Canceled">Canceled</option>
                                    </select><br>
                                    <input type="hidden" name="function" value="create_Orders">
                                    <button type="submit" name="submit_order">Submit Order</button>
                                </form>';
            }
            break;


        case 'create_Catalog':
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
                        <input type="hidden" name="function" value="create_Catalog">
                        <button type="submit" name="submit_catalog">Submit Catalog</button>
                    </form>';
            }
            break;



        default:
            $result = "Invalid function selected.";
            break;
    }
}
?>

<h1>Select a Function to Operate</h1>
<form method="post">
    <label for="function">Choose a function:</label>
    <select name="function" id="function" required>
        <option value="">--Select a Function--</option>
        <option value="view_info">View my information</option>
        <option value="create_Orders">Create New Orders</option>
        <option value="create_Catalog">Create New Test Catalogs</option>
        <option value="update_info">Change/Update my current information</option>
        <option value="view_Orders">View existing orders</option>
        <option value="view_Catalogs">View existing test catalogs</option>
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