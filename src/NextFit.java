import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    private static int lastAddressBlock=0;  //last memory block address a process was allocated to
    private static int lastAddressSlot=-1;   //last memory slot address a process was allocated to

    public int fitProcess(Process p) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        //the search for the allocation of process p starts from block i and slot j
        int i = lastAddressBlock;
        int j = lastAddressSlot + 1;

        //this flag keeps the last slot we searched before starting to search another block
        //when this reaches the number lastAddress+1 it means we did a full circle in our memory
        int flag=-1;

        //if we do a full circle in memory the while() will stop
        while(!(i==lastAddressBlock && flag==lastAddressSlot+1)) {
            ArrayList<MemorySlot> block = memory.get(i); //current block for which we are searching if there is place for process p

            //if p can fit in the size of the current block
            if (p.getMemoryRequirements() <= availableBlockSizes[i]) {

                //if it's the first time entering the while() loop
                // start the search in the current block from the last slot a process was allocated to
                for (int k = j; k < block.size(); k++) {
                    //if the process can fit in that slot
                    if (p.getMemoryRequirements() <= block.get(k).getEnd() - block.get(k).getStart()) {
                        //and if that slot is not currently used by another process
                        if (!currentlyUsedMemorySlots.contains(block.get(k))) {
                            //then put it in that slot
                            if (!fit)
                                fit = true;
                            address = i;
                            slot = j;
                            lastAddressBlock = address;
                            lastAddressSlot = slot;
                            break;          //if the process fitted in a slot stop searching other slots

                        }
                    }
                    flag=k;
                }
            }
            if (fit)
                break;   //if the process fitted in a slot stop searching other blocks
            //the mod operation will help in going back to the starting block when the end is reached
            i = (i + 1) % availableBlockSizes.length;
            j = 0;  //when changing block, start searching from the 1st slot of memory in that block
        }
        return address;
    }
}


