import java.util.ArrayList;

public class WorstFit extends MemoryAllocationAlgorithm {

    public WorstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p) {
        boolean fit = false;
        int address = -1;
        //int slot = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        /*/////////////////////////////////////////////////////////////////////////////////////*/


        for(int i=0; i<memorySlotsNeeded.size(); i++)         //Check every available memory block
        {
            for(int j=0; j<memorySlotsNeeded.get(i).size(); j++) {
                if (p.getMemoryRequirements() <= memorySlotsNeeded.get(i).get(j).getBlockEnd() - memorySlotsNeeded.get(i).get(j).getEnd())       //If the process fits in a memory block
                {
                    if (!memorySlotsNeeded.get(i).get(j).isOccupied()) {
                        if (!fit) {
                            fit = true;           //Update the flag that the process fits
                            address = i;        //Keep the block address where the process fits
                            slot = j;
                        } else if (memorySlotsNeeded.get(i).get(j).getBlockEnd() > memorySlotsNeeded.get(address).get(slot).getBlockEnd()) {   //Find the block with the largest available size
                            address = i;
                            slot = j;
                        }
                    }
                }
            }
        }
        System.out.println(fit);
        if(fit) {         //If the process fits
            //System.out.println(p.getBurstTime());
                   //Subtract the size required for the process from the block it was stored in

            memorySlotsNeeded.get(address).get(slot).setOccupied(true);


            /*/////////////////////////////////////////////////////////////////////////////////////*/

/*
            System.out.println(memorySlotsNeeded.get(address).getStart());
            System.out.println(memorySlotsNeeded.get(address).getEnd());
            System.out.println(memorySlotsNeeded.get(address).getBlockStart());
            System.out.println(memorySlotsNeeded.get(address).getBlockEnd());
*/

        }
        return address;             //If the process fits return the address it was stored in, if not return -1.
    }

}
