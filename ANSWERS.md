# Assignment Questions

## Instructions
Answer all 4 questions with detailed explanations. Each answer should be **3-5 sentences minimum** and demonstrate your understanding of the concepts.

---

## Question 1: Thread vs Process

**Question**: Explain the difference between a **thread** and a **process**. Why did we use threads in this assignment instead of creating separate processes?

**Your Answer:**

[Write your answer here. Consider: What is a process? What is a thread? How do they differ in terms of memory, resources, creation overhead? Why are threads more suitable for this simulation?]

A process is an independent program with its own memory space, while a thread is a smaller part of a process that shares the same memory. Threads are faster and require less overhead compared to processes. In this assignment, we used threads because they are more efficient for simulating multiple tasks within the same program. Threads also make it easier to share data between tasks. In SchedulerSimulation.java, each process was implemented as a thread to simulate CPU scheduling behavior.
---

## Question 2: Ready Queue Behavior

**Question**: In Round-Robin scheduling, what happens when a process doesn't finish within its time quantum? Explain using an example from your program output.

**Your Answer:**

[Write your answer here. Describe the specific behavior - where does the process go? When does it run again? Give an example from your actual program output showing a process that was re-queued.]
In Round-Robin scheduling, if a process does not finish within its time quantum, it is moved back to the ready queue to wait for its next turn. This ensures that all processes get a fair share of CPU time. The process will continue executing in the next cycle until it finishes. This behavior helps prevent one process from using the CPU for too long. It also improves fairness and system responsiveness.
Example from my output:
```
[Paste a relevant snippet from your program output here showing a process being re-queued]
```

**Explanation of example:**
[Explain what's happening in the output snippet you pasted]

---

## Question 3: Thread States

**Question**: A thread can be in different states: **New**, **Runnable**, **Running**, **Waiting**, **Terminated**. Walk through these states for one process (P1) from your simulation.

**Your Answer:**

[Write your answer here. For each state, explain when P1 enters that state during the simulation. Use your understanding of the code to trace through the lifecycle.]

1. **New**: [When is P1 in New state?]

2. **Runnable**: [When does P1 become Runnable?]

3. **Running**: [When is P1 Running?]

4. **Waiting**: [When/why would P1 be Waiting?]

5. **Terminated**: [When is P1 Terminated?]

1. New:
P1 is in the New state when it is created but not yet started. In my simulation, this happens when the process object is initialized before calling Thread.start(). At this point, the thread exists but is not running or ready for execution.

2. Runnable:
P1 becomes Runnable after calling Thread.start(). At this stage, it is ready to run and waiting for the CPU to assign it execution time. The scheduler will place it in the ready queue.

3. Running:
P1 enters the Running state when the CPU selects it from the ready queue and starts executing its code. In my simulation, this happens during its allocated time quantum when it is actively performing its tasks.

4. Waiting:
P1 may enter the Waiting state if it is paused using Thread.sleep() or if it is waiting for another thread using Thread.join(). During this time, it is not using the CPU and cannot continue execution until the condition is resolved.

5. Terminated:
P1 reaches the Terminated state when it finishes all its execution and completes its task. At this point, the thread has no more instructions to run and cannot be restarted.




---

## Question 4: Real-World Applications

**Question**: Give **TWO** real-world examples where Round-Robin scheduling with threads would be useful. Explain why this scheduling algorithm works well for those scenarios.

**Your Answer:**

### Example 1: [Web Server]

**Description**: 
[Describe the real-world scenario or application]
A web server handles multiple user requests at the same time. It needs to process many requests without blocking others.

**Why Round-Robin works well here**: 
[Explain why Round-Robin scheduling is suitable. Consider fairness, responsiveness, predictability, etc.]
Round-Robin gives each request a fair time slice (time quantum), which ensures fairness between users. It improves responsiveness because no single request delays others. It also makes the system more predictable since every request gets a turn to run.

### Example 2: [Operating System CPU Scheduling]

**Description**: 
[Describe the real-world scenario or application]
An operating system manages multiple running processes at the same time and decides which process gets CPU time.


**Why Round-Robin works well here**: 
[Explain why Round-Robin scheduling is suitable. Consider fairness, responsiveness, predictability, etc.]
Round-Robin ensures that each process gets equal CPU time using a time quantum. If a process does not finish, it goes back to the ready queue, which prevents starvation. This improves fairness and keeps the system responsive and efficient.


---

## Summary

**Key concepts I understood through these questions:**
1. The difference between threads and processes
2. How Round-Robin scheduling works with a time quantum
3. Thread states and their lifecycle

**Concepts I need to study more:**
1. Advanced multithreading concepts
2. debugging and working with concurrent execution
