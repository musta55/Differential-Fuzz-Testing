/**
 * Gets {@link SamplerCreator} for content type, if none is found returns {@link DefaultSamplerCreator}
 * @param request {@link HttpRequestHdr} from which the content type should be used
 * @param pageEncodings Map of pageEncodings
 * @param formEncodings  Map of formEncodings
 * @return SamplerCreator for the content type of the <code>request</code>, or {@link DefaultSamplerCreator} when none is found
 */
public SamplerCreator getSamplerCreator(HttpRequestHdr request, Map<String, String> pageEncodings, Map<String, String> formEncodings) {
    SamplerCreator creator = samplerCreatorMap.get(request.getContentType());
    if (creator == null) {
        return DEFAULT_SAMPLER_CREATOR;
    }
    return creator;
}