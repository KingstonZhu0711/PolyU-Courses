import re
import time

SENSITIVE_TABLES = ["Patients"]
SENSITIVE_COLUMNS = ["RealName", "ContactInfo", "PasswordHash", "DateOfBirth", "InsuranceDetails"]

SQLI_PATTERNS = [
    r"(?i)UNION\s+SELECT",        
    r"(?i)\bOR\b.*=.*",          
    r"(?i)AND\b.*=.*",           
    r"(?i)--",                    
    r"(?i)\/\*",                  
    r"(?i)\bDROP\b",              
    r"(?i)\bALTER\b",             
]

def monitor_logs(log_file_path):
    
    with open(log_file_path, "r", encoding="utf-8", errors="ignore") as log_file:
        log_file.seek(0, 2)  
        while True:
            line = log_file.readline()
            if not line:
                time.sleep(0.1)
                continue
            
            
            if "Query" in line:
                detect_threats(line)

def detect_threats(log_line):
    
    match = re.search(r"Query\s+(.+)", log_line)
    if not match:
        return
    sql_query = match.group(1)
   
    for table in SENSITIVE_TABLES:
        if re.search(rf"\b{table}\b", sql_query, re.IGNORECASE):
            print_alert("Sensitive data access detected", log_line)

    for column in SENSITIVE_COLUMNS:
        if re.search(rf"\b{column}\b", sql_query, re.IGNORECASE):
            print_alert("Sensitive column access detected", log_line)

    
    for pattern in SQLI_PATTERNS:
        if re.search(pattern, sql_query):
            print_alert("SQL Injection attempt detected", log_line)

def print_alert(alert_type, log_line):
    
    print(f"ALERT: {alert_type}")
    print(f"Log Entry: {log_line}")
    print("-" * 50)

log_file_path = "./mysql/data/general.log"
monitor_logs(log_file_path)