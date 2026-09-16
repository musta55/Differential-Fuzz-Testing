protected AnnotatedMemberImpl(AnnotatedType<X> declaringType, M member, Class<?> memberType, AnnotationStore annotations, Type genericType, Type overriddenType) {
    super(memberType, annotations, genericType, overriddenType);
    this.declaringType = declaringType;
    javaMember = member;
}