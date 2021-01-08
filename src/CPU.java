import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */

        int lasti=0;  //last place of the process that hasn't been added to the array list of processes at the scheduler
        int terminatedProcesses=0;  //holds the terminated processes

        //-------------------------------------//
        Process current = null;
        //-------------------------------------//

        do{
            //insert code for memory here
            while(lasti < processes.length && processes[lasti].getArrivalTime() == clock)
            {
                scheduler.addProcess(processes[lasti]);
                lasti++;
            }

            //-------------------------------------//
            if (scheduler instanceof RoundRobin){

                if (current == null) current = scheduler.getNextProcess();  //calls getNextProcess if current is null

                if (current != null) {

                    current.run(clock);
                    currentProcess = current.getPCB().getPid();
                    current.setRunTime(current.getRunTime() + 1);           //increments the runtime of the process

                    ArrayList<Integer> startTimes = current.getPCB().getStartTimes();   //gets startTimes from PCB
                    if (current.getRunTime() == current.getBurstTime()) {         //is true when runtime of current process has reached its burst time
                        //terminate current process
                        terminatedProcesses++;

                        scheduler.removeProcess(current);

                        current = null;                                                 //make the current process null
                    }
                    else if (clock == startTimes.get(startTimes.size() - 1) + ((RoundRobin) scheduler).getQuantum() - 1) {  //is true if quantum ticks have passed since the last start time of the current process
                        current.waitInBackround(clock);

                        current = null;                                                 //make the current process null
                    }
                }
            }
            //-------------------------------------//
            tick();
        }
        while (terminatedProcesses < processes.length);
    }

    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */
        clock++;
    }

}
