@Override
protected void onInitialize() {
    super.onInitialize();
    final PagingNavigatorPanel navPanel = new PagingNavigatorPanel("pagedPanel", dataView) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            PagedEntityListPanel.this.onEvent(target);
        }
    };
    final SearchableDataProvider<? extends IDataProviderEntity> dp = dataView.getDataProvider();
    Form<Void> searchForm = new Form<>("searchForm");
    add(searchForm.setOutputMarkupId(true));
    searchForm.add(new TextField<>("searchText", new PropertyModel<>(dp, "search")).setOutputMarkupId(true));
    BootstrapAjaxButton b = new BootstrapAjaxButton("search", new ResourceModel("714"), searchForm, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            target.add(navPanel);
            PagedEntityListPanel.this.onEvent(target);
        }
    };
    searchForm.add(b);
    searchForm.setDefaultButton(b);
    add(navPanel);
}