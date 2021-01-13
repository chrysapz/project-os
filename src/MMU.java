import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    private static ArrayList<ArrayList<MemorySlot>> memory = new ArrayList<ArrayList<MemorySlot>>();       //Represents the virtual memory with the given available blocks


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


        if (memory.isEmpty()){                                        //initialize the ArrayList memory, by adding an empty slot for each block
            for (int i = 0; i < availableBlockSizes.length; i++)
            {
                ArrayList<MemorySlot> slots = new ArrayList<MemorySlot>();          //Empty slot which represents an initially empty block
                int blockStart = 0;
                if(i > 0) blockStart = availableBlockSizes[i - 1];
                MemorySlot slot = new MemorySlot(blockStart,blockStart + availableBlockSizes[i], blockStart,blockStart + availableBlockSizes[i]);
                slots.add(slot);
                memory.add(slots);
            }
        }

        algorithm.setMemory(memory);

        int address = algorithm.fitProcess(p);        //Try to allocate space for the process
        if(address != -1){                            //If the process was allocated
            ArrayList<MemorySlot> block = memory.get(address);      //Block where the process was allocated
            int slotIndex = algorithm.getSlot();                    //Slot where the process was allocated

            fit = true;

            int blockStart = 0;
            if (address > 0)
                blockStart = availableBlockSizes[address - 1];

            int start = blockStart;
            if(slotIndex > 0)            //If the slot isn't the first(or only slot) in the block
                start = block.get(slotIndex - 1).getEnd();          //Set the slot's start to begin from the end of the previous slot

            int end = start + p.getMemoryRequirements();            //Set the end to value of the slot's end plus the memory required for the current process

            MemorySlot usedSlot = new MemorySlot(start, end, blockStart, blockStart + availableBlockSizes[address]);

            usedSlot.setPid(p.getPCB().getPid());       //Set the newly allocated slot's id to the id of the process stored in it
            usedSlot.setBlockAddress(address);          //Set the newly allocated slot's block address to the address of the block the slot is located in
            block.add(slotIndex,usedSlot);

            currentlyUsedMemorySlots.add(usedSlot);     //Add the newly allocated slot to the list of the currently used slots
            algorithm.setCurrentlyUsedMemorySlots(currentlyUsedMemorySlots);

            block.get(slotIndex + 1).setStart(block.get(slotIndex).getEnd());       //Set the start of the now empty slot in the block to the end of the previous slot

        }
        return fit;
    }

    public ArrayList<ArrayList<MemorySlot>> getMemory() {
        return memory;
    }

    public ArrayList<MemorySlot> getCurrentlyUsedMemorySlots() {
        return currentlyUsedMemorySlots;
    }

    public int[] getAvailableBlockSizes() {
        return availableBlockSizes;
    }

    public MemoryAllocationAlgorithm getAlgorithm() {
        return algorithm;
    }

}

