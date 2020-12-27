
public class Process {
    private ProcessControlBlock pcb;

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    private int arrivalTime;
    private int burstTime;
    private int memoryRequirements;

    public Process(int arrivalTime, int burstTime, int memoryRequirements) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock();
    }

    public ProcessControlBlock getPCB() {
        return this.pcb;
    }

    public void run(int currentClockTime) {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process starts running */

    }


    public void wait(int currentClockTime) {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */

    }

    public double getWaitingTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return 0;
    }

    public double getResponseTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return 0;
    }

    public double getTurnAroundTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return this.getWaitingTime() + burstTime;
    }
}

