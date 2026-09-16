@Override
protected void onInitialize() {
    super.onInitialize();
    dataView.setItemsPerPage(entitiesPerPage);
    Form<Void> form = new Form<>("pagingForm");
    form.add(new OmPagingNavigator("navigator", dataView).setOutputMarkupId(true)).add(new DropDownChoice<>("entitiesPerPage", new PropertyModel<>(this, "entitiesPerPage"), numbers).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        long newPage = calculateNewPage(dataView, entitiesPerPage);
        dataView.setItemsPerPage(entitiesPerPage);
        dataView.setCurrentPage(newPage);
        target.add(form);
        onEvent(target);
    })));
    add(form.setOutputMarkupId(true));
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateNewPage(DataView<?> dataView, int newEntitiesPerPage) {
    return dataView.getCurrentPage() * dataView.getItemsPerPage() / newEntitiesPerPage;
}

