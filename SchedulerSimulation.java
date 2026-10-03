import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

// ANSI Color Codes for enhanced terminal output
class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String WHITE = "\u001B[37m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
}

// Class representing a process that implements Runnable to be run by a thread
class Process implements Runnable {
    private String name; // Name of the process
    private int burstTime; // Total time the process requires to complete (in milliseconds)
    private int timeQuantum; // Time slice (time quantum) allowed per CPU access (in milliseconds)
    private int remainingTime; // Time left for the process to finish its execution

     // FEATURE 1: Add priority field (integer 1-10, where 10 is highest)
    private int priority; // Priority of the process (1-10, 10 being highest)

    // FEATURE 3: Fields to track waiting time
    private long creationTime; // Time when process was created (in milliseconds)
    private long totalWaitingTime; // Total time spent waiting in queue (in milliseconds)
    private long lastReadyTime; // Last time the process entered the ready queue

    // Constructor to initialize the process with name, burst time, and time quantum
    public Process(String name, int burstTime, int timeQuantum) {
        this.name = name;
        this.burstTime = burstTime;
        this.timeQuantum = timeQuantum;
        this.remainingTime = burstTime; // Initially, remaining time is equal to the burst time
        this.priority = priority; // FEATURE 1: Initialize priority
        
        // FEATURE 3: Initialize timing fields
        this.creationTime = System.currentTimeMillis(); // Record when process is created
        this.totalWaitingTime = 0; // Start with 0 waiting time
        this.lastReadyTime = this.creationTime; // Initially, process is ready at creation
    }

    // This method will be called when the thread for this process is started
    @Override
    public void run() {
        // Simulate running for either the time quantum or remaining time, whichever is smaller
        int runTime = Math.min(timeQuantum, remainingTime); // Run for the smaller of the two times
        
        // Show quantum execution starting
        String quantumBar = createProgressBar(0, 15);
        System.out.println(Colors.BRIGHT_GREEN + "  ▶ " + Colors.BOLD + Colors.CYAN + name + 
                          Colors.RESET + Colors.GREEN + " executing quantum" + Colors.RESET + 
                          " [" + runTime + "ms] ");
        
        try {
            // Simulate quantum execution with progress updates
            int steps = 5; // Number of progress updates
            int stepTime = runTime / steps;
            
            for (int i = 1; i <= steps; i++) {
                Thread.sleep(stepTime);
                int quantumProgress = (i * 100) / steps;
                quantumBar = createProgressBar(quantumProgress, 15);
                
                // Clear line and show updated progress
                System.out.print("\r  " + Colors.YELLOW + "⚡" + Colors.RESET + 
                                " Quantum progress: " + quantumBar);
            }
            System.out.println(); // New line after quantum completion
            
        } catch (InterruptedException e) {
            System.out.println(Colors.RED + "\n  ✗ " + name + " was interrupted." + Colors.RESET);
        }
        
        remainingTime -= runTime; // Deduct the run time from the remaining time
        int overallProgress = (int) (((double)(burstTime - remainingTime) / burstTime) * 100);
        String overallProgressBar = createProgressBar(overallProgress, 20);
        
        System.out.println(Colors.YELLOW + "  ⏸ " + Colors.CYAN + name + Colors.RESET + 
                          " completed quantum " + Colors.BRIGHT_YELLOW + runTime + "ms" + Colors.RESET + 
                          " │ Overall progress: " + overallProgressBar);
        System.out.println(Colors.MAGENTA + "     Remaining time: " + remainingTime + "ms" + Colors.RESET);
        
        // If the process still has remaining time, it yields CPU for the next process
        if (remainingTime > 0) {
            System.out.println(Colors.BLUE + "  ↻ " + Colors.CYAN + name + Colors.RESET + 
                              " yields CPU for context switch" + Colors.RESET);
        } else {
            // If no time is left, the process has finished its execution
            System.out.println(Colors.BRIGHT_GREEN + "  ✓ " + Colors.BOLD + Colors.CYAN + name + 
                              Colors.RESET + Colors.BRIGHT_GREEN + " finished execution!" + 
                              Colors.RESET);
        }
        System.out.println();
    }
    
