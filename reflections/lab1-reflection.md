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

### 3. AI use
We used AI to generate the different tests connected to the tables in [`lab1-analysis.md`](../lab1-analysis.md).
We created all the tables ourselves, and once the **``Equivalence Partitioning``**, **``Boundary Value Analysis``**,
and **``Decision table``** tables were done, AI helped us to generate the tests.

After the tests were generated, we reviewed them thoroughly to ensure that they were accurate and complete. The tests
were accurate overall with just some minor adjustments, and nothing felt wrong or weak.

One of the tests required some adjustments to fit in with our initial plan regarding the **``wallet top-up amount``**.

AI gave us this:
```java
@Test
    @DisplayName("Wallet top-up that would exceed 20000.00 SEK balance is rejected")
    void walletTopUpExceedingMaxBalanceIsRejected() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00); // Balance: 20 000.00
        assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(10.00)); // Would make 20 010.00
    }
```

But the final result to match the intended result was:
```java
@Test
    @DisplayName("Wallet top-up that would exceed 20000.00 SEK balance is rejected")
    void walletTopUpExceedingMaxBalanceIsRejected() {
        Seeker seeker = new Seeker("adam@example.com", "Adam", "0731231234");
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(5000.00);
        seeker.addFunds(4991.00); // Balance: 19 991.00
        assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(10.00)); // Would make 20 001.00
    }
```

### 4. Judgment
Where did *you* have to decide something the tools/AI couldn't decide for you? (e.g. which
equivalence classes matter, whether coverage was "enough", whether a mutant was equivalent.)

### 5. What we'd test next
If you had another hour, what's the next test or risk you'd go after?

---