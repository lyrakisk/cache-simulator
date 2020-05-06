# Sprint Review (05/05/2020)

## Problems encountered during last sprint.
### 1
**Problem:** During the previous week, the workload was not divided properly. Two members of the team did not have enough work to do.  
**Cause:**  Last week was part of the research phase of the project. As a result, the way the parser and policies would be implemented and communicate with each other, had to change multiple times. This made it hard to parallelize the tasks that had to be completed.  
**Solution:** The application now has a stable structure, that enables all 4 members of the team to work in parallel.

## What each team member will work on during the week

### Everyone
- Research a way to make the Record data structure flexible. The Parser should be able to parse many different trace files, so having a subclass for each one of them, is not a scalable solution.
- Consider: Protocol Buffers, Column-oriented storage.

### Kostas
New feature: users must be able to modify a configuration file to choose which policy they want to simulate

### Ivaylo
Fix bug in LFU policy.

### Ioanna
Choose two trace files and implement the corresponding trace parsers.

### Gancho, Ivaylo
Implement ARC policy