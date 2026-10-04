@Override
public String toString() {
    return String.format("OAuthServer [id=%s, name=%s, iconUrl=%s, enabled=%s, clientId=%s, clientSecret=%s, requestKeyUrl=%s, requestTokenUrl=%s, requestTokenAttributes=%s, requestTokenMethod=%s, requestInfoUrl=%s, mapping=%s, isDeleted()=%s]", id, name, iconUrl, enabled, clientId, clientSecret, requestKeyUrl, requestTokenUrl, requestTokenAttributes, requestTokenMethod, requestInfoUrl, mapping, isDeleted());
}