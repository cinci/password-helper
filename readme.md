# Simple password manager

Stores encrypted passwords in local file

## Setup

```
./gradlew clean build initApp
```

```
cd build/app
java -cp password-helper-1.1.jar Application
```

## Usage

- For first run select mode 'encrypt'
- Set master password - minimal length = 6 characters
- File is encrypted with this password
- After that it's possible to select read/add/delete/decrypt/list service
- Input for service value is masked so value is not visible
- 'decrypt' mode creates readable file
