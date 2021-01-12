import java.util.ArrayList;

public class MMU {
    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    private ArrayList<ArrayList<MemorySlot>> memory = new ArrayList<ArrayList<MemorySlot>>();


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

        if (memory.isEmpty()){                                        //initialize the ArrayList memory, by adding an empty slot for each block
            for (int i = 0; i < availableBlockSizes.length; i++)
            {
                ArrayList<MemorySlot> slots = new ArrayList<MemorySlot>();
                int blockStart = 0;
                if(i > 0) blockStart = availableBlockSizes[i - 1];
                MemorySlot slot = new MemorySlot(blockStart,blockStart + availableBlockSizes[i], blockStart,blockStart + availableBlockSizes[i]);
                slots.add(slot);
                memory.add(slots);
            }
        }

        algorithm.setMemory(memory);

        int address = algorithm.fitProcess(p);
        if(address != -1){
            ArrayList<MemorySlot> block = memory.get(address);
            int slotIndex = algorithm.getSlot();

            fit = true;

            int blockStart = 0;
            if (address > 0)
                blockStart = availableBlockSizes[address - 1];

            int start = blockStart;
            if(block.size() > 1)
                start = block.get(slotIndex - 1).getEnd();

            int end = start + p.getMemoryRequirements();

            MemorySlot usedSlot = new MemorySlot(start, end, blockStart, blockStart + availableBlockSizes[address]);

            usedSlot.setPid(p.getPCB().getPid());
            usedSlot.setBlockAddress(address);
            block.add(slotIndex,usedSlot);

            currentlyUsedMemorySlots.add(usedSlot);
            algorithm.setCurrentlyUsedMemorySlots(currentlyUsedMemorySlots);
            System.out.println("added " + p.getBurstTime() + " in address " + address + " and slot " + algorithm.getSlot());


            block.get(slotIndex + 1).setStart(block.get(slotIndex).getEnd());

        }
        else System.out.println("process " + p.getBurstTime() + " doesn't fit");
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

    /*/////////////////////////////////////////////////////////////////////////////////////*/

}

