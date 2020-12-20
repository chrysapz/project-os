import java.util.ArrayList;

public abstract class MemoryAllocationAlgorithm {

    protected final int[] availableBlockSizes;
    protected ArrayList<MemorySlot> currentlyUsedMemorySlots;

    public MemoryAllocationAlgorithm(int[] availableBlockSizes) {
        this.availableBlockSizes = availableBlockSizes;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    public abstract int fitProcess(Process p);
}
