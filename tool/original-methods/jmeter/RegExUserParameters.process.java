@Override
public void process() {
    if (log.isDebugEnabled()) {
        //$NON-NLS-1$
        log.debug("{} Running up named: {}", Thread.currentThread().getName(), getName());
    }
    Sampler entry = getThreadContext().getCurrentSampler();
    if (!(entry instanceof HTTPSamplerBase)) {
        return;
    }
    Map<String, String> paramMap = buildParamsMap();
    if (paramMap == null || paramMap.isEmpty()) {
        log.info("RegExUserParameters element: {} => Referenced RegExp was not found, no parameter will be changed", getName());
        return;
    }
    HTTPSamplerBase sampler = (HTTPSamplerBase) entry;
    for (JMeterProperty jMeterProperty : sampler.getArguments()) {
        Argument arg = (Argument) jMeterProperty.getObjectValue();
        String oldValue = arg.getValue();
        // if parameter name exists in http request
        // then change its value with value obtained with regular expression
        String val = paramMap.get(arg.getName());
        if (val != null) {
            arg.setValue(val);
        }
        if (log.isDebugEnabled()) {
            log.debug("RegExUserParameters element: {} => changed parameter: {} = {}, was: {}", getName(), arg.getName(), arg.getValue(), oldValue);
        }
    }
}