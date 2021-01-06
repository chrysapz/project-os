import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    public NextFit(int[] availableBlockSizes) {
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
        while(!fit || i<availableBlockSizes.length){    //Exit the loop as soon as the process fits in a block
            if(p.getMemoryRequirements() <= availableBlockSizes[i]){       //If the process fits in a memory block
                fit=true;              //Update the flag that the process fits
                address = i;         //Keep the block address where the process fits
            }
            i++; //Increment through all available blocks
        }
        if(fit)         //If the process fits
            availableBlockSizes[address] -= p.getMemoryRequirements();        //Subtract the size required for the process from the block it was stored in

        return address;        //If the process fits return the address it was stored in, if not return -1
    }

}
