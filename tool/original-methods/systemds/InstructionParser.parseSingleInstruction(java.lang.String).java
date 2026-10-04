public static Instruction parseSingleInstruction(String str) {
    if (str == null || str.isEmpty())
        return null;
    ExecType et = InstructionUtils.getExecType(str);
    switch(et) {
        case CP:
        case CP_FILE:
            InstructionType cptype = InstructionUtils.getCPType(str);
            if (cptype == null)
                throw new DMLRuntimeException("Unknown CP instruction: " + str);
            return CPInstructionParser.parseSingleInstruction(cptype, str);
        case SPARK:
            InstructionType sptype = InstructionUtils.getSPType(str);
            if (sptype == null)
                throw new DMLRuntimeException("Unknown SPARK instruction: " + str);
            return SPInstructionParser.parseSingleInstruction(sptype, str);
        case GPU:
            GPUINSTRUCTION_TYPE gputype = InstructionUtils.getGPUType(str);
            if (gputype == null)
                throw new DMLRuntimeException("Unknown GPU instruction: " + str);
            return GPUInstructionParser.parseSingleInstruction(gputype, str);
        case FED:
            InstructionType fedtype = InstructionUtils.getFEDType(str);
            if (fedtype == null)
                throw new DMLRuntimeException("Unknown FEDERATED instruction: " + str);
            return FEDInstructionParser.parseSingleInstruction(fedtype, str);
        case OOC:
            InstructionType ooctype = InstructionUtils.getOOCType(str);
            if (ooctype == null)
                throw new DMLRuntimeException("Unknown OOC instruction: " + str);
            return OOCInstructionParser.parseSingleInstruction(ooctype, str);
        default:
            throw new DMLRuntimeException("Unknown execution type in instruction: " + str);
    }
}