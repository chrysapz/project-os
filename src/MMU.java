import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;

    /*/////////////////////////////////////////////////////////////////////////////////////*/
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;
    /*/////////////////////////////////////////////////////////////////////////////////////*/

    private ArrayList<ArrayList<MemorySlot>> memorySlotsNeeded = new ArrayList<ArrayList<MemorySlot>>();


    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */

        /*/////////////////////////////////////////////////////////////////////////////////////*/

        algorithm.setMemorySlotsNeeded(memorySlotsNeeded);

        int address = algorithm.fitProcess(p);
        if(address != -1){
            fit = true;

            int start = memorySlotsNeeded.get(address).get(algorithm.getSlot()).getEnd() - memorySlotsNeeded.get(address).get(algorithm.getSlot()).getStart();
            int end = memorySlotsNeeded.get(address).get(algorithm.getSlot()).getEnd() + p.getMemoryRequirements();
            MemorySlot newSlot = new MemorySlot(start,end,0,availableBlockSizes[address]);
            memorySlotsNeeded.get(address).add(newSlot);

            memorySlotsNeeded.get(address).get(algorithm.getSlot()).setStart(memorySlotsNeeded.get(address).get(algorithm.getSlot()).getEnd() - memorySlotsNeeded.get(address).get(algorithm.getSlot()).getStart());
            start=memorySlotsNeeded.get(address).get(algorithm.getSlot()).getStart();
            memorySlotsNeeded.get(address).get(algorithm.getSlot()).setEnd(memorySlotsNeeded.get(address).get(algorithm.getSlot()).getEnd() + p.getMemoryRequirements());
            end=memorySlotsNeeded.get(address).get(algorithm.getSlot()).getEnd();
/*
            System.out.println(memorySlotsNeeded.get(address).getStart());
            System.out.println(memorySlotsNeeded.get(address).getEnd());
            System.out.println(memorySlotsNeeded.get(address).getBlockStart());
            System.out.println(memorySlotsNeeded.get(address).getBlockEnd());
*/

            MemorySlot usedSlot = new MemorySlot(start, end, 0, availableBlockSizes[address] );
            usedSlot.setBlockAddress(address);
            usedSlot.setPid(p.getPCB().getPid());
            currentlyUsedMemorySlots.add(usedSlot);
            System.out.println("added " + p.getBurstTime() + " in address " + address + " and slot " + algorithm.getSlot());

            /*
            System.out.println(usedSlot.getStart());
            System.out.println(usedSlot.getEnd());
            System.out.println(usedSlot.getBlockStart());
            System.out.println(usedSlot.getBlockEnd());
            //memorySlotsNeeded.set(address,usedSlot);
            currentlyUsedMemorySlots.add(usedSlot);

             */

            /*
            for(int i=0; i<currentlyUsedMemorySlots.size(); i++)
            {
                System.out.println(currentlyUsedMemorySlots.get(address).getStart());
                System.out.println(currentlyUsedMemorySlots.get(address).getEnd());
                System.out.println(currentlyUsedMemorySlots.get(address).getBlockStart());
                System.out.println(currentlyUsedMemorySlots.get(address).getBlockEnd());
            }

             */
        }
        return fit;
    }

    public ArrayList<ArrayList<MemorySlot>> getMemorySlotsNeeded() {
        return memorySlotsNeeded;
    }


    public ArrayList<MemorySlot> getCurrentlyUsedMemorySlots() {
        return currentlyUsedMemorySlots;
    }

    public int[] getAvailableBlockSizes() {
        return availableBlockSizes;
    }


    /*/////////////////////////////////////////////////////////////////////////////////////*/

}


