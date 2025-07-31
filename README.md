# Exercism Java Solutions Downloader

A Java program that iterates through all your Exercism Java track solutions and downloads iterations.

## Installation

Download and open the repository in your IDE.

## Usage

Run the main method in Main class. Input your Exercism username through the console. Your Chrome browser will be opened. While the program is running, you should manually pass Cloudflare tests when they appear. When finished, there will be a directory named "exercism-java-solutions", which contains the downloaded solutions, next to the project directory.

## Notes

- The program receives code files of an iteration in a single String and splits it using 4 consecutive newline characters ("\n\n\n\n"). Therefore, your code should not contain 4 consecutive newline characters, or else the code will be cut off in seperate files.
- The "Hello World" exercise is skipped as it doesn't share your solution publicly, and the "ETL" exercise is also omitted since it doesn't show up in the solutions page due to a glitch. You will have to download these two exercises manually.

## Performance

While testing, it took 10 minutes to download 151 solutions with the average of 2~3 iterations for each. You can see the test video here: https://drive.google.com/file/d/13xr6X3vj0_0hBIpX5gQ3XBvDeX4gdKPR/view?usp=drive_link
