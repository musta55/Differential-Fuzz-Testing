/**
 * Recursively resolve any type parameters against the type arguments in this context.
 * @param type
 * @param meta
 */
private void resolveTypeParameters(Type type, JSONObject meta) throws JSONException {
    if (type instanceof ParameterizedType) {
        handleParameterizedType((ParameterizedType) type, meta);
    } else if (type instanceof GenericArrayType) {
        handleGenericArrayType((GenericArrayType) type, meta);
    } else if (type instanceof WildcardType) {
        handleWildcardType((WildcardType) type, meta);
    } else {
        handleOtherType(type, meta);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleParameterizedType(ParameterizedType ptype, JSONObject meta) throws JSONException {
    JSONArray typeArgs = new JSONArray();
    for (Type argType : ptype.getActualTypeArguments()) {
        JSONObject argMeta = new JSONObject();
        resolveTypeParameters(argType, argMeta);
        typeArgs.put(argMeta);
    }
    meta.put("typeArgs", typeArgs);
    meta.put("type", ((Class<?>) ptype.getRawType()).getName());
    UI_TYPE uiType = UI_TYPE.getEnumFor((Class<?>) ptype.getRawType());
    if (uiType != null) {
        meta.put("uiType", uiType.getName());
    }
}

private void handleGenericArrayType(GenericArrayType gat, JSONObject meta) throws JSONException {
    JSONArray typeArgs = new JSONArray();
    JSONObject argMeta = new JSONObject();
    Type componentType = gat.getGenericComponentType();
    componentType = this.typeArguments.get(componentType.toString());
    if (componentType == null) {
        componentType = gat.getGenericComponentType();
    }
    resolveTypeParameters(componentType, argMeta);
    typeArgs.put(argMeta);
    meta.put("typeArgs", typeArgs);
    if (componentType instanceof Class) {
        meta.put("type", Array.newInstance((Class<?>) componentType, 0).getClass().getName());
    } else {
        meta.put("type", Object[].class.getName());
    }
}

private void handleWildcardType(WildcardType wtype, JSONObject meta) throws JSONException {
    meta.put("type", wtype);
    JSONObject wtMeta = new JSONObject();
    wtMeta.put("upper", getTypes(wtype.getUpperBounds()));
    wtMeta.put("lower", getTypes(wtype.getLowerBounds()));
    meta.put("typeBounds", wtMeta);
}

private void handleOtherType(Type type, JSONObject meta) throws JSONException {
    Type ta = this.typeArguments.get(type.toString());
    if (ta == null) {
        ta = type;
    }
    if (ta instanceof Class) {
        meta.put("type", ((Class<?>) ta).getName());
        UI_TYPE uiType = UI_TYPE.getEnumFor(((Class<?>) ta));
        if (uiType != null) {
            meta.put("uiType", uiType.getName());
        }
    } else if (ta instanceof ParameterizedType) {
        resolveTypeParameters(ta, meta);
    } else {
        meta.put("type", ta.toString());
    }
}

