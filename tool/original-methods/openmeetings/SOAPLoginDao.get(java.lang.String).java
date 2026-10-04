public SOAPLogin get(String hash) {
    if (hash == null) {
        return null;
    }
    try {
        //MSSql find nothing in case SID is passed as-is without wildcarting '%hash%'
        SOAPLogin sl = only(em.createNamedQuery("getSoapLoginByHash", SOAPLogin.class).setParameter("hash", '%' + hash + '%').getResultList());
        if (sl != null) {
            if (hash.equals(sl.getHash())) {
                return sl;
            } else {
                log.error("[get]: Wrong SOAPLogin was found by hash! {}", hash);
            }
        }
    } catch (Exception ex2) {
        log.error("[get]: ", ex2);
    }
    return null;
}