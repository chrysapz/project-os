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
            MemorySlot slot = new MemorySlot(0,mmu.getAvailableBlockSizes()[i],0,mmu.getAvailableBlockSizes()[i]);
            slots.add(slot);
            mmu.getMemory().add(slots);
        }

        /*/////////////////////////////////////////////////////////////////////////////////////*/


        QuickSort(0,processes.length-1);
        int lasti=0;  //last place of the process that hasn't been added to the array list of processes at the scheduler
        int terminatedProcesses=0;  //holds the terminated processes

        /*/////////////////////////////////////////////////////////////////////////////////////*/
        boolean fit;
        Process current=null;
        Process process;
        int currentBlockIndex = -1;
        int currentSlotIndex = -1;
        /*/////////////////////////////////////////////////////////////////////////////////////*/

        do{

            /*/////////////////////////////////////////////////////////////////////////////////////*/
            if(current==null) {
                for (int i = 0; i < priorityQueue.size(); i++) {
                    process = priorityQueue.get(i);
                    fit = mmu.loadProcessIntoRAM(process);
                    if (fit) {
                        process.getPCB().setState(ProcessState.READY,clock);
                        scheduler.addProcess(process);
                        priorityQueue.remove(process);
                    }
                }
            }
            while(lasti<processes.length && processes[lasti].getArrivalTime()==clock)
            {
                process = processes[lasti];
                System.out.println("process " + process.getBurstTime() + " arrives");
                fit = mmu.loadProcessIntoRAM(processes[lasti]);
                if (fit) {
                    process.getPCB().setState(ProcessState.READY,clock);
                    scheduler.addProcess(process);
                }
                else {
                    priorityQueue.add(process);
                }
                lasti++;
            }
            /*/////////////////////////////////////////////////////////////////////////////////////*/

            //is in state READY
            if (current == null) {
                current = scheduler.getNextProcess();  //calls getNextProcess if current is null
                System.out.println("process " + current.getBurstTime() + " starts");
            }

            if (current != null) {
                current.run(clock);
                currentProcess = current.getPCB().getPid();
                current.setRunTime(current.getRunTime() + 1);           //increments the runtime of the process
                System.out.println("runtime " + current.getRunTime());
                if (current.getRunTime() == current.getBurstTime()) {         //is true when runtime of current process has reached its burst time
                    current.getPCB().setState(ProcessState.TERMINATED, clock); //terminate current process
                    terminatedProcesses++;


                    /*/////////////////////////////////////////////////////////////////////////////////////*/
                    for(int i=0; i<mmu.getCurrentlyUsedMemorySlots().size(); i++) {
                        if (mmu.getCurrentlyUsedMemorySlots().get(i).getPid() == currentProcess) {
                                currentSlotIndex = i;
                                currentBlockIndex = mmu.getCurrentlyUsedMemorySlots().get(i).getBlockAddress();
                        }
                    }

                    int index=-1;
                    for(int i=0; i<mmu.getMemory().get(currentBlockIndex).size(); i++)
                    {
                        if(mmu.getMemory().get(currentBlockIndex).get(i) == mmu.getCurrentlyUsedMemorySlots().get(currentSlotIndex))
                            index = i;
                    }
                    MemorySlot currentSlot = mmu.getMemory().get(currentBlockIndex).get(index);
                    MemorySlot prevSlot;
                    MemorySlot nextSlot;


                    if (index > 0) {
                        prevSlot = mmu.getMemory().get(currentBlockIndex).get(index - 1);
                        if (!mmu.getCurrentlyUsedMemorySlots().contains(prevSlot)) {
                            currentSlot.setStart(prevSlot.getStart());
                            mmu.getMemory().get(currentBlockIndex).remove(prevSlot);
                            index = index - 1;
                        }
                    }
                    if (index + 1 < mmu.getMemory().get(currentBlockIndex).size()) {
                        nextSlot = mmu.getMemory().get(currentBlockIndex).get(index + 1);
                        if (!mmu.getCurrentlyUsedMemorySlots().contains(nextSlot)) {
                            currentSlot.setEnd(nextSlot.getEnd());
                            mmu.getMemory().get(currentBlockIndex).remove(nextSlot);
                        }
                    }


                    System.out.println("block " + currentBlockIndex + " has " + mmu.getMemory().get(currentBlockIndex).size() + " slots");


                    mmu.getCurrentlyUsedMemorySlots().remove(currentSlotIndex);
                    mmu.getAlgorithm().setCurrentlyUsedMemorySlots(mmu.getCurrentlyUsedMemorySlots());
                    System.out.println("removed process " + current.getBurstTime() + "'s slot from address " + currentBlockIndex);

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
