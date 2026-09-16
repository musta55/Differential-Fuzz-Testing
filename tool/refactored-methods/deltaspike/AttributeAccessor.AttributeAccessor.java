public AttributeAccessor(final Method get, final Method set, final boolean presentAsTabularIfPossible) {
    this.getter = get;
    this.setter = set;
    this.presentAsTabularIfPossible = presentAsTabularIfPossible;
}