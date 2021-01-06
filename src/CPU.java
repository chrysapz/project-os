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

        QuickSort(0,processes.length-1);
        int lasti=0;  //last place of the process that hasn't been added to the array list of processes at the scheduler
        int terminatedProcesses=0;  //holds the terminated processes
        Process current=null;
        do{
            //insert code for memory here
            while(lasti<processes.length && processes[lasti].getArrivalTime()==clock)
            {
                scheduler.addProcess(processes[lasti]);
                lasti++;
            }
            //is in state READY
            if (current == null)
                current = scheduler.getNextProcess();  //calls getNextProcess if current is null

            if (current != null) {

                current.run(clock);
                currentProcess = current.getPCB().getPid();
                current.setRunTime(current.getRunTime() + 1);           //increments the runtime of the process

                if (current.getRunTime() == current.getBurstTime()) {         //is true when runtime of current process has reached its burst time
                    current.getPCB().setState(ProcessState.TERMINATED, clock); //terminate current process
                    terminatedProcesses++;

                    scheduler.removeProcess(current);

                    current = null;                                                 //make the current process null
                }
                else if(scheduler instanceof RoundRobin){
                    ArrayList<Integer> startTimes = current.getPCB().getStartTimes();   //gets startTimes from PCB
                    if (clock == startTimes.get(startTimes.size() - 1) - 1) {  //is true if quantum ticks have passed since the last start time of the current process
                        current.waitInBackround(clock);

                        current = null;        //make the current process null
                    }
                }
            }

            tick();
        }while (terminatedProcesses<processes.length);

        /*
        for(int i=0; i<processes.length; i++)
        {
            System.out.println(i+1);
            System.out.println("TAT " + processes[i].getTurnAroundTime());
            System.out.println("WT " + processes[i].getWaitingTime());
            System.out.println("RT " + processes[i].getResponseTime());
            System.out.println();
        }
         */
    }

    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */
        clock++;
    }

    //QuickSort algorithm that sorts the processes based on arrival time in decreasing order
    private void QuickSort(int low, int high){
        int temp;
        if(low<high)
        {
            int pivot=processes[high].getArrivalTime();
            int i=low;
            for(int j=low; j<high; j++)
            {
                if(processes[j].getArrivalTime()<pivot)
                {
                    temp = processes[i].getArrivalTime();
                    processes[i].setArrivalTime(processes[j].getArrivalTime());
                    processes[j].setArrivalTime(temp);
                    i++;
                }
            }
            temp = processes[i].getArrivalTime();
            processes[i].setArrivalTime(processes[high].getArrivalTime());
            processes[high].setArrivalTime(temp);
            int index=i;
            QuickSort(low, index-1);
            QuickSort(index+1, high);
        }
    }
}
