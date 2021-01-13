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


        for(int i = 0; i< memory.size(); i++)         //Check every available memory block
        {
            ArrayList<MemorySlot> block = memory.get(i);
            if (p.getMemoryRequirements() <= availableBlockSizes[i])       //If the process fits in a memory block
            {
                for (int j = 0; j < memory.get(i).size(); j++) {       //Check every slot in the block
                    if (p.getMemoryRequirements() <= block.get(j).getEnd() - block.get(j).getStart()) {     //If the process fits in a slot

                        if (!currentlyUsedMemorySlots.contains(block.get(j))) {      //If the slot is not occupied
                            if (!fit) {             //If initially no slot was found where the process fits
                                fit = true;           //Notify the flag that the process fits
                                address = i;        //Keep the block address
                                slot = j;           //and the slot address where where the process fits

                                //Check if there is a slot larger than the one the process was allocated to up to this point
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