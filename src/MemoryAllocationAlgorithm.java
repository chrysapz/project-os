import java.util.ArrayList;

public abstract class MemoryAllocationAlgorithm {

    protected final int[] availableBlockSizes;
    protected ArrayList<MemorySlot> currentlyUsedMemorySlots;
    protected ArrayList<ArrayList<MemorySlot>> memorySlotsNeeded = new ArrayList<ArrayList<MemorySlot>>();

    /*/////////////////////////////////////////////////////////////////////////////////////*/
    protected int slot;
    /*/////////////////////////////////////////////////////////////////////////////////////*/

    public MemoryAllocationAlgorithm(int[] availableBlockSizes) {
        this.availableBlockSizes = availableBlockSizes;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    public abstract int fitProcess(Process p);


    /*/////////////////////////////////////////////////////////////////////////////////////*/

    public void setMemorySlotsNeeded(ArrayList<ArrayList<MemorySlot>> memorySlotsNeeded) {
        this.memorySlotsNeeded = memorySlotsNeeded;
    }

    public int getSlot() {
        return slot;
    }

    /*/////////////////////////////////////////////////////////////////////////////////////*/
}
