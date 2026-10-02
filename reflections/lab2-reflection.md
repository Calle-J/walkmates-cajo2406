# Lab Reflection — WalkMates

**Lab:** 2

**Pair:** Adam and Carl

**Repo:** [GitHub Repo](https://github.com/Calle-J/walkmates-cajo2406)

---

### 1. What we did
The first step was to run **``mvn clean test jacoco:report``** to generate a report of the code coverage. This 
allowed us to record the line and branch coverage for **``PricingCalculator``**. Once the report was generated, 
we were able to identify areas of the code that were not being tested and focus our efforts on writing tests for 
those areas. Our focus was on ensuring that all branches were covered.

The next step in the process was to optimize the tests using **Mutation testing**. We ran a pitest and
saw that all mutants on **``PricingCalculator``** were killed, which led us to the next step of isolating
components with mocking. Our two main targets were **``BookingService``** and **``SeekerService``**. We wrote
three tests for **``SeekerService``** covering **success**, **failure**, and **timeout** and one test for 
**``BookingService``** verifying the confirmation notification sent on a successful booking.

The last thing we did was a **Regression selection** for the **weekend-surcharge** with a focus on identifying
those tests that were affected by the new feature. We created the prioritization based on which tests had a
connection to the pricing calculation and gave those tests a higher priority, and tests not connected lower priority.

### 2. What we found
The most interesting thing you learned or uncovered — a boundary bug, a surviving mutant, a
covered-but-buggy path, a fallback that didn't behave, a metamorphic relation that broke.

**Activity 3.3**  
A test could "cover" the surcharge line yet miss the bug if the test does not test for a 
booking of exactly 480 minutes. A booking that is more than 480 minutes would still meet
the criteria of the if-statement and thus "cover" the surcharge line. 

**Activity 4.1**  
When we ran **``mvn clean test org.pitest:pitest-maven:mutationCoverage``**, 
all mutants on **``PricingCalculator``** were killed. 


### 3. AI use (be honest — it doesn't lower your grade)
- What did you use AI for in this lab?
- **What did the AI suggest vs. what you kept or changed — and why?** (the key question)
- Anything the AI produced that you suspected was wrong or weak? How did you check?

### 4. Judgment
Where did *you* have to decide something the tools/AI couldn't decide for you? (e.g. which
equivalence classes matter, whether coverage was "enough", whether a mutant was equivalent.)

### 5. What we'd test next
If you had another hour, what's the next test or risk you'd go after?

---
