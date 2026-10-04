public String getJavaCode() {
    StringBuilder src = new StringBuilder();
    int ncols = properties.getNcols();
    MappingProperties.DataProperties data = properties.getMappingProperties().getDataProperties();
    RowIndexStructure.IndexProperties rowIndex = properties.getRowIndexStructure().getProperties();
    ColIndexStructure.IndexProperties colIndex = properties.getColIndexStructure().getProperties();
    // example: csv
    if (data != MappingProperties.DataProperties.NOTEXIST && ((rowIndex == RowIndexStructure.IndexProperties.Identity && colIndex == ColIndexStructure.IndexProperties.Identity) || rowIndex == RowIndexStructure.IndexProperties.SeqScatter)) {
        getJavaCode(ctnValue, src, "0", true);
        src.append("row++; \n");
    } else // example: MM
    if (rowIndex == RowIndexStructure.IndexProperties.CellWiseExist && colIndex == ColIndexStructure.IndexProperties.CellWiseExist) {
        getJavaCode(ctnIndexes, src, "0", false);
        src.append("if(col < " + ncols + "){ \n");
        if (data != MappingProperties.DataProperties.NOTEXIST) {
            getJavaCode(ctnValue, src, "0", false);
        } else
            src.append(destination).append("(row, col, cellValue); \n");
        src.append("} \n");
    } else // example: LibSVM
    if (rowIndex == RowIndexStructure.IndexProperties.Identity && colIndex == ColIndexStructure.IndexProperties.CellWiseExist) {
        src.append("String strValues[] = str.split(\"" + properties.getColIndexStructure().getValueDelim() + "\"); \n");
        src.append("for(String si: strValues){ \n");
        src.append("String strIndexValue[] = si.split(\"" + properties.getColIndexStructure().getIndexDelim() + "\", -1); \n");
        src.append("if(strIndexValue.length == 2){ \n");
        src.append("col = UtilFunctions.parseToInt(strIndexValue[0]); \n");
        src.append("if(col <= " + ncols + "){ \n");
        if (this.isMatrix) {
            src.append("try{ \n");
            src.append(destination).append("(row, col, Double.parseDouble(strIndexValue[1])); \n");
            src.append("lnnz++;\n");
            src.append("} catch(Exception e){" + destination + "(row, col, 0d);} \n");
        } else {
            src.append(destination).append("(row, col, UtilFunctions.stringToObject(_props.getSchema()[col], strIndexValue[1]); \n");
        }
        src.append("} \n");
        src.append("} \n");
        src.append("} \n");
        src.append("row++; \n");
    }
    return src.toString();
}