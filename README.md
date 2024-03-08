CSV File Utility

Overview

This project is a utility designed to read, parse, and handle records from a CSV file, providing mechanisms for persisting the records to a database. The implementation is done using a Spring Boot application, deployed on an AWS EC2 instance, with an AWS RDS PostgreSQL database.

Functionality

CSV File Processing:
Reads and parses the provided CSV file.
Handles problematic records gracefully.
Database Persistence:
Implements a mechanism to persist valid records to an AWS RDS PostgreSQL database.
Deployment:
Deploys the application on an AWS EC2 instance, utilizing cloud technology concepts.
Security Measures:
Implements security measures to ensure data integrity and protect against potential vulnerabilities.
Metrics and Reporting

Number of Valid Records:
The utility provides the count of valid records processed from the CSV file.
Maximum Cost:
Calculates and reports the maximum cost present in the processed records.
Average Purchase Time:
Computes and reports the average purchase time planned across all valid records.

Deployment

AWS EC2:
Deploy the Spring Boot application on an AWS EC2 instance.
AWS RDS PostgreSQL:
Utilize an AWS RDS PostgreSQL database for data persistence.

http://ec2-3-255-116-31.eu-west-1.compute.amazonaws.com:8080/api/inventory/uploadcsv
