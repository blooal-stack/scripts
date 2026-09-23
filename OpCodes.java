public enum OpCodes {
    READ(10), WRITE(11), LOAD(20), STORE(21), 
    ADD(30), SUBTRACT(31), DIVIDE(32), MULTIPLY(33), 
    BRANCH(40), BRANCHNEG(41), BRANCHZERO(42), HALT(43);

    private final int opCode;

    OpCodes(int opCode){
        this.opCode = opCode;
    }
    public int getOpCode(){
        return opCode;
    }
}
