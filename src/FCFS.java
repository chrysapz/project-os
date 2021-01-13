public class FCFS extends Scheduler {

    public FCFS() {
        /* TODO: you _may_ need to add some code here */
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        processes.add(p);           //Add the process to the list of processes to be scheduled
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if(processes.isEmpty())         //If there are no processes in the scheduler
            return null;
        else
        {
            return processes.get(0);    //Dispatch the process to arrive first
        }
    }
}

