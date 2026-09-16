public AttributeAccessor(final Method get, final Method set, final boolean presentAsTabularIfPossible) {
    this.setter = set;
    this.getter = get;
    this.presentAsTabularIfPossible = presentAsTabularIfPossible;
}