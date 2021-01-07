import java.util.ArrayList;
import java.util.Collections;

public class FCFS extends Scheduler {

    public FCFS() {
        /* TODO: you _may_ need to add some code here */
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        //adding a process in the array list of processes then resorting the array based on arrival times
        processes.add(p);
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if(processes.isEmpty())
            return null;
        else
        {
            Process nextProcess = processes.get(0);
            return nextProcess;
        }
    }

   /* private void sorting(ArrayList<Process> processes)
    {
        //sorting processes based on arrival time
        for (int i = 0; i < processes.size() ; i++) {
            for (int j = 0; j < processes.size() -(i+1); j++)
            {
                if(processes.get(j).getArrivalTime() > processes.get(j+1).getArrivalTime()) {
                    Collections.swap(processes, i, j);
                }
            }
        }
    }*/

}

