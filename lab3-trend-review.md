# Research trend mini-review

**Group:** Carl Jonsson & Adam Persson

---

## Trend: Testing AI systems

### Metamorphic Testing: A Review of Challenges and Opportunities (academic source)

This article deals with the **test oracle problem**. This problem occurs when it is challenging, impossible, or too
expensive to check if the output of a single test is correct. Examples include finding the shortest path in a large
graph, compilers, machine-learning programs, and search engines.

The authors present metamorphic testing (MT) as a way to both create new tests and check test results. Instead of 
checking each output against a known correct answer, MT checks if the outputs of several related tests follow certain 
rules. These rules are called metamorphic relations (MRs). For example, if you swap the start and end points in a
shortest-path search, the length of the path should remain unchanged. If it does not, the program must contain a fault.

The article shows that MT works well in practice. It has found real bugs in well-tested software, including over 100
bugs in the GCC and LLVM compilers. One study mentioned in the article found that only three to six different MRs
were enough to find at least 90% of the faults that a full test oracle would find. However, the authors point out
that MT reduces the oracle problem but does not fully solve it. The article also explains the key ideas behind MT,
clears up common misunderstandings, and lists open challenges, such as how to find and choose hood MRs.


## Sources

- [Metamorphic testing: A review of challenges and opportunities](https://dl.acm.org/doi/pdf/10.1145/3143561) (academic source)
- [The ML Test Score: A rubric for ML production systems](https://ieeexplore.ieee.org/abstract/document/8258038) (practitioner source)