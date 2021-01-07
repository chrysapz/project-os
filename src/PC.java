public class PC {
    /**
     * We suppose the processes come in chronological order
     * @param args
     */
    public static void main(String[] args) {
        /* TODO: You may change this method to perform any tests you like */
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(0, 4, 10),
                new Process(1, 5, 40),
                new Process(2, 2, 25),
                new Process(3, 1, 30),
                new Process(4, 6, 30),
                new Process(6, 3, 30)
        };
        final int[] availableBlockSizes = {15, 40, 10, 20}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new RoundRobin(2);
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();

    }

}
