import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    private static int lastAddress=-1;

    public int fitProcess(Process p) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */
      int i=lastAddress+1;
      while(i<availableBlockSizes.length)
      {
          if(p.getMemoryRequirements()<=availableBlockSizes[i])
          {
              fit=true;
              address=i;
              availableBlockSizes[address]-=p.getMemoryRequirements();
              lastAddress=address;
              System.out.println(address);
              break;
          }
          //the mod operation will help in going back to the starting block when the end is reached
          i=(i+1)%availableBlockSizes.length;
      }

      return address;             //If the process fits return the address it was stored in, if not return -1.
    }
}


