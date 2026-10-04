public String getErrorMessage() {
    if (_data[0] instanceof Throwable)
        return ExceptionUtils.getStackTrace((Throwable) _data[0]);
    else if (_data[0] instanceof String)
        return (String) _data[0];
    else
        return "No readable error message";
}