package org.apache.deltaspike.jsf.impl.token;

public interface PostRequestTokenMarker
{
    String POST_REQUEST_TOKEN_KEY = "dsprt";

    String POST_REQUEST_TOKEN_WITH_PREFIX_KEY = ":" + POST_REQUEST_TOKEN_KEY;
    String POST_REQUEST_TOKEN_WITH_MANUAL_PREFIX_KEY = "_" + POST_REQUEST_TOKEN_KEY;
}