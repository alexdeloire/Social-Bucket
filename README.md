# Social Bucket, the future of social media

Social Bucket is a social media application that allows users to share their thoughts and ideas with the world. You can upload images, text or even files! You can also follow your friends and see what they are up to and if you like their content, you can give them a like and even comment on their posts. You can also chat with them with our global chat feature. Social Bucket lets you load up a wallet with money so that you can pay for our custom ad service or even pay in shops that accept Social Bucket payments.

## Pre-requisites

This app works with:

Java 11 (any version above 11 should work)
Apache Maven 3.6.3

to install maven on ubuntu:
```bash
sudo apt install maven
```
## How to run

The database is deployed on Render and the application show automatically connect to it.

If you wish to use a local database, don't forget to change the database configs and run the SocialBucket.sql script.

To run the application, run the following command in the root directory of the project:
```bash
mvn clean test javafx:run
```

## How to test

To run the tests, run the following command in the root directory of the project:
```bash
mvn clean test
```

## How to generate javadoc

To generate the javadoc, run the following command in the root directory of the project:
```bash
mvn javadoc:javadoc
```
