private static void cloneChildren(JMeterTreeNode to, JMeterTreeNode from) {
    for (Enumeration<?> enumr = from.children(); enumr.hasMoreElements(); ) {
        JMeterTreeNode child = (JMeterTreeNode) enumr.nextElement();
        JMeterTreeNode childClone = (JMeterTreeNode) child.clone();
        childClone.setUserObject(((TestElement) child.getUserObject()).clone());
        to.add(childClone);
        cloneChildren((JMeterTreeNode) to.getLastChild(), child);
    }
}