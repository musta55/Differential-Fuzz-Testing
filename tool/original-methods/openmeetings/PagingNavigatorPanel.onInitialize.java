@Override
protected void onInitialize() {
    super.onInitialize();
    dataView.setItemsPerPage(entitiesPerPage);
    final Form<Void> f = new Form<>("pagingForm");
    f.add(new OmPagingNavigator("navigator", dataView).setOutputMarkupId(true)).add(new DropDownChoice<>("entitiesPerPage", new PropertyModel<>(this, "entitiesPerPage"), numbers).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        long newPage = dataView.getCurrentPage() * dataView.getItemsPerPage() / entitiesPerPage;
        dataView.setItemsPerPage(entitiesPerPage);
        dataView.setCurrentPage(newPage);
        target.add(f);
        PagingNavigatorPanel.this.onEvent(target);
    })));
    add(f.setOutputMarkupId(true));
}