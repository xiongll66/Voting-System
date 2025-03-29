`quotaMet.csv`
- Droop Quota: 3
- Seats: 1
- candidate A got 3 votes, meets quota and is elected
- Winner: A

`redistributed.csv`
- Droop Quota: 2
- Seats: 2
- candidate A has 2 votes
- candidate A gets another vote so it's redistributed to B (the next preference)
- B reaches droop quota and is elected
- Winners: A, B

`elimination.csv`
- Droop Quota: 3
- Seats: 1
- none of the candidates reach droop quota after first round
- candidate C is eliminated because it has the fewest amount of votes
- redistribute the ballot to candidate A (the next preference)
- candidate A reaches droop quota and is elected.
- Winner: A

`eliminationTie.csv`
- Droop Quota: 3
- Seats: 1
- none of the candidates reach droop quota after first round
- candidates B and C are tied for elimination
- candidate C eliminated because it was the last to receive its first vote
- redistribute the ballot to candidate A
- candidate A reached quota and is elected
- Winner: A
