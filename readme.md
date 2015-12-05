# Simple password manager

Stores encrypted passwords in local file

## Setup

- gradlew clean build
- copy master-file.txt to same folder as password-helper-1.0-SNAPSHOT.jar (e.g. build/libs)
- java -cp password-helper-1.0-SNAPSHOT.jar Application

## Usage

- For first run select mode 'encrypt' with empty service name
- Set master password - minimal length = 6 characters
- File is encrypted with this password
- Now it's possible to select read/add/delete/decrypt
- e.g. 'read' mode and service name 'service-name'
- when adding new service value input is masked so value is not visible
- With each write operation backup file is created
- For 'decrypt' mode (use empty service name) readable file is created in same folder
