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

        for(int i=0; i<availableBlockSizes.length; i++)         //Check every available memory block
        {
            if(p.getMemoryRequirements() <= availableBlockSizes[i])       //If the process fits in a memory block
            {
                if(!fit) {
                    fit=true;           //Update the flag that the process fits
                    address = i;        //Keep the block address where the process fits
                }
                else if (availableBlockSizes[i] > availableBlockSizes[address])       //Find the block with the largest available size
                    address = i;
            }
        }
        if(fit)         //If the process fits
            availableBlockSizes[address] -= p.getMemoryRequirements();        //Subtract the size required for the process from the block it was stored in
        return address;             //If the process fits return the address it was stored in, if not return -1.
    }

}
