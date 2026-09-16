protected PagingNavigatorPanel(String id, final DataView<?> dataView, List<Integer> numbers, int entitiesPerPage) {
    super(id);
    setOutputMarkupId(true);
    this.entitiesPerPage = entitiesPerPage;
    this.dataView = dataView;
    this.numbers = numbers;
}