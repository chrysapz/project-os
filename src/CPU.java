import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;
    private ArrayList<Process> priorityQueue = new ArrayList<Process>();        //Stores processes that could not fit

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */


        QuickSort(0,processes.length - 1);      //If the processes given are not sorted, sort them based on arrival time
        int lasti=0;                                  //last place of the process that hasn't been added to the array list of processes at the scheduler
        int terminatedProcesses=0;                  //Counts how many processes are in state TERMINATED
        boolean fit;
        Process current = null;                     //keeps the current process
        Process process;

        do{

            if(current == null) {           //If there is no process running
                for (int i = 0; i < priorityQueue.size(); i++) {    //Check every process in priorityQueue that was unable to have memory allocated previously
                    process = priorityQueue.get(i);
                    fit = mmu.loadProcessIntoRAM(process);          //Try and find enough space to fit the process
                    if (fit) {                                      //If the process fits
                        process.getPCB().setState(ProcessState.READY,clock);        //Change its state to READY
                        scheduler.addProcess(process);              //Add it to the scheduler
                        priorityQueue.remove(process);              //Remove it from the queue
                    }
                    else{
                        int j;
                        for (j = 0; j < mmu.getAvailableBlockSizes().length; j++)                                   //Checks all the availableBlockSizes to see if the process in PriorityQueue is able to fit in any of the blocks
                            if (priorityQueue.get(i).getMemoryRequirements() <= mmu.getAvailableBlockSizes()[j])    //checks if process fits in block
                                break;                                                                              //breaks so j has value less than the length of availableBlockSizes
                        if (j == mmu.getAvailableBlockSizes().length) {                                             //if the for loop didn't break means that the process can't fit in any block
                            priorityQueue.get(i).getPCB().setState(ProcessState.TERMINATED, clock);                 //terminates the process
                            priorityQueue.remove(i);                                                                //removes it from priority queue
                            terminatedProcesses++;                                                                  //increments terminatedProcesses
                        }
                    }
                }
            }

            while(lasti < processes.length && processes[lasti].getArrivalTime() == clock)       //Check if any new processes have arrived at the current clock time
            {
                process = processes[lasti];
                fit = mmu.loadProcessIntoRAM(processes[lasti]);             //Try and find enough space to fit the process
                if (fit) {                                          //If the process fits
                    process.getPCB().setState(ProcessState.READY,clock);        //Set the process' state to READY
                    scheduler.addProcess(process);                  //Add it to the scheduler
                }
                else {                          //If there isn't enough space for the process
                    priorityQueue.add(process);        //Add it to the queue of processes waiting to be allocated
                }
                lasti++;
            }

            if (current == null) {              //If there is no process running
                current = scheduler.getNextProcess();  //signals the scheduler to get the next process
            }

            if (current != null) {          //If there is already a process running
                if (current.getPCB().getState() != ProcessState.RUNNING)        //If the process's hasn't been set to RUNNING
                    current.run(clock);
                currentProcess = current.getPCB().getPid();
                current.setRunTime(current.getRunTime() + 1);           //increments the runtime of the process
                //System.out.println("runtime " + current.getRunTime());
                if (current.getRunTime() == current.getBurstTime()) {         //If the runtime of the current process has reached its burst time
                    current.getPCB().setState(ProcessState.TERMINATED, clock); //terminate current process
                    terminatedProcesses++;

                    this.deleteFromMemory(current);        //Free the process' allocated memory

                    scheduler.removeProcess(current);      //Remove the process from the processes to be scheduled

                    current = null;                        //make the current process null
                }
                else if (scheduler instanceof RoundRobin) {         //If RoundRobin is being used
                    ArrayList<Integer> startTimes = current.getPCB().getStartTimes();   //gets startTimes from PCB
                    if (clock == startTimes.get(startTimes.size() - 1) + ((RoundRobin) scheduler).getQuantum()  - 1) {  //is true if quantum ticks have passed since the last start time of the current process
                        current.waitInBackround(clock);

                        current = null;        //make the current process null
                    }
                }
            }

            tick();
        }while (terminatedProcesses<processes.length);          //Loop while there are
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

    private void deleteFromMemory(Process current){
        int currentBlockIndex = -1;             //block index of current process
        int currentIndex = -1;              //index of current process in ArrayList currentlyUsedMemorySlots

        for(int i = 0; i < mmu.getCurrentlyUsedMemorySlots().size(); i++) {             //find current process in currentlyUsedMemorySlots

            if (mmu.getCurrentlyUsedMemorySlots().get(i).getPid() == currentProcess) {
                currentIndex = i;
                currentBlockIndex = mmu.getCurrentlyUsedMemorySlots().get(i).getBlockAddress();
                break;
            }
        }

        MemorySlot currentSlot = mmu.getCurrentlyUsedMemorySlots().get(currentIndex);
        int currentSlotIndex = mmu.getMemory().get(currentBlockIndex).indexOf(currentSlot);     //index of current slot in ArrayList memory

        MemorySlot prevSlot;
        MemorySlot nextSlot;

        //checks if neighboring slots are empty to merge them
        if (currentSlotIndex > 0) {         //checks if the current slot has a previous slot in its block
            prevSlot = mmu.getMemory().get(currentBlockIndex).get(currentSlotIndex - 1);
            if (!mmu.getCurrentlyUsedMemorySlots().contains(prevSlot)) {        //checks if the previous slot is contained in currentlyUsedMemorySlots
                currentSlot.setStart(prevSlot.getStart());                      //sets the start of the current slot to the start of the previous slot
                mmu.getMemory().get(currentBlockIndex).remove(prevSlot);        //removes previous slot
                currentSlotIndex = currentSlotIndex - 1;                        //reduces the currentSlotIndex by one since its previous slot got removed
            }
        }
        if (currentSlotIndex + 1 < mmu.getMemory().get(currentBlockIndex).size()) {     //checks if the current slot has a next slot in its block
            nextSlot = mmu.getMemory().get(currentBlockIndex).get(currentSlotIndex + 1);
            if (!mmu.getCurrentlyUsedMemorySlots().contains(nextSlot)) {
                currentSlot.setEnd(nextSlot.getEnd());
                mmu.getMemory().get(currentBlockIndex).remove(nextSlot);
            }
        }


        currentSlot.setPid(-1);                                     //changes the pid of the slot that is to be removed
        mmu.getCurrentlyUsedMemorySlots().remove(currentIndex);     //remove slot from currentlyUsedMemorySlot
        mmu.getAlgorithm().setCurrentlyUsedMemorySlots(mmu.getCurrentlyUsedMemorySlots());
    }
}