    // Helper method to create a visual progress bar
    private String createProgressBar(int progress, int width) {
        int filled = (progress * width) / 100;
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            if (i < filled) {
                bar.append(Colors.GREEN + "█" + Colors.RESET);
            } else {
                bar.append(Colors.WHITE + "░" + Colors.RESET);
            }
        }
        bar.append("] ").append(progress).append("%");
        return bar.toString();
    }

    // Method to run the last process to completion, ignoring the time quantum
    public void runToCompletion() {
        try {
            // Run for the remaining time without splitting into smaller time slices
            System.out.println(Colors.BRIGHT_CYAN + "  ⚡ " + Colors.BOLD + Colors.CYAN + name + 
                              Colors.RESET + Colors.BRIGHT_CYAN + " is the last process, running to completion" + 
                              Colors.RESET + " [" + remainingTime + "ms]");
            Thread.sleep(remainingTime); // Run until completion
            remainingTime = 0; // Mark the process as completed
            System.out.println(Colors.BRIGHT_GREEN + "  ✓ " + Colors.BOLD + Colors.CYAN + name + 
                              Colors.RESET + Colors.BRIGHT_GREEN + " finished execution!" + Colors.RESET);
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println(Colors.RED + "  ✗ " + name + " was interrupted." + Colors.RESET);
        }
    }

    // Getter methods for process name, burst time, and remaining time
    public String getName() {
        return name;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

      // FEATURE 1: Getter for priority
    public int getPriority() {
        return priority;
    }
    
    // FEATURE 3: Getter for creation time
    public long getCreationTime() {
        return creationTime;
    }
    
    // FEATURE 3: Getter for total waiting time
    public long getTotalWaitingTime() {
        return totalWaitingTime;
    }
    
    // FEATURE 3: Getter for last ready time
    public long getLastReadyTime() {
        return lastReadyTime;
    }
    
    // FEATURE 3: Method to update waiting time when process is about to run
    // Call this when process starts executing to calculate how long it waited
    public void updateWaitingTime() {
        long currentTime = System.currentTimeMillis();
        long waitTime = currentTime - lastReadyTime; // Time spent waiting since last added to queue
        totalWaitingTime += waitTime;
    }
    
    // FEATURE 3: Method to set last ready time when process re-enters queue
    public void setLastReadyTime(long time) {
        this.lastReadyTime = time;
    }

    // FEATURE 3: Calculate turnaround time = waiting time + burst time
    public long getTurnaroundTime() {
        return totalWaitingTime + burstTime;
    }

    // Check if the process has finished (i.e., no remaining time)
    public boolean isFinished() {
        return remainingTime <= 0;
    }
}

public class SchedulerSimulation {

 // FEATURE 2: Static counter for context switches
    // Incremented each time a new process starts running
    private static int contextSwitchCount = 0;

     // FEATURE 3: List to store all completed processes for summary
    private static List<Process> completedProcesses = new ArrayList<>();

