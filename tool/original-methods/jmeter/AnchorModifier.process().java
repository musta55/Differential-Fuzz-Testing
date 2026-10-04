/**
 * Modifies an Entry object based on HTML response text.
 */
@Override
public void process() {
    JMeterContext context = getThreadContext();
    Sampler sam = context.getCurrentSampler();
    SampleResult res = context.getPreviousResult();
    HTTPSamplerBase sampler;
    HTTPSampleResult result;
    if (!(sam instanceof HTTPSamplerBase) || !(res instanceof HTTPSampleResult)) {
        log.info("Can't apply HTML Link Parser when the previous" + " sampler run is not an HTTP Request.");
        return;
    } else {
        sampler = (HTTPSamplerBase) sam;
        result = (HTTPSampleResult) res;
    }
    List<HTTPSamplerBase> potentialLinks = new ArrayList<>();
    String responseText = result.getResponseDataAsString();
    // $NON-NLS-1$
    int index = responseText.indexOf('<');
    if (index == -1) {
        index = 0;
    }
    if (log.isDebugEnabled()) {
        log.debug("Check for matches against: " + sampler.toString());
    }
    Document html = (Document) HtmlParsingUtils.getDOM(responseText.substring(index));
    addAnchorUrls(html, result, sampler, potentialLinks);
    addFormUrls(html, result, sampler, potentialLinks);
    addFramesetUrls(html, result, sampler, potentialLinks);
    if (!potentialLinks.isEmpty()) {
        HTTPSamplerBase url = potentialLinks.get(ThreadLocalRandom.current().nextInt(potentialLinks.size()));
        if (log.isDebugEnabled()) {
            log.debug("Selected: " + url.toString());
        }
        sampler.setDomain(url.getDomain());
        sampler.setPath(url.getPath());
        if (url.getMethod().equals(HTTPConstants.POST)) {
            for (JMeterProperty jMeterProperty : sampler.getArguments()) {
                Argument arg = (Argument) jMeterProperty.getObjectValue();
                modifyArgument(arg, url.getArguments());
            }
        } else {
            sampler.setArguments(url.getArguments());
        }
        sampler.setProtocol(url.getProtocol());
    } else {
        log.debug("No matches found");
    }
}