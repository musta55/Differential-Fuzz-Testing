public static Instruction parseSingleInstruction(String str) {
    if (str == null || str.isEmpty())
        return null;
    ExecType et = InstructionUtils.getExecType(str);
    switch(et) {
        case CP:
        case CP_FILE:
            return parseCPInstruction(str);
        case SPARK:
            return parseSPInstruction(str);
        case GPU:
            return parseGPUInstruction(str);
        case FED:
            return parseFEDInstruction(str);
        case OOC:
            return parseOOCInstruction(str);
        default:
            throw new DMLRuntimeException("Unknown execution type in instruction: " + str);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static Instruction parseCPInstruction(String str) {
    InstructionType cptype = InstructionUtils.getCPType(str);
    if (cptype == null)
        throw new DMLRuntimeException("Unknown CP instruction: " + str);
    return CPInstructionParser.parseSingleInstruction(cptype, str);
}

private static Instruction parseSPInstruction(String str) {
    InstructionType sptype = InstructionUtils.getSPType(str);
    if (sptype == null)
        throw new DMLRuntimeException("Unknown SPARK instruction: " + str);
    return SPInstructionParser.parseSingleInstruction(sptype, str);
}

private static Instruction parseGPUInstruction(String str) {
    GPUINSTRUCTION_TYPE gputype = InstructionUtils.getGPUType(str);
    if (gputype == null)
        throw new DMLRuntimeException("Unknown GPU instruction: " + str);
    return GPUInstructionParser.parseSingleInstruction(gputype, str);
}

private static Instruction parseFEDInstruction(String str) {
    InstructionType fedtype = InstructionUtils.getFEDType(str);
    if (fedtype == null)
        throw new DMLRuntimeException("Unknown FEDERATED instruction: " + str);
    return FEDInstructionParser.parseSingleInstruction(fedtype, str);
}

private static Instruction parseOOCInstruction(String str) {
    InstructionType ooctype = InstructionUtils.getOOCType(str);
    if (ooctype == null)
        throw new DMLRuntimeException("Unknown OOC instruction: " + str);
    return OOCInstructionParser.parseSingleInstruction(ooctype, str);
}

