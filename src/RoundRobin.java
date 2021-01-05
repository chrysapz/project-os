import java.util.ArrayList;

public class RoundRobin extends Scheduler {

    private int quantum;

    //-------------------------------------//
    private int processIndex;
    //-------------------------------------//

    public RoundRobin() {
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */

        //-------------------------------------//
        this.processIndex = -1;
        //-------------------------------------//
    }

    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public int getQuantum(){ return this.quantum;}

    public int getProcessIndex(){ return this.processIndex;}

    public void setProcessIndex(int index){this.processIndex = index;}

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */

        //-------------------------------------//
        this.processes.add(p);
        //-------------------------------------//
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */

        //-------------------------------------//
        if (processes.isEmpty()) return null;
        processIndex = (processIndex + 1) % processes.size();
        return processes.get(processIndex);
        //-------------------------------------//
    }

    public void printProcesses(){                                       //for debugging purposes, will delete later
        for (int i = 0; i < processes.size(); i++){
            if (i == processIndex) System.out.print(" _" + processes.get(i).getBurstTime() + "_ ");
            else System.out.print(" " + processes.get(i).getBurstTime() + " ");
        }
        System.out.println(" ");
    }


}
