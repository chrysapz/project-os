import java.util.ArrayList;

public class WorstFit extends MemoryAllocationAlgorithm {
    public WorstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        /*/////////////////////////////////////////////////////////////////////////////////////*/


        for (int i = 0; i < memory.size(); i++)         //Check every available memory block
        {
            ArrayList<MemorySlot> block = memory.get(i);

            for (int j = 0; j < block.size(); j++) {
                if (p.getMemoryRequirements() <= availableBlockSizes[i])       //If the process fits in a memory block
                {
                    if (p.getMemoryRequirements() <= block.get(j).getEnd() - block.get(j).getStart()) {

                        if (!currentlyUsedMemorySlots.contains(block.get(j))) {
                            if (!fit) {
                                fit = true;           //Update the flag that the process fits
                                address = i;        //Keep the block address where the process fits
                                slot = j;
                            } else if (block.get(j).getEnd() - block.get(j).getStart() > memory.get(address).get(slot).getEnd() - memory.get(address).get(slot).getStart()) {   //Find the block with the largest available size
                                address = i;
                                slot = j;
                            }
                        }
                    }
                }
            }
        }
        return address;             //If the process fits return the address it was stored in, if not return -1.
    }
}
