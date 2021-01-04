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
        do{
            //insert code for memory here
            while(processes[lasti].getArrivalTime()==clock)
            {
                scheduler.addProcess(processes[lasti]);
                lasti++;
            }
            //is in state READY
            currentProcess= scheduler.getNextProcess().getPCB().getPid();

            tick();
        }while (terminatedProcesses<processes.length);
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
            temp = processes[i+1].getArrivalTime();
            processes[i+1].setArrivalTime(processes[high].getArrivalTime());
            processes[high].setArrivalTime(temp);
            int index=i+1;
            QuickSort(low, index-1);
            QuickSort(index+1, high);
        }
    }
}
