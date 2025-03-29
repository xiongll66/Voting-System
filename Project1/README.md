# How to compile and run
navigate to `Project1/`

compile: `javac -d bin/ src/main/*.java`

run: `java -cp bin/ main.Election`

# How to correctly input ballot file names
Ballot file names should be relative to the `Project1/` directory. For example, a valid input would be `testing/stv/elimination.csv`.


# User Input Required at Runtime
When run the program, user will be prompted to enter the following information in the console:

1. Type of Voting System
Enter either:
s - Single Transferable Vote
p - Plurality Vote

2. Number of Seats
Enter a positive integer.

3. Ballot File Name
Enter the relative path to the CSV file.
Example: testing/stv/elimination.csv

4. Audit file name
Enter a file name for generate an audit file:

5. Shuffle Ballots
Choose whether to shuffle ballots before processing:
1 - true to shuffle
0 - false to shuffle