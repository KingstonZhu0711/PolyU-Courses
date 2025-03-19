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
echo "<h2>Your role is: Secretary </h2>";

$selected_function = isset($_POST['function']) ? $_POST['function'] : null;
$result = null;
$formHtml = '';

if ($selected_function) {
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
                    )
                    ;

                    $contactInfo = decryptAES128(
                        $user_info['ContactInfo'],
                        $encryption_key,
                        $user_info['ContactInfoIV']
                    )
                    ;

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
        case 'view_Appointments':
            try {
                $query = $pdo->prepare("SELECT * FROM Appointments");
                $query->execute();
                $appointments = $query->fetchAll(PDO::FETCH_ASSOC);

                if ($appointments) {
                    $result = "<h3>Appointments:</h3><table border='2'><tr><th>AppointmentID</th><th>PatientID</th><th>Appointment Date</th></tr>";
                    foreach ($appointments as $report) {
                        $result .= "<tr>";
                        $result .= "<td>" . htmlspecialchars($report['AppointmentID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['PatientID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['AppointmentDate']) . "</td>";
                        $result .= "</tr>";
                    }
                    $result .= "</table>";
                } else {
                    $result = "No appointments found.";
                }
            } catch (PDOException $e) {
                $result = "Error retrieving appointments: " . htmlspecialchars($e->getMessage());
            }
            break;
        case 'create_Appointments':

            if (isset($_POST['submit_appointment'])) {
                $patientID = $_POST['patientID'];
                $appointmentDate = $_POST['appointmentDate'];

                if (empty($appointmentDate)) {
                    $result = "Appointment date is required.";
                    break;
                }

                // Check if PatientID exists in the database
                try {
                    $query = $pdo->prepare("SELECT COUNT(*) FROM Patients WHERE PatientID = ?");
                    $query->execute([$patientID]);
                    $patientExists = $query->fetchColumn();

                    if (!$patientExists) {
                        $result = "PatientID does not exist. Please enter a valid Patient ID.";
                        break;
                    }

                    $query = $pdo->prepare("INSERT INTO Appointments (PatientID, AppointmentDate) VALUES (?, ?)");
                    $query->execute([$patientID, $appointmentDate]);
                    $result = "New appointment created successfully!";
                } catch (PDOException $e) {
                    $result = "Error creating appointment: " . htmlspecialchars($e->getMessage());
                }
            } else {

                $formHtml = '
                        <form method="post">
                            <h3>Create a New Appointment</h3>
                            <label for="patientID">Patient ID:</label>
                            <input type="text" name="patientID" id="patientID" required><br>
                            <label for="appointmentDate">Appointment Date:</label>
                            <input type="datetime-local" name="appointmentDate" id="appointmentDate" required><br>
                            <input type="hidden" name="function" value="create_Appointments">
                            <button type="submit" name="submit_appointment">Submit Appointment</button>
                        </form>';
            }
            break;
        case 'view_Billings':
            try {
                $query = $pdo->prepare("SELECT * FROM Billing");
                $query->execute();
                $billings = $query->fetchAll(PDO::FETCH_ASSOC);

                if ($billings) {
                    $result = "<h3>Billing Reports:</h3><table border='2'><tr><th>BillingID</th><th>OrderID</th><th>Billed Amount</th><th>Payment Status</th><th>Insurance Claim Status</th></tr>";
                    foreach ($billings as $report) {
                        $result .= "<tr>";
                        $result .= "<td>" . htmlspecialchars($report['BillingID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['OrderID']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['BilledAmount']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['PaymentStatus']) . "</td>";
                        $result .= "<td>" . htmlspecialchars($report['InsuranceClaimStatus']) . "</td>";
                        $result .= "</tr>";
                    }
                    $result .= "</table>";
                } else {
                    $result = "No billings found.";
                }
            } catch (PDOException $e) {
                $result = "Error retrieving billings: " . htmlspecialchars($e->getMessage());
            }
            break;
        case 'create_Billings':

            if (isset($_POST['submit_billing'])) {
                $orderID = $_POST['orderID'];
                $billedAmount = $_POST['billedAmount'];
                $paymentStatus = $_POST['paymentStatus'];
                $insuranceClaimStatus = $_POST['insuranceClaimStatus'];

                $validPaymentStatuses = ['Paid', 'Unpaid', 'Pending'];
                if (!in_array($paymentStatus, $validPaymentStatuses)) {
                    $result = "Invalid payment status selected. Please choose from 'Paid', 'Unpaid', or 'Pending'.";
                    break;
                }

                $validClaimStatuses = ['Submitted', 'Approved', 'Rejected', 'Pending'];
                if (!in_array($insuranceClaimStatus, $validClaimStatuses)) {
                    $result = "Invalid insurance claim status selected. Please choose from 'Submitted', 'Approved', 'Rejected', or 'Pending'.";
                    break;
                }

                // Check if OrderID exists in the database
                try {
                    $query = $pdo->prepare("SELECT COUNT(*) FROM Orders WHERE OrderID = ?");
                    $query->execute([$orderID]);
                    $orderExists = $query->fetchColumn();

                    if (!$orderExists) {
                        $result = "OrderID does not exist. Please enter a valid Order ID.";
                        break;
                    }

                    $query = $pdo->prepare("INSERT INTO Billing (OrderID, BilledAmount, PaymentStatus, InsuranceClaimStatus) VALUES (?, ?, ?, ?)");
                    $query->execute([$orderID, $billedAmount, $paymentStatus, $insuranceClaimStatus]);
                    $result = "New billing entry created successfully!";
                } catch (PDOException $e) {
                    $result = "Error creating billing entry: " . htmlspecialchars($e->getMessage());
                }
            } else {

                $formHtml = '
                        <form method="post">
                            <h3>Create a New Billing Entry</h3>
                            <label for="orderID">Order ID:</label>
                            <input type="text" name="orderID" id="orderID" required><br>
                            <label for="billedAmount">Billed Amount:</label>
                            <input type="text" name="billedAmount" id="billedAmount" required><br>
                            <label for="paymentStatus">Payment Status:</label>
                            <select name="paymentStatus" id="paymentStatus" required>
                                <option value="">--Select Payment Status--</option>
                                <option value="Paid">Paid</option>
                                <option value="Unpaid">Unpaid</option>
                                <option value="Pending">Pending</option>
                            </select><br>
                            <label for="insuranceClaimStatus">Insurance Claim Status:</label>
                            <select name="insuranceClaimStatus" id="insuranceClaimStatus" required>
                                <option value="">--Select Insurance Claim Status--</option>
                                <option value="Submitted">Submitted</option>
                                <option value="Approved">Approved</option>
                                <option value="Rejected">Rejected</option>
                                <option value="Pending">Pending</option>
                            </select><br>
                            <input type="hidden" name="function" value="create_Billings">
                            <button type="submit" name="submit_billing">Submit Billing</button>
                        </form>';
            }
            break;

        case 'update_info':
            if (isset($_POST['submit_update'])) {
                $newRealName = $_POST['real_name'];
                $newContactInfo = $_POST['contact_info'];
                $newPassword = $_POST['password'];
                $encryption_key = "superkey";

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
        default:
            $result = "Invalid function selected.";
            break;
    }
} else {
    $result = null;
}

?>


<h1>Select a Function to Operate</h1>
<form method="post">
    <label for="function">Choose a function:</label>
    <select name="function" id="function" required>
        <option value="">--Select a Function--</option>
        <option value="view_info">View my information</option>
        <option value="view_Catalogs">View All Test Catalogs</option>
        <option value="print_Reports">Print out Test reports</option>
        <option value="view_Appointments">View All Appointments</option>
        <option value="create_Appointments">Create New Appointments</option>
        <option value="view_Billings">View All Billings</option>
        <option value="create_Billings">Create New Billings</option>
        <option value="update_info">Change/Update my current information</option>
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