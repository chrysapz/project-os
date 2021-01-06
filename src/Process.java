import java.util.ArrayList;

public class Process {
    private ProcessControlBlock pcb;


    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    //-------------------------------------//

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getRunTime(){return this.runTime;}
    //-------------------------------------//

    public int getMemoryRequirements() { return memoryRequirements; }

    private int arrivalTime;
    private int burstTime;
    private int memoryRequirements;

    //-------------------------------------//
    private int runTime;                        //keeps tack of how long the process has run
    //-------------------------------------//

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
        //-------------------------------------//
        if (this.getPCB().getStartTimes().isEmpty()) this.runTime = 0;      //initialization of runtime when process runs for the first time
        //-------------------------------------//
        this.pcb.setState(ProcessState.RUNNING, currentClockTime);
    }


    public void waitInBackround(int currentClockTime) {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */

        this.pcb.setState(ProcessState.READY, currentClockTime);
    }


    public double getWaitingTime() {
        /* TODO: you need to add some code here
         * and change the return value */

        ArrayList<Integer> startTimes = this.pcb.getStartTimes();
        ArrayList<Integer> stopTimes = this.pcb.getStopTimes();
        double waiting = 0;
        if (!startTimes.isEmpty()) waiting += startTimes.get(0) - this.arrivalTime;
        for (int i = 0; startTimes.size() > (i + 1); i++)
            waiting += startTimes.get(i+1) - stopTimes.get(i);
        return waiting;
    }

    public double getResponseTime() {
        /* TODO: you need to add some code here
         * and change the return value */

        if (this.pcb.getStartTimes().isEmpty())
            return 0;
        else return (this.pcb.getStartTimes().get(0) - this.arrivalTime);
    }

    public double getTurnAroundTime() {
        /* TODO: you need to add some code here
         * and change the return value */

        return (this.burstTime + this.getWaitingTime());
    }
}

