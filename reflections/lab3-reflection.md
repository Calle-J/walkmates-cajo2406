# Lab Reflection — WalkMates

**Lab:** 3

**Pair:** Adam and Carl

**Repo:** [GitHub Repo](https://github.com/Calle-J/walkmates-cajo2406)

---

### 1. What we did

A few sentences: which tests/artifacts you produced and why those, against which requirements
(cite rule IDs, e.g. FR-1.3, FR-4.4).

We extended the **``promptIncludesStructuredFields()``** test in **``MatchExplanationServiceTest.java``**, 
regarding requirement **FR-5.1**, by expanding the testing of the prompt structure to make sure it includes 
"Listing base rate" and data delimiters for the free-text description.  

To test the **FR-5.2** requirement, we created three tests: 
**``fallsBackOnLlmTimeout()``**, **``fallsBackOnNullResponse()``** and **``fallsBackOnBlankResponse()``**, 
in order to test the deterministic **fallback** and so that no exceptions are leaked. 
This was done by mocking the **``LlmClient``** to throw an ``LlmTimeoutException``, return a null/blank response,
and throw an``LlmException`` respectively. 

To test the **FR-5.3** requirement, we created two tests; one for each metamorphic relation. 
The first test was created to test irrelevant details. It was done by
creating a list with candidate-listings and run it against the **``recommendBestMatch()``** method. 
One of the candidates were then modified to contain an irrelevant sentence in its description. 
We then ran the **``recommendBestMatch()``** method again and compared the new result to the original one.  
The second test was created to test the chosen listing when the order of the list changes. 
This was done by creating a list with candidate-listings and run it against the **``recommendBestMatch()``** method, 
and then reversing the list and run it again, comparing the two outcomes. 

### 2. What we found

The most interesting thing you learned or uncovered — a boundary bug, a surviving mutant, a
covered-but-buggy path, a fallback that didn't behave, a metamorphic relation that broke.

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
