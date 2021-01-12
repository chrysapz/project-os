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
        int i = 0;
        while(i< memory.size() || !fit)
        {
            ArrayList<MemorySlot> block = memory.get(i);
            if (p.getMemoryRequirements() <= availableBlockSizes[i])       //If the process fits in a memory block
            {
                int j = 0;
                while(j< memory.get(i).size() || !fit) {
                    if(p.getMemoryRequirements() <= block.get(j).getEnd() - block.get(j).getStart()) {

                        if (!currentlyUsedMemorySlots.contains(block.get(j))) {
                            if (!fit) {
                                fit = true;           //Update the flag that the process fits
                                address = i;        //Keep the block address where the process fits
                                slot = j;
                            }
                        }
                    }
                    j++;
                }
            }
            i++;
        }

        return address;
    }

}
