public void resetSendingStatus(Calendar date) {
    em.createNamedQuery("resetMailStatusByDate").setParameter("noneStatus", Status.NONE).setParameter("sendingStatus", Status.SENDING).setParameter("date", date).executeUpdate();
}