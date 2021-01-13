import java.util.ArrayList;

public abstract class MemoryAllocationAlgorithm {

    protected final int[] availableBlockSizes;
    protected ArrayList<MemorySlot> currentlyUsedMemorySlots;

    protected ArrayList<ArrayList<MemorySlot>> memory = new ArrayList<ArrayList<MemorySlot>>();     //Represents the virtual memory with the given available blocks
    protected int slot;         //Stores the slot address where the last process was allocated


    public MemoryAllocationAlgorithm(int[] availableBlockSizes) {
        this.availableBlockSizes = availableBlockSizes;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    public abstract int fitProcess(Process p);

    public void setMemory(ArrayList<ArrayList<MemorySlot>> memory) {
        this.memory = memory;
    }

    public int getSlot() {
        return slot;
    }

    public void setCurrentlyUsedMemorySlots(ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        this.currentlyUsedMemorySlots = currentlyUsedMemorySlots;
    }
}