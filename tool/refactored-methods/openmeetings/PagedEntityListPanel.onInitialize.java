@Override
protected void onInitialize() {
    super.onInitialize();
    PagingNavigatorPanel navPanel = createPagingNavigatorPanel("pagedPanel", dataView);
    SearchableDataProvider<? extends IDataProviderEntity> dp = dataView.getDataProvider();
    Form<Void> searchForm = new Form<>("searchForm");
    add(searchForm.setOutputMarkupId(true));
    searchForm.add(new TextField<>("searchText", new PropertyModel<>(dp, "search")).setOutputMarkupId(true));
    BootstrapAjaxButton b = createSearchButton("search", searchForm, navPanel);
    searchForm.add(b);
    searchForm.setDefaultButton(b);
    add(navPanel);
}
// ---- helper method(s) introduced by the refactoring ----
private PagingNavigatorPanel createPagingNavigatorPanel(String id, SearchableDataView<? extends IDataProviderEntity> dataView) {
    return new PagingNavigatorPanel(id, dataView) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            PagedEntityListPanel.this.onEvent(target);
        }
    };
}

private BootstrapAjaxButton createSearchButton(String id, Form<Void> form, PagingNavigatorPanel navPanel) {
    return new BootstrapAjaxButton(id, new ResourceModel("714"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            target.add(navPanel);
            PagedEntityListPanel.this.onEvent(target);
        }
    };
}

