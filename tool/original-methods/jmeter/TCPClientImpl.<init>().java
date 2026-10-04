// default is not in range of a byte
public TCPClientImpl() {
    super();
    setEolByte(EOL_INT);
    if (useEolByte) {
        log.info("Using eolByte={}", eolByte);
    }
    setCharset(CHARSET);
    String configuredCharset = JMeterUtils.getProperty("tcp.charset");
    if (StringUtils.isEmpty(configuredCharset)) {
        log.info("Using platform default charset:{}", CHARSET);
    } else {
        log.info("Using charset:{}", configuredCharset);
    }
}