    public static void main(String[] args) {
        // ⚠️ IMPORTANT: Put your student ID here to seed the random number generator
        // This makes your output unique to you - DO NOT forget to change this!
        int studentID = 445052144;  // ← CHANGE THIS TO YOUR ACTUAL STUDENT ID
        
        Random random = new Random(studentID);
        
        // Define the time quantum in milliseconds (the maximum time a process gets in one round)
        // Choose a random number between 2000 and 5000 ms with a step of 1000 ms
        int timeQuantum = 2000 + random.nextInt(4) * 1000; // Random: 2000, 3000, 4000, or 5000
        
        // Generate random number of processes between 10 and 20
        int numProcesses = 10 + random.nextInt(11); // Random number between 10 and 20
        
        // Queue to manage processes in a First-In-First-Out (FIFO) order
        Queue<Thread> processQueue = new LinkedList<>();
        
        // Map to associate each thread with its respective process object
        Map<Thread, Process> processMap = new HashMap<>();
        
        // Print simulation header with elegant formatting
        System.out.println("\n" + Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╔═══════════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.BG_BLUE + Colors.BRIGHT_WHITE + Colors.BOLD + 
                          "                          CPU SCHEDULER SIMULATION                                " + 
                          Colors.RESET + Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╠═══════════════════════════════════════════════════════════════════════════════════════╣" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.YELLOW + "  ⚙ Processes:     " + Colors.RESET + Colors.BRIGHT_YELLOW + 
                          String.format("%-65s", numProcesses) + 
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.YELLOW + "  ⏱ Time Quantum:  " + Colors.RESET + Colors.BRIGHT_YELLOW + 
                          String.format("%-65s", timeQuantum + "ms") + 
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.YELLOW + "  🔑 Student ID:    " + Colors.RESET + Colors.BRIGHT_YELLOW + 
                          String.format("%-65s", studentID) + 
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╚═══════════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n");
        
        // Create 'numProcesses' number of processes
        for (int i = 1; i <= numProcesses; i++) {
            // Random burst time for each process between timeQuantum/2 and 3*timeQuantum
            int burstTime = timeQuantum/2 + random.nextInt(2 * timeQuantum + 1);
            
              // FEATURE 1: Generate random priority between 1 and 10 (10 is highest)
            int priority = 1 + random.nextInt(10); // Random number between 1 and 10

            // Create a new process object with a unique name, burst time, and the defined time quantum
            Process process = new Process("P" + i, burstTime, timeQuantum);
            
            // Add the process to the ready queue and the map
            addProcessToQueue(process, processQueue, processMap);
        }
        
        // Start of the scheduler simulation
        System.out.println(Colors.BOLD + Colors.GREEN + 
                          "╔════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.GREEN + "║" + Colors.RESET + 
                          Colors.BG_GREEN + Colors.WHITE + Colors.BOLD + 
                          "                        ▶  SCHEDULER STARTING  ◀                               " + 
                          Colors.RESET + Colors.BOLD + Colors.GREEN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.GREEN + 
                          "╚════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n");
        
        // Loop to manage the scheduling of processes
        while (!processQueue.isEmpty()) {
            // Get the next thread from the queue (FIFO)
            Thread currentThread = processQueue.poll(); // Dequeues the next thread
            
              // FEATURE 2: Increment context switch counter when a new process starts running
            contextSwitchCount++;
            
            // Retrieve the process associated with the thread from the map
            Process process = processMap.get(currentThread);

              // FEATURE 3: Update waiting time for this process before it runs
            // Calculate how long it waited in queue since it was last added
            process.updateWaitingTime();

            // Print the current process queue (list of process IDs in the queue)
            System.out.println(Colors.BOLD + Colors.MAGENTA + "┌─ Ready Queue " + "─".repeat(65) + Colors.RESET);
            System.out.print(Colors.MAGENTA + "│ " + Colors.RESET + Colors.BRIGHT_WHITE + "[" + Colors.RESET);
            int queueCount = 0;
            for (Thread thread : processQueue) {
                 process = processMap.get(thread);
                if (queueCount > 0) System.out.print(Colors.WHITE + " → " + Colors.RESET);
                System.out.print(Colors.BRIGHT_CYAN + process.getName() + Colors.RESET);
                queueCount++;
            }
            if (queueCount == 0) {
                System.out.print(Colors.YELLOW + "empty" + Colors.RESET);
            }
            System.out.println(Colors.BRIGHT_WHITE + "]" + Colors.RESET);
            System.out.println(Colors.BOLD + Colors.MAGENTA + "└" + "─".repeat(79) + Colors.RESET + "\n");
            
            // Start the thread, which will run the process for one time quantum
            currentThread.start();
            
            try {
                // Wait for the thread to finish its time quantum before continuing to the next process
                currentThread.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted.");
            }
            
            // Retrieve the process associated with the thread from the map
              process = processMap.get(currentThread);
        
            // Check if the process is not finished
            if (!process.isFinished()) {
                // If the process still has remaining time, check if there are more processes in queue
                if (!processQueue.isEmpty()) {
                    // FEATURE 3: Set last ready time when re-entering queue
                    // This marks when the process started waiting again
                    process.setLastReadyTime(System.currentTimeMillis());
                   
                    // Re-enqueue the process to give it another chance to run in the next round
                    addProcessToQueue(process, processQueue, processMap);
                } else {
                    // If this is the last process in the queue, run it to completion
                    System.out.println(Colors.BRIGHT_YELLOW + "  ⚠ " + Colors.CYAN + process.getName() + 
                                      Colors.RESET + Colors.YELLOW + " is the last process → running to completion" + 
                                      Colors.RESET);
                    process.runToCompletion(); // Run until the process completes
                     
                    // FEATURE 3: Add to completed processes list for summary
                    completedProcesses.add(process);
                
                }
            } else {
                // FEATURE 3: Process finished, add to completed list for summary
                completedProcesses.add(process);
            }
        }
        
        // End of the scheduler simulation
        System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN + 
                          "╔════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN + "║" + Colors.RESET + 
                          Colors.BG_GREEN + Colors.WHITE + Colors.BOLD + 
                          "                     ✓  ALL PROCESSES COMPLETED  ✓                            " + 
                          Colors.RESET + Colors.BOLD + Colors.BRIGHT_GREEN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_GREEN + 
                          "╚════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n");
    
    // FEATURE 2: Display total context switches at the end of simulation
        System.out.println(Colors.BOLD + Colors.BRIGHT_YELLOW + 
                          "╔══════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_YELLOW + "║" + Colors.RESET + 
                          Colors.BG_BLUE + Colors.BRIGHT_WHITE + Colors.BOLD + 
                          "                        SCHEDULER STATISTICS                                     " + 
                          Colors.RESET + Colors.BOLD + Colors.BRIGHT_YELLOW + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_YELLOW + 
                          "╠══════════════════════════════════════════════════════════════════════════════════╣" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_YELLOW + "║" + Colors.RESET + 
                          Colors.CYAN + "  🔄 Total Context Switches: " + Colors.RESET + 
                          Colors.BRIGHT_CYAN + String.format("%-52s", contextSwitchCount) + 
                          Colors.BOLD + Colors.BRIGHT_YELLOW + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_YELLOW + 
                          "╚══════════════════════════════════════════════════════════════════════════════════╝" + 
                          Colors.RESET + "\n"); 

              // FEATURE 3: Display waiting time & turnaround time summary table at the end
                displayWaitingTimeSummary();
    } 

