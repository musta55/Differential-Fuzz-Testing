public static IndexingCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (opcode.equalsIgnoreCase(Opcodes.RIGHT_INDEX.toString())) {
        if (parts.length == 7) {
            CPOperand in, rl, ru, cl, cu, out;
            in = new CPOperand(parts[1]);
            rl = new CPOperand(parts[2]);
            ru = new CPOperand(parts[3]);
            cl = new CPOperand(parts[4]);
            cu = new CPOperand(parts[5]);
            out = new CPOperand(parts[6]);
            if (in.getDataType() == DataType.MATRIX)
                return new MatrixIndexingCPInstruction(in, rl, ru, cl, cu, out, opcode, str);
            else if (in.getDataType() == DataType.FRAME)
                return new FrameIndexingCPInstruction(in, rl, ru, cl, cu, out, opcode, str);
            else if (in.getDataType() == DataType.LIST)
                return new ListIndexingCPInstruction(in, rl, ru, cl, cu, out, opcode, str);
            else
                throw new DMLRuntimeException("Can index only on matrices, frames, and lists.");
        } else {
            throw new DMLRuntimeException("Invalid number of operands in instruction: " + str);
        }
    } else if (opcode.equalsIgnoreCase(Opcodes.LEFT_INDEX.toString())) {
        if (parts.length == 8) {
            CPOperand lhsInput, rhsInput, rl, ru, cl, cu, out;
            lhsInput = new CPOperand(parts[1]);
            rhsInput = new CPOperand(parts[2]);
            rl = new CPOperand(parts[3]);
            ru = new CPOperand(parts[4]);
            cl = new CPOperand(parts[5]);
            cu = new CPOperand(parts[6]);
            out = new CPOperand(parts[7]);
            if (lhsInput.getDataType() == DataType.MATRIX)
                return new MatrixIndexingCPInstruction(lhsInput, rhsInput, rl, ru, cl, cu, out, opcode, str);
            else if (lhsInput.getDataType() == DataType.FRAME)
                return new FrameIndexingCPInstruction(lhsInput, rhsInput, rl, ru, cl, cu, out, opcode, str);
            else if (lhsInput.getDataType() == DataType.LIST)
                return new ListIndexingCPInstruction(lhsInput, rhsInput, rl, ru, cl, cu, out, opcode, str);
            else
                throw new DMLRuntimeException("Can index only on matrices, frames, and lists.");
        } else {
            throw new DMLRuntimeException("Invalid number of operands in instruction: " + str);
        }
    } else {
        throw new DMLRuntimeException("Unknown opcode while parsing a MatrixIndexingCPInstruction: " + str);
    }
}