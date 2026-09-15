# Lab Reflection — WalkMates

> One per lab. Keep it **short and specific** — this is graded for *understanding*, not length.
> Half a page to a page is plenty. Bullet points are fine.
>
> **Before writing:** copy this file to `reflections/labN-reflection.md`, replacing `N` with the
> lab number. Keep this template unchanged so it remains available for the next lab.

**Lab:** 1

**Pair:** Carl Jonsson & Adam Persson

**Repo commit/tag:** [GitHub Repo (latest commit: 3a88bf9)](https://github.com/Calle-J/walkmates-cajo2406)

---

### 1. What we did
A few sentences: which tests/artifacts you produced and why those, against which requirements
(cite rule IDs, e.g. FR-1.3, FR-4.4).

We produced tests for the following rule IDs: **``FR-1.1``**  **``FR-1.2``** **``FR-1.3``**  
These tests were created to make sure that e-mail, display name, and phone number
are entered correctly by the user, using proper length, characters etc.  

### 2. What we found
The most interesting thing you learned or uncovered — a boundary bug, a surviving mutant, a
covered-but-buggy path, a fallback that didn't behave, a metamorphic relation that broke.

We found an error in Seeker.java where the format for the international phone number was to short.  
The format looked like this. 
```java
 private static final Pattern PHONE = Pattern.compile("^(07\\d{8}|\\+467\\d{7})$");
```
when it should have been like this:
```java
 private static final Pattern PHONE = Pattern.compile("^(07\\d{8}|\\+467\\d{8})$");
```

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