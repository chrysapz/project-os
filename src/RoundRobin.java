import java.util.ArrayList;

public class RoundRobin extends Scheduler {

    private int quantum;
    //-------------------------------------//
    private int noOfProcesses;
    //-------------------------------------//

    public RoundRobin() {
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */
        //-------------------------------------//
        this.noOfProcesses = 0;
    }

    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    //-------------------------------------//
    public int getQuantum(){ return this.quantum;}

    public ArrayList getProcesses(){return this.processes;}                 //to print for debugging, delete later

    public void setProcesses(ArrayList processes){this.processes = processes;}  //to print for debugging, delete later
    //-------------------------------------//

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */

        //-------------------------------------//
        this.processes.add(p);
        this.noOfProcesses++;
        //-------------------------------------//
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */

        //-------------------------------------//
        if (processes.isEmpty()) return null;
        if (noOfProcesses != processes.size()) {        //checks if a process has been remove, so it can return the correct process
            noOfProcesses = processes.size();           //updates the number of processes
            return processes.get(0);
        }
        Process next = processes.get(0);
        processes.remove(next);                      //removes the first process and adds it to the end of the queue
        processes.add(next);
        return processes.get(0);
        //-------------------------------------//
    }
}
