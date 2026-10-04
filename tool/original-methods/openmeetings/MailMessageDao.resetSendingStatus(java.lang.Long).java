public void resetSendingStatus(Long id) {
    em.createNamedQuery("resetMailStatusById").setParameter("noneStatus", Status.NONE).setParameter("id", id).executeUpdate();
}