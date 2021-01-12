
public class MemorySlot {

    private int start; // the address where the memory slot starts
    private int end;   // the address where the memory slot ends
    private final int blockStart; // address of where the block starts
    private final int blockEnd;   // address of where the block ends
    /* The following should always hold true:
     * start >= blockStart
     * end <= blockEnd */

    private int blockAddress;       //Block address the slot is located
    private int pid;                //Id of the process stored in the slot


    public MemorySlot(int start, int end, int blockStart, int blockEnd) {
        if ((start < blockStart) || (end > blockEnd)) {
            throw new java.lang.RuntimeException("Memory access out of bounds.");
        }
        this.start = start;
        this.end = end;
        this.blockStart = blockStart;
        this.blockEnd = blockEnd;
    }

    public int getBlockStart() {
        return blockStart;
    }

    public int getBlockEnd() {
        return blockEnd;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }



    public int getBlockAddress() {
        return blockAddress;
    }

    public void setBlockAddress(int blockAddress) {
        this.blockAddress = blockAddress;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }


}
