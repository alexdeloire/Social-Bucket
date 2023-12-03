# Social Bucket, the future of social media

## Versions

This app works with:

Java 11 (any version above 11 should work)
Apache Maven 3.6.3

to install maven on ubuntu:
```bash
sudo apt install maven
```
## How to run

Make sure you have your PostgreSQL database running on port 5432.

**MAKE SURE THE CONFIGS ARE CORRECT IN THE CODE FOR DATABASE CONNECTION.**

Don't forget to create the database and tables in your PostgreSQL database with the SQL scripts provided.

To run the application, run the following command in the root directory of the project:
```bash
mvn clean test javafx:run
```

## How to test

To run the tests, run the following command in the root directory of the project:
```bash
mvn clean test
```

