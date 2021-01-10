import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;
    private ArrayList<Process> priorityQueue = new ArrayList<Process>();

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */

        /*/////////////////////////////////////////////////////////////////////////////////////*/

        for (int i=0; i<mmu.getAvailableBlockSizes().length; i++)
        {
            ArrayList<MemorySlot> slots = new ArrayList<MemorySlot>();
            MemorySlot slot = new MemorySlot(0,0,0,mmu.getAvailableBlockSizes()[i]);
            slots.add(slot);
            mmu.getMemorySlotsNeeded().add(slots);
        }

        /*/////////////////////////////////////////////////////////////////////////////////////*/


        QuickSort(0,processes.length-1);
        int lasti=0;  //last place of the process that hasn't been added to the array list of processes at the scheduler
        int terminatedProcesses=0;  //holds the terminated processes

        /*/////////////////////////////////////////////////////////////////////////////////////*/
        boolean fit;
        Process current=null;
        Process temp;
        int currentBlock = -1;
        int currentSlot = -1;
        /*/////////////////////////////////////////////////////////////////////////////////////*/

        do{

            /*/////////////////////////////////////////////////////////////////////////////////////*/
            if(current==null) {
                for (int i = 0; i < priorityQueue.size(); i++) {
                    temp = priorityQueue.get(i);
                    fit = mmu.loadProcessIntoRAM(temp);
                    //priorityQueue.remove(temp);
                    if (fit) {
                        scheduler.addProcess(temp);
                        priorityQueue.remove(temp);
                    }
                }
            }
            while(lasti<processes.length && processes[lasti].getArrivalTime()==clock)
            {
                    temp = processes[lasti];
                    fit = mmu.loadProcessIntoRAM(processes[lasti]);
                    if (fit)
                        scheduler.addProcess(temp);
                    else {
                        priorityQueue.add(temp);
                        //terminatedProcesses++;
                    }
                    lasti++;
            }
            /*/////////////////////////////////////////////////////////////////////////////////////*/

            //is in state READY
            if (current == null) {
                /*for (int i=0; i<scheduler.processes.size(); i++)
                    System.out.println(scheduler.processes.get(i).getBurstTime());
                 */
                current = scheduler.getNextProcess();  //calls getNextProcess if current is null
                System.out.println("process " + current.getBurstTime() + " started");
            }

            if (current != null) {
                System.out.println("runtime " + current.getRunTime());
                    current.run(clock);
                    currentProcess = current.getPCB().getPid();
                    current.setRunTime(current.getRunTime() + 1);           //increments the runtime of the process
                    if (current.getRunTime() == current.getBurstTime()) {         //is true when runtime of current process has reached its burst time
                        current.getPCB().setState(ProcessState.TERMINATED, clock); //terminate current process
                        terminatedProcesses++;


                        /*/////////////////////////////////////////////////////////////////////////////////////*/
                        //System.out.println(currentProcess);
                        for(int i=0; i<mmu.getCurrentlyUsedMemorySlots().size(); i++) {
                            //System.out.println(mmu.getCurrentlyUsedMemorySlots().get(i).getPid());
                            if (mmu.getCurrentlyUsedMemorySlots().get(i).getPid() == currentProcess) {
                                currentSlot = i;
                                currentBlock = mmu.getCurrentlyUsedMemorySlots().get(i).getBlockAddress();
                            }
                            mmu.getCurrentlyUsedMemorySlots().remove(currentSlot);
                            System.out.println("removed process " + current.getBurstTime() + "'s slot from address " + currentBlock);
                            mmu.getMemorySlotsNeeded().get(currentBlock).get(currentSlot).setStart(mmu.getMemorySlotsNeeded().get(currentBlock).get(currentSlot).getEnd() - current.getMemoryRequirements());
                            mmu.getMemorySlotsNeeded().get(currentBlock).get(currentSlot).setEnd(0);
                            mmu.getMemorySlotsNeeded().get(currentBlock).get(currentSlot).setOccupied(false);
                            mmu.getMemorySlotsNeeded().get(currentBlock).get(currentSlot).setPid(-1);
                            //mmu.getMemorySlotsNeeded().get(currentBlock).remove(currentSlot);
                        }
                        /*/////////////////////////////////////////////////////////////////////////////////////*/


                        scheduler.removeProcess(current);

                        current = null;                                                 //make the current process null
                    } else if (scheduler instanceof RoundRobin) {
                        ArrayList<Integer> startTimes = current.getPCB().getStartTimes();   //gets startTimes from PCB
                        if (clock == startTimes.get(startTimes.size() - 1) - 1) {  //is true if quantum ticks have passed since the last start time of the current process
                            current.waitInBackround(clock);

                            current = null;        //make the current process null
                        }
                    }
                }

            tick();
            System.out.println("sec " + clock);
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
        Process temp;
        if(low<high)
        {
            int pivot=processes[high].getArrivalTime();
            int i=low;
            for(int j=low; j<high; j++)
            {
                if(processes[j].getArrivalTime()<pivot)
                {
                    temp = processes[i];
                    processes[i]=processes[j];
                    processes[j]=temp;
                    i++;
                }
            }
            temp = processes[i];
            processes[i]=processes[high];
            processes[high]=temp;
            int index=i;
            QuickSort(low, index-1);
            QuickSort(index+1, high);
        }
    }


}
