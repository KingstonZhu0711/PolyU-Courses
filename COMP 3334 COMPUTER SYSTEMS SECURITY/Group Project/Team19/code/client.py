from functions import reset_password, upload_file, list_files, delete_file, download_file, login, register,  \
    share_file, read_shared_files, get_user_role, export_audit_logs, update_email, verify_email, update_email_status, \
    email_verified, log_in_by_email,log_audit

DATABASE_NAME = 'File_System.db'  # Database name


def display_menu(user_role):
    # Display the menu options based on user role
    print("\nFunction List:")
    print("1. List all files")
    print("2. Upload a file")
    print("3. Download a file")
    print("4. Delete a file")
    print("5. Share file")
    print("6. Read shared files")
    print("7. Reset my password")
    print("8. Set/Update your email")
    print("9. Verify your email")
    print("10. Log out")
    print("11. Exit")
    if user_role == 'admin':
        print("12. Export audit logs")  # Additional option for admin


def main():
    while True:
        print("\nWelcome to the COMP3334 project presented by Team 19!")
        print("1. Login by password")
        print("2. Login by email")
        print("3. Register")
        print("4. Exit")

        choice = input("\nPlease select an option: ")

        if choice == '1' or choice == '2':
            success = False

            if choice == '1':
                success, username = login()  # Attempt login by password

            if choice == '2':
                username = input("\nPlease enter your username: ")
                if not email_verified(username):  # Check if the email is verified
                    success = False
                    print("You have not provided an email, or your email has not been verified. Please log in by password.")
                else:
                    success = log_in_by_email(username)  # Attempt login by email

            if success:
                # Get user's role first to determine privileges
                user_role = get_user_role(username)
                print(f"Welcome {username}, your role is {user_role}")

                while True:  # Loop for menu options after successful login

                    display_menu(user_role)  # Show the menu
                    menu_choice = input("\nPlease select an option: ")

                    if menu_choice == '1':
                        list_files(username)  # Call the function to list files

                    elif menu_choice == '2':
                        upload_file(username)  # Call the function to upload a file

                    elif menu_choice == '3':
                        download_file(username)  # Call the function to download a file

                    elif menu_choice == '4':
                        delete_file(username)  # Call the function to delete a file

                    elif menu_choice == '5':
                        share_file(username)  # Call the function to share a file

                    elif menu_choice == '6':
                        read_shared_files(username)  # Call the function to read shared files

                    elif menu_choice == '7':
                        current_password = input("Please enter your current password: ")
                        new_password = input("Please enter your new password: ")
                        if reset_password(username, current_password, new_password):
                            print("Password reset successful.")
                        else:
                            print("Failed to reset the password. Your input current password is incorrect, or the new password is the same as the current. Please try again.")

                    elif menu_choice == '8':
                        input_password = input("\nPlease Enter your password: ")
                        input_email = input("Please enter your email: ")
                        update_email(username, input_password, input_email)  # Update user's email

                    elif menu_choice == '9':
                        if verify_email(username):  # Verify the user's email
                            update_email_status(username)
                            print("Your email has been successfully verified. You can log in to your account from now on.")
                            log_audit("Verify Email", username, f"{username} has successfully verified their email")
                        else:
                            print("Failed to verify the email. Please try again.")
                            log_audit("Verify Email", username, f"{username} failed to verify their email")

                    elif menu_choice == '10':
                        print("\nLogging out...")
                        log_audit("LOGOUT", username, f"User {username} logged out")
                        break  # Exit the menu loop to allow re-login

                    elif menu_choice == '11':
                        print("\nExiting the program...")
                        log_audit("LOGOUT", username, f"User {username} exited")
                        return  # Exit the entire program

                    elif user_role == 'admin' and menu_choice == '12':
                        export_audit_logs()  # Admin option to export audit logs
                        log_audit("EXPORT_LOGS", username, f"Admin {username} exported the log file")

                    else:
                        print("\nInvalid choice. Please try again.")  # Handle invalid menu choice

            elif choice == '1':
                print("\nIncorrect username or password. Please try again.")

        elif choice == '3':
            if register():  # Attempt to register
                print("\nRegistration successful! You can log in now.")
            else:
                print("\nFailed to register. Please try again.")

        elif choice == '4':
            print("\nExiting the program...")
            return  # Exit the entire program

        else:
            print("\nInvalid choice. Please try again.")  # Handle invalid main menu choice


if __name__ == "__main__":
    main()  # Run the main function