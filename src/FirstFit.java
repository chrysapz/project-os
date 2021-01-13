import java.util.ArrayList;

public class FirstFit extends MemoryAllocationAlgorithm {

    public FirstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */
        int i=0;

        //when we've searched all the memory the while() loop stops
        while(i<memory.size())
        {
            ArrayList<MemorySlot> block = memory.get(i); //current block for which we are searching if there is place for process p

            //if p can fit in the size of the current block
            if(p.getMemoryRequirements()<=availableBlockSizes[i])
            {
                for(int j=0;j<block.size();j++)   //search the slots of the current block
                {
                    if(p.getMemoryRequirements() <= block.get(j).getEnd() - block.get(j).getStart())  //checking if the process p can fit in that slot
                    {
                        if(!currentlyUsedMemorySlots.contains(block.get(j)))   //checking if that slot is currently used by another process
                        {
                            fit=true;
                            address=i;
                            slot=j;
                            break;   //if the process fitted in a slot stop searching other slots
                        }
                    }
                }
            }
            if(fit)
                break;     //if the process fitted in a slot stop searching other blocks
            i++;
        }

        return address;
    }

}
