
public class SJF extends Scheduler {

    public SJF() {
        /* TODO: you _may_ need to add some code here */
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        processes.add(p);
        QuickSort(0,processes.size()-1);        //Sorting the processes based on burst time in decreasing order
    }


    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if(processes.size()>0)                  //If at least one process has arrived and is in READY state
        {
            Process nextProcess=processes.get(0);       //Keep the process which is next in line to be dispatched
            processes.remove(0);        //Remove the process from the queue of READY processes
            return nextProcess;             //Return the process with the shortest burst time
        }
        return null;            //If no processes have arrived
    }

    //QuickSort algorithm that sorts the processes based on burst time in decreasing order
    protected void QuickSort(int low, int high){
        int temp;
        if(low<high)
        {
            int pivot=processes.get(high).getBurstTime();
            int i=low;
            for(int j=low; j<high; j++)
            {
                if(processes.get(j).getBurstTime()<pivot)
                {
                    temp = processes.get(i).getBurstTime();
                    processes.get(i).setBurstTime(processes.get(j).getBurstTime());
                    processes.get(j).setBurstTime(temp);
                    i++;
                }
            }
            temp = processes.get(i+1).getBurstTime();
            processes.get(i+1).setBurstTime(processes.get(high).getBurstTime());
            processes.get(high).setBurstTime(temp);

            int index=i+1;
            QuickSort(low, index-1);
            QuickSort(index+1, high);
        }
    }
}

