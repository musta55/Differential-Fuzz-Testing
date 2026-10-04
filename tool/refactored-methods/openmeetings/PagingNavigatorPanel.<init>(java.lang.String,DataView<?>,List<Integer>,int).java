protected PagingNavigatorPanel(String id, DataView<?> dataView, List<Integer> numbers, int initialEntitiesPerPage) {
    super(id);
    setOutputMarkupId(true);
    this.entitiesPerPage = initialEntitiesPerPage;
    this.dataView = dataView;
    this.numbers = numbers;
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateNewPage(DataView<?> dataView, int newEntitiesPerPage) {
    return dataView.getCurrentPage() * dataView.getItemsPerPage() / newEntitiesPerPage;
}

