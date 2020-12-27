import java.util.Collections;

public class FCFS extends Scheduler {

    private int count=0;
    public FCFS() {
        /* TODO: you _may_ need to add some code here */
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        processes.add(p);
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */

        //sorting processes based on arriva; time
        for (int i = 0; i < processes.size() ; i++) {
            for (int j = 0; j < processes.size() ; j++) {
                if(i!=j && processes.get(i).getArrivalTime() < processes.get(j).getArrivalTime())
                    Collections.swap(processes, i, j);
            }
        }

        return null;
    }

}