    // Method to add a process to the queue and map, while printing a "ready" message
  // FEATURE 1: Updated to display priority in the output message
    public static void addProcessToQueue(Process process, Queue<Thread> processQueue, 
                                        Map<Thread, Process> processMap) {
        // Create a new thread to run the process
        Thread thread = new Thread(process);
        
        // Add the thread to the ready queue
        processQueue.add(thread);
        
        // Map the thread to the process, so we can track the process associated with each thread
        processMap.put(thread, process);

      // FEATURE 1: Updated output message to include priority
        System.out.println(Colors.BLUE + "  ➕ " + Colors.BOLD + Colors.CYAN + process.getName() + 
                          Colors.RESET + Colors.YELLOW + " (Priority: " + process.getPriority() + ")" + 
                          Colors.RESET + Colors.BLUE + " added to ready queue" + Colors.RESET + 
                          " │ Burst time: " + Colors.YELLOW + process.getBurstTime() + "ms" + 
                          Colors.RESET);
                                        }
 
 // FEATURE 3: Method to display waiting time & turnaround time summary table
    // Shows Process Name, Burst Time, Priority, Waiting Time, and Turnaround Time for each process
    public static void displayWaitingTimeSummary() {
        // Print table header with decorative border
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╔══════════════════════════════════════════════════════════════════════════════════╗" + 
                          Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          Colors.BG_BLUE + Colors.BRIGHT_WHITE + Colors.BOLD + 
                          "                PROCESS WAITING & TURNAROUND TIME SUMMARY                        " + 
                          Colors.RESET + Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╠══════════════════════════════════════════════════════════════════════════════════╣" + 
                          Colors.RESET);
        
        // Print column headers
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                          "  " + Colors.BOLD + Colors.BRIGHT_WHITE + 
                          String.format("%-10s", "Process") + 
                          String.format("%-12s", "Burst Time") + 
                          String.format("%-10s", "Priority") + 
                          String.format("%-15s", "Waiting Time") + 
                          String.format("%-18s", "Turnaround Time") + 
                          Colors.RESET + "   " +
                          Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
        
        System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + 
                          "╠══════════════════════════════════════════════════════════════════════════════════╣" + 
                          Colors.RESET);
        
        // Calculate totals for average calculation
        long totalWaitingTime = 0;
        long totalTurnaroundTime = 0;
        
        // Print each process's information in the table
        for (Process process : completedProcesses) {
            // FEATURE 3: Calculate turnaround time = waiting time + burst time
            long turnaroundTime = process.getTurnaroundTime();
            String waitTimeStr = process.getTotalWaitingTime() + "ms";
            String turnaroundStr = turnaroundTime + "ms";
            
            System.out.println(Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET + 
                              "  " + Colors.BRIGHT_CYAN + 
                              String.format("%-10s", process.getName()) + Colors.RESET +
                              Colors.YELLOW + 
                              String.format("%-12s", process.getBurstTime() + "ms") + Colors.RESET +
                              Colors.MAGENTA + 
                              String.format("%-10s", process.getPriority()) + Colors.RESET +
                              Colors.BRIGHT_GREEN + 
                              String.format("%-15s", waitTimeStr) + Colors.RESET +
                              Colors.BRIGHT_YELLOW + 
                              String.format("%-18s", turnaroundStr) + Colors.RESET +
                              "   " +
                              Colors.BOLD + Colors.BRIGHT_CYAN + "║" + Colors.RESET);
            
            totalWaitingTime += process.getTotalWaitingTime();
            totalTurnaroundTime += turnaroundTime;
    

  
              
        // Print a message indicating the process has entered the ready queue
        System.out.println(Colors.BLUE + "  ➕ " + Colors.BOLD + Colors.CYAN + process.getName() + 
                          Colors.RESET + Colors.BLUE + " added to ready queue" + Colors.RESET + 
                          " │ Burst time: " + Colors.YELLOW + process.getBurstTime() + "ms" + 
                          Colors.RESET);
    }
}
}
