# CSci 5801 – Software Engineering I, Spring 2025  

## Team 4 – Team Members
- Ly Xiong
- Kongmeng Thao  
- Elsdon Chang
- Cindy Su 

---

## Repository Overview
This repository contains deliverables for **Project 1 – Waterfall Methodology** and **Project 2 – Agile Scrum** for the Voting System project.

- **Project 1:** Voting System supporting STV (Single Transferable Vote) and Plurality Voting with multiple seats using the Waterfall methodology. Includes Software Design Document (SDD) and UML diagrams.
- **Project 2:** Enhancements to the Voting System, including Multiple Popularity Voting (MV) and support for multiple CSV files. Uses Agile Scrum practices with Product Backlogs, Sprint Backlogs, Daily Scrum logs, and testing documentation.

---

## Project 1 – Waterfall Methodology

### Overview
The Voting System supports:
- **STV (Droop)**: Single Transferable Voting with multiple candidates and ballots.
- **Plurality Voting (PV)**: Multiple seats, voters select one candidate per seat.

### Deliverables
- `SDD_TeamXX.pdf`: Software Design Document.
- UML Diagrams:
  - Class Diagram (all classes and associations)
  - Sequence Diagram for STV election process
  - Activity Diagram for Plurality election workflow

### Instructions to Run
1. Place the CSV input files in `Project1-Waterfall/SDD` or the project’s input folder.  
2. Run information can be found in readme for project 1. 
3. Verify results against specifications in the SDD.

---

## Project 2 – Agile Scrum

### Overview
Enhancements include:
- **Multiple Popularity Voting (MV)** with tie-breaking via coin toss.  
- Importing **multiple CSV files** for different voting locations.  
- Output statistics:
  - Type of election  
  - Number of seats and ballots  
  - Candidate vote counts and percentages  
  - Winners  

### Deliverables
- **Source Code:** `/Project2/src/`  
- **Testing:** `/Project2/testing/` with CSV files  
- **Documentation:** `/Project2/documentation/`  
- **Product Backlogs:** `/Project2/product_backlogs/`  
- **Sprint Backlogs:** `/Project2/sprint_backlogs/`  

### Running the Voting System
1. Place election CSV files in `/Project2/testing/`.  
2. Run information can be found in readme for project 2 
3. Review the output for results.

### Testing
- Test cases and logs in `/Project2/testing/`.  
- Use CSVs for STV, PV, and MV.  
- Logs include PBI, task, input, expected output, and pass/fail status.

### Documentation
- Generated using **Javadocs (Java)** or **Doxygen (C++)**.  
- Located in `/Project2/documentation/`.  
- Includes all classes, methods/functions, input/output parameters, return values, and exception handling.

---

## Notes
- Follow folder structure for all new files.   
- Refer to SDDs, Product Backlogs, and Sprint Backlogs for project-specific details.
