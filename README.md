# Cigarette Smokers Problem

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Concurrency](https://img.shields.io/badge/Topic-Concurrency-blue.svg)]()
[![License](https://img.shields.io/badge/License-MIT-green.svg)]()

> 🇹🇷 [Türkçe README için tıklayın](README_TR.md)

A comprehensive Java implementation demonstrating the classic **Cigarette Smokers Problem** - a fundamental synchronization problem in concurrent programming. This project showcases two different approaches: a naive solution prone to deadlock and the elegant Parnas pushers solution.

## 📖 Table of Contents

- [Problem Description](#-problem-description)
- [Solutions Overview](#-solutions-overview)
- [Repository Structure](#-repository-structure)
- [Quick Start](#-quick-start)
- [Detailed Solutions](#-detailed-solutions)
  - [Naive Deadlock Solution](#1-naive-deadlock-solution)
  - [Parnas Pushers Solution](#2-parnas-pushers-solution)
- [Demonstration](#-demonstration)
- [Educational Use](#-educational-use)
- [References](#-references)

## 🎯 Problem Description

The **Cigarette Smokers Problem** is a classical process synchronization problem introduced by Suhas Patil in 1971. It demonstrates the limitations of semaphores in solving certain synchronization problems and the need for more sophisticated coordination mechanisms.

### Problem Setup

- **Three smokers**, each possessing an infinite supply of one ingredient:
  - Smoker 1 has **tobacco**
  - Smoker 2 has **paper**
  - Smoker 3 has **matches**
  
- **One agent** who has an infinite supply of all three ingredients

- To smoke a cigarette, a smoker needs all three ingredients:
  - Tobacco
  - Paper
  - Matches

### Problem Rules

1. The agent randomly places **two different ingredients** on the table
2. The smoker who has the third ingredient picks up the two items, makes a cigarette, and smokes it
3. The agent waits for the smoker to finish before placing another two ingredients
4. This process repeats indefinitely

### Challenge

The challenge is to coordinate the smokers and agent using only semaphores, ensuring:
- No deadlock occurs
- No starvation happens
- Proper synchronization is maintained

## 🔍 Solutions Overview

This repository implements two distinct approaches:

| Solution | Status | Description | Use Case |
|----------|--------|-------------|----------|
| **Deadlock Solution** | ⚠️ Prone to Deadlock | Naive implementation where multiple smokers can acquire materials separately | Educational - demonstrates why simple semaphore usage fails |
| **Parnas Pushers** | ✅ Deadlock-Free | Uses intermediate pusher threads and a scoreboard for proper coordination | Production-ready solution |

## 📁 Repository Structure

```
CigarattesSmokersProblem/
├── README.md                    # This file (English)
├── README_TR.md                 # Turkish documentation
├── deadlock-solution/           # Naive, deadlock-prone implementation
│   ├── Agent.java              # Agent that places materials
│   ├── Smoker.java             # Smoker threads (naive approach)
│   ├── SmokerType.java         # Enum for smoker types
│   ├── DeadlockSolution.java   # Main class with deadlock watchdog
│   └── README.md               # Solution-specific documentation
└── parnas-solution/             # Parnas pushers approach (deadlock-free)
    ├── Agent.java              # Agent that places materials
    ├── Smoker.java             # Smoker threads
    ├── SmokerType.java         # Enum for smoker types
    ├── PusherTobacco.java      # Pusher for tobacco
    ├── PusherPaper.java        # Pusher for paper
    ├── PusherMatch.java        # Pusher for matches
    ├── Scoreboard.java         # Shared state tracker
    ├── Main.java               # Main class
    └── README.md               # Solution-specific documentation
```

## 🚀 Quick Start

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Terminal or Command Prompt

### Running the Deadlock Solution

```bash
cd deadlock-solution
javac *.java
java DeadlockSolution
```

**Expected Behavior:** The program will run for a few iterations, then likely deadlock. A watchdog thread will detect the deadlock and print thread states.

### Running the Parnas Pushers Solution

```bash
cd parnas-solution
javac *.java
java Main
```

**Expected Behavior:** The program runs indefinitely without deadlock, demonstrating proper synchronization.

### Running on Windows (PowerShell)

```powershell
# Deadlock solution
cd deadlock-solution; javac *.java; java DeadlockSolution

# Parnas solution
cd parnas-solution; javac *.java; java Main
```

## 🔬 Detailed Solutions

### 1. Naive Deadlock Solution

**Location:** `deadlock-solution/`

#### How It Works

Each smoker attempts to acquire two materials independently using separate semaphores:

```java
// Simplified concept
if (hasTobacco) {
    acquire(paper);    // Might get paper
    acquire(match);    // Waiting for match...
}
```

#### Why It Fails

**Deadlock Scenario:**

1. Agent places **TOBACCO + PAPER** on the table
2. `Smoker-Tobacco` acquires **PAPER**, waits for **MATCH**
3. `Smoker-Match` acquires **TOBACCO**, waits for **PAPER**
4. Both smokers are blocked waiting for each other's resources
5. The `tableEmpty` semaphore is never released
6. Agent cannot place new materials → **DEADLOCK**

#### Key Components

- **Agent:** Randomly selects and places two ingredients
- **Smokers:** Each tries to acquire the two ingredients they need
- **Deadlock Watchdog:** Monitors system state and reports when deadlock occurs

#### Sample Output

```
[Agent] puts TOBACCO + PAPER
[Smoker-Match] acquires TOBACCO
[Smoker-Tobacco] acquires PAPER

*** No progress: table full, material semaphores waiting; high deadlock probability ***
Agent -> WAITING
Smoker-Tobacco -> WAITING
Smoker-Paper -> WAITING
Smoker-Match -> WAITING
```

### 2. Parnas Pushers Solution

**Location:** `parnas-solution/`

#### How It Works

This solution introduces **intermediate pusher threads** and a **scoreboard** to track which materials are on the table:

```
Agent → Materials → Pushers → Scoreboard → Correct Smoker
```

#### Architecture

1. **Agent:** Places two random ingredients and waits for acknowledgment
2. **Pushers (3 threads):** One for each material type
   - Monitor their respective material semaphore
   - Update the scoreboard when a material arrives
   - Wake the correct smoker when both needed materials are present
3. **Scoreboard:** Thread-safe shared state tracking which materials are available
4. **Smokers (3 threads):** Wait for their specific signal, then make and smoke cigarette

#### Step-by-Step Flow

```
1. Agent acquires agentSem → Places TOBACCO + PAPER
2. PusherTobacco wakes up → Updates scoreboard (tobacco = true)
3. PusherPaper wakes up → Updates scoreboard (paper = true)
4. PusherPaper sees: tobacco=true, paper=true → Wakes Smoker-Match
5. Smoker-Match makes cigarette → Releases agentSem
6. Agent can now place next materials → Cycle continues without deadlock
```

#### Key Components

- **Semaphore agentSem:** Controls agent's ability to place new materials
- **Material semaphores (3):** Signal pushers when materials arrive
- **Smoker semaphores (3):** Signal specific smoker to wake up
- **Mutex:** Protects scoreboard from race conditions
- **Scoreboard:** Tracks current table state

#### Sample Output

```
[Agent] puts TOBACCO + PAPER
[Pusher-Tobacco] updates scoreboard
[Pusher-Paper] sees combination → wakes Smoker-Match
[Smoker-Match] makes cigarette and smokes
[Agent] puts PAPER + MATCH
[Pusher-Paper] updates scoreboard
[Pusher-Match] sees combination → wakes Smoker-Tobacco
[Smoker-Tobacco] makes cigarette and smokes
...continues indefinitely...
```

## 🎓 Demonstration

### For Presentations or Teaching

1. **Start with the Deadlock Solution:**
   ```bash
   cd deadlock-solution && javac *.java && java DeadlockSolution
   ```
   - Observe the output showing initial iterations
   - Wait for deadlock detection message
   - Discuss thread states and why the deadlock occurred

2. **Switch to Parnas Solution:**
   ```bash
   cd ../parnas-solution && javac *.java && java Main
   ```
   - Show continuous, deadlock-free execution
   - Highlight the role of pushers and scoreboard
   - Explain how proper coordination prevents deadlock

3. **Key Discussion Points:**
   - Limitations of simple semaphore-based solutions
   - Importance of intermediate coordination mechanisms
   - Race conditions and how mutex prevents them
   - Difference between deadlock and starvation

## 📚 Educational Use

This project is ideal for:

- **Operating Systems courses** - Demonstrates process synchronization
- **Concurrent Programming courses** - Shows practical semaphore usage
- **Software Engineering courses** - Illustrates design patterns for coordination
- **Interview preparation** - Classic problem frequently asked in technical interviews

### Learning Objectives

Students will understand:
- How semaphores work in practice
- Why naive synchronization can lead to deadlock
- Importance of proper resource coordination
- Thread communication patterns
- Critical section protection with mutexes

## 📖 References

1. Patil, S. S. (1971). "Limitations and capabilities of Dijkstra's semaphore primitives for coordination among processes"
2. Parnas, D. L. (1975). "On a solution to the cigarette smoker's problem (without conditional statements)"
3. [Little Book of Semaphores - Allen B. Downey](https://greenteapress.com/wp/semaphores/)
4. [Operating System Concepts - Silberschatz, Galvin, Gagne](https://www.os-book.com/)

## 📄 License

This project is open source and available for educational purposes.

## 🤝 Contributing

Feel free to open issues or submit pull requests for improvements or bug fixes.

---

**Note:** This implementation is designed for educational purposes to demonstrate concurrency concepts. The deadlock solution intentionally shows problematic behavior to illustrate what to avoid in production code.