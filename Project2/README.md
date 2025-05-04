# How to compile and run
navigate to `Project2/`

compile: `javac -d bin/ src/p2main/*.java`

run: `java -cp bin/ p2main.Election`

# How to correctly input ballot file names
Ballot file names should be relative to the `Project2/` directory. For example, a valid input would be `testing/stv/elimination.csv`.


# User Input Required at Runtime
When running the program, a file selection pop up will appear for the user to choose ballot file(s).

### Additional input if STV ballot file(s) are chosen:
- Enter a file name for generate an audit file:
- Choose whether to shuffle ballots before processing: 1 - true to shuffle, 0 - false to shuffle