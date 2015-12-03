# Simple password manager

Stores encrypted passwords in local file

## Run

- gradlew clean build
- copy master-file.txt to same folder as password-helper-1.0-SNAPSHOT.jar (e.g. build/libs)
- java -cp password-helper-1.0-SNAPSHOT.jar Application

## Usage

- For first run select mode 'encrypt' and service name '1'
- Set master password - minimal length = 6 characters
- File is encrypted with this password
- Now it's possible to select read/add/delete/decrypt
- e.g. 'read' mode and service name 'service-name'
- With each write operation backup file is created
- For 'decrypt' mode readable file is created in same folder